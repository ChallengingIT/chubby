/*
 * Copyright (c) 2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.EmailSenderService;
import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.ChurchRepository;
import it.challenging.torchy.repository.PassRepository;
import it.challenging.torchy.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth/mapp")
public class Auth4MappController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ChurchRepository churchRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PassRepository passRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    JwtService jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @CrossOrigin(origins = "*")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Login");

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
            user.getUsername(),
            roles));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/code/verify/{username}")
    public ResponseEntity<?> consumeQRCodeImage(
            @PathVariable("username") String username
    ) throws Exception {

        Pass pass = passRepository.findByUsername(username) != null ?
                passRepository.findByUsername(username) : null;

        if (null != pass) {
            if (pass.getConsumed().equals((byte)1)) {
                return ResponseEntity.badRequest().body(new MessageResponse("QrCode gia' utilizzato"));
            } else {
                pass.setConsumed((byte) 1);

                passRepository.save(pass);
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("QrCode non abilitato"));
        }

        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        logger.info("Registrazione");

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {

            logger.debug("Username selezionato già presente");

            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Username is already taken!"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getNome(), signUpRequest.getCognome(),
            encoder.encode(signUpRequest.getPassword()), (byte)1, signUpRequest.getEmail());

        logger.debug("User generato");

        user.setRole(Role.USER);
        userRepository.save(user);

        for (int i = 1; i <= 7; i++) {
            Chiesa chiesa = new Chiesa();
            chiesa.setIdChiesa(i);
            chiesa.setUsername(user.getUsername());
            chiesa.setOttenuta((byte)0);

            churchRepository.save(chiesa);
        }

        logger.debug("Utenza creata");

        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/lost/password")
    public ResponseEntity<?> lostPassword(@Valid @RequestBody LostPasswordRequest lostRequest) {

        logger.info("Dimenticato la Password");

        try {

            if (userRepository.existsByEmail(lostRequest.getEmail())) {

                User user = userRepository.findByEmail(lostRequest.getEmail()).get();

                String newPassword = UtilLib.generatePassayPassword();

                user.setPassword(encoder.encode(newPassword));

                Email email = UtilLib.getEmail(lostRequest.getEmail(), Constants.NOTE_LOST_PASSWORD + newPassword, Constants.OGGETTO_LOST_PASSWORD);

                emailSenderService.sendHtmlMessage(email);

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
    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changeRequest) {

        logger.info("Cambio Password");

        try {

            if (userRepository.existsByUsername(changeRequest.getUsername())) {

                User user = userRepository.findByUsername(changeRequest.getUsername()).get();

                if (encoder.matches(changeRequest.getOldPassword(), user.getPassword())) {

                    user.setPassword(encoder.encode(changeRequest.getNewPassword()));

                } else {
                    logger.debug("Old Password non corrispondente all'attuale");

                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Wrong old password!"));
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
    @PostMapping("/change/role")
    public ResponseEntity<?> changeRole(@Valid @RequestBody ChangeRoleRequest changeRequest) {

        logger.info("Cambio Password");

        try {

            if (userRepository.existsByUsername(changeRequest.getUsername())) {

                User user = userRepository.findByUsername(changeRequest.getUsername()).get();

                Authority authority = new Authority();

                authority.setAuthority(changeRequest.getRole());
                authority.setUsername(user.getUsername());

                user.setRole(Role.valueOf(changeRequest.getRole()));

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