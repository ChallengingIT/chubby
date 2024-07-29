package it.challenging.torchy.controller;

import it.challenging.torchy.EmailSenderService;
import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.UserCandidatoRepository;
import it.challenging.torchy.request.*;
import it.challenging.torchy.response.JwtResponse;
import it.challenging.torchy.response.MessageResponse;
import it.challenging.torchy.security.services.JwtService;
import it.challenging.torchy.util.Constants;
import it.challenging.torchy.util.UtilLib;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/candidato/auth")
public class AuthCandidatoController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserCandidatoRepository userCandidatoRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private EmailSenderService serviceEmail;

    @Autowired
    JwtService jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthCandidatoController.class);

    @CrossOrigin(origins = "*")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateCandidato(@Valid @RequestBody CandidatoLoginRequest loginRequest) {

        logger.info("Login");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            logger.debug("Autenticazione passata");

            UserCandidato user = userCandidatoRepository.findByUsername(loginRequest.getUsername()).get();

            if (user.getExpirationDate().isBefore(LocalDateTime.now())) {
                logger.debug("Password scaduta");

                return ResponseEntity.badRequest().body(new MessageResponse("Error: password expired!"));
            }

            String jwt = jwtUtils.generateJwtToken(user);

            logger.debug("Token generato");

            List<String> roles = new ArrayList<>();

            roles.add(user.getRole().name());

            logger.debug("Login effettuato");

            return ResponseEntity.ok(new JwtResponse(jwt,
                    user.getUsername(), user.getNome(),
                    user.getCognome(),roles));
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    public ResponseEntity<?> registerCandidato(
            @Valid @RequestBody CandidatoSignupRequest signUpRequest
    ) {

        logger.info("Registrazione");

        try {

            if (userCandidatoRepository.existsByUsername(signUpRequest.getUsername())) {

                logger.debug("Username selezionato già presente");

                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            LocalDateTime expirationDate = LocalDateTime.now().plusDays(365);

            // Create new user's account
            UserCandidato user = new UserCandidato(signUpRequest.getUsername(), signUpRequest.getNome(),
                    signUpRequest.getCognome(), signUpRequest.getEmail(), signUpRequest.getCellulare(),signUpRequest.getResidenza(),
                    encoder.encode(signUpRequest.getPassword()), (byte)1, expirationDate);

            logger.debug("User generato");

            user.setRole(Role.CANDIDATO);
            userCandidatoRepository.save(user);

            logger.debug("Utenza creata");

            return ResponseEntity.ok(new MessageResponse(user.getUsername()));

        } catch(Exception e) {
            logger.error(e.toString());
        }

        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/signup/cv")
    public ResponseEntity<?> registerFileCandidato(
            @RequestParam("username") String username,
            @RequestParam("cv") MultipartFile cv
    ) {

        logger.info("Registrazione CV");

        try {

            if (userCandidatoRepository.existsByUsername(username)) {

                UserCandidato user = userCandidatoRepository.findByUsername(username).get();

                if (( null != cv.getOriginalFilename() ) && !cv.getOriginalFilename().isEmpty()) {
                    user.setFile(fileVoid(cv));
                }

                userCandidatoRepository.save(user);
            }

            logger.debug("Utenza aggiornata");

        } catch(Exception e) {
            logger.error(e.toString());
        }

        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changeRequest) {

        logger.info("Cambio Password");

        try {

            if (userCandidatoRepository.existsByUsername(changeRequest.getUsername())) {

                UserCandidato user = userCandidatoRepository.findByUsername(changeRequest.getUsername()).get();

                if (encoder.matches(changeRequest.getOldPassword(), user.getPassword())) {

                    LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

                    user.setExpirationDate(expirationDate);
                    user.setPassword(encoder.encode(changeRequest.getNewPassword()));

                }

                userCandidatoRepository.save(user);

                logger.debug("Password aggiornata");

            } else {

                logger.debug("Username non presente");

                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is not present!"));
            }
        } catch(Exception e) {
            logger.error(e.toString());
        }

        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/lost/password")
    public ResponseEntity<?> lostPassword(@Valid @RequestBody LostPasswordRequest lostRequest) {

        logger.info("Dimenticato la Password");

        try {

            if (userCandidatoRepository.existsByEmail(lostRequest.getEmail())) {

                UserCandidato user = userCandidatoRepository.findByEmail(lostRequest.getEmail()).get();

                LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

                String newPassword = UtilLib.generatePassayPassword();

                user.setExpirationDate(expirationDate);
                user.setPassword(encoder.encode(newPassword));

                Email email = UtilLib.getEmail(lostRequest.getEmail(), Constants.NOTE_LOST_PASSWORD + newPassword, Constants.OGGETTO_LOST_PASSWORD);

                serviceEmail.sendHtmlMessage(email);

                userCandidatoRepository.save(user);

                logger.debug("Password aggiornata");

            } else {

                logger.debug("Email non presente");

                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is not present!"));
            }
        } catch(Exception e) {
            logger.error(e.toString());
        }

        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody LoginRequest changeRequest) {

        logger.info("Eliminazione utente");

        try {

            if (userCandidatoRepository.existsByUsername(changeRequest.getUsername())) {

                UserCandidato user = userCandidatoRepository.findByUsername(changeRequest.getUsername()).get();

                if (encoder.matches(changeRequest.getPassword(), user.getPassword())) {

                    userCandidatoRepository.delete(user);

                    logger.debug("Utente eliminato");

                } else {
                    logger.debug("Password non uguale a quella presente");

                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Password incorrect!"));
                }

            } else {

                logger.debug("Username non presente");

                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is not present!"));
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    public File fileVoid(
            MultipartFile file
    )
            throws Exception {
        logger.debug("Creazione file");

        try {
            String         descrizione          = Objects.requireNonNull(file.getOriginalFilename()).length() <= 90 ? file.getOriginalFilename() : file.getOriginalFilename().substring(0,89);
            byte[]         arrayByte            = file.getBytes();
            String         tipo                 = file.getContentType();
            File           fileOggettoCandidato = new File();
            java.util.Date date                 = new java.util.Date();
            java.sql.Date sqlDate   = new Date(date.getTime());
            TipologiaF tipologia = new TipologiaF();

            fileOggettoCandidato.setData(arrayByte);
            fileOggettoCandidato.setDataInserimento(new java.sql.Date(sqlDate.getTime()));
            fileOggettoCandidato.setDescrizione(descrizione);
            fileOggettoCandidato.setTipo(tipo);

            tipologia.setId(1);
            fileOggettoCandidato.setTipologia(tipologia);

            return fileOggettoCandidato;

        } catch (Exception e) {
            logger.error(e.toString());

            throw new Exception(e);
        }
    }
}