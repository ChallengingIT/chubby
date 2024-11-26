package it.challenging.torchy.controller;

import it.challenging.torchy.EmailSenderService;
import it.challenging.torchy.entity.Email;
import it.challenging.torchy.entity.Role;
import it.challenging.torchy.entity.User;
import it.challenging.torchy.repository.UserRepository;
import it.challenging.torchy.request.ChangePasswordRequest;
import it.challenging.torchy.request.LoginRequest;
import it.challenging.torchy.request.LostPasswordRequest;
import it.challenging.torchy.request.SignupRequest;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private EmailSenderService serviceEmail;

    @Autowired
    JwtService jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @CrossOrigin(origins = "*")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Login");

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
            );

            logger.debug("Autenticazione passata");

            User user = userRepository.findByUsername(loginRequest.getUsername()).get();

            String jwt = jwtUtils.generateJwtToken(user);

            logger.debug("Token generato");

            List<String> roles = new ArrayList<>();
            roles.add(user.getRole().name());

            logger.debug("Login effettuato");

            return ResponseEntity.ok(new JwtResponse(jwt,
                user.getUsername(), user.getNome(),
                user.getCognome(), user.getIdAzienda(),
                roles));
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        logger.info("Registrazione");

        try {

            if (userRepository.existsByUsername(signUpRequest.getUsername())) {

                logger.debug("Username selezionato già presente");

                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

            // Create new user's account
            User user = new User(signUpRequest.getUsername(), signUpRequest.getNome(),
                                signUpRequest.getCognome(), encoder.encode(signUpRequest.getPassword()),
                                (byte)1, expirationDate);

            user.setEmail(signUpRequest.getEmail());

            logger.debug("User generato");

            user.setRole(Objects.requireNonNullElse(Role.valueOf(signUpRequest.getRole()), Role.USER));
            userRepository.save(user);

            logger.debug("Utenza creata");

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

            if (userRepository.existsByUsername(changeRequest.getUsername())) {

                User user = userRepository.findByUsername(changeRequest.getUsername()).get();

                if (encoder.matches(changeRequest.getOldPassword(), user.getPassword())) {

                    LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

                    user.setExpirationDate(expirationDate);
                    user.setPassword(encoder.encode(changeRequest.getNewPassword()));

                }

                userRepository.save(user);

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

            if (userRepository.existsByEmail(lostRequest.getEmail())) {

                User user = userRepository.findByEmail(lostRequest.getEmail()).get();

                LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

                String newPassword = UtilLib.generatePassayPassword();

                user.setExpirationDate(expirationDate);
                user.setPassword(encoder.encode(newPassword));

                Email email = UtilLib.getEmail(lostRequest.getEmail(), Constants.NOTE_LOST_PASSWORD + newPassword, Constants.OGGETTO_LOST_PASSWORD);

                serviceEmail.sendHtmlMessage(email);

                userRepository.save(user);

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

            if (userRepository.existsByUsername(changeRequest.getUsername())) {

                User user = userRepository.findByUsername(changeRequest.getUsername()).get();

                if (encoder.matches(changeRequest.getPassword(), user.getPassword())) {

                    userRepository.delete(user);

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
}