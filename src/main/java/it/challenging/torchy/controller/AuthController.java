package it.challenging.torchy.controller;

import it.challenging.torchy.entity.Authority;
import it.challenging.torchy.entity.User;
import it.challenging.torchy.repository.AuthorityRepository;
import it.challenging.torchy.repository.UserRepository;
import it.challenging.torchy.request.ChangePasswordRequest;
import it.challenging.torchy.request.LoginRequest;
import it.challenging.torchy.request.SignupRequest;
import it.challenging.torchy.response.JwtResponse;
import it.challenging.torchy.response.MessageResponse;
import it.challenging.torchy.security.jwt.JwtUtils;
import it.challenging.torchy.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @CrossOrigin(origins = "*")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Login");

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            logger.debug("Autenticazione passata");

            User user = userRepository.findByUsername(loginRequest.getUsername()).get();

            if (user.getExpirationDate().isBefore(LocalDateTime.now())) {
                logger.debug("Password scaduta");

                return ResponseEntity.badRequest().body(new MessageResponse("Error: password expired!"));
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            logger.debug("Token generato");

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = new ArrayList<>();


            roles.add(authorityRepository.findByUsername(loginRequest.getUsername()).getAuthority());

            logger.debug("Login effettuato");

            return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(), user.getNome(),
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

            logger.debug("User generato");

            String strRole = signUpRequest.getRole();

            Authority authority = new Authority();

            authority.setAuthority(Objects.requireNonNullElse(strRole, "ROLE_USER"));
            authority.setUsername(user.getUsername());

            user.setAuthority(authority);
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

                if (user.getPassword().equals(encoder.encode(changeRequest.getOldPassword()))) {

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
}