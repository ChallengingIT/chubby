package it.challenging.torchy.controller;

import it.challenging.torchy.repository.AuthorityRepository;
import it.challenging.torchy.repository.UserRepository;
import it.challenging.torchy.request.LoginRequest;
import it.challenging.torchy.request.SignupRequest;
import it.challenging.torchy.response.JwtResponse;
import it.challenging.torchy.response.MessageResponse;
import it.challenging.torchy.security.services.UserDetailsImpl;
import it.challenging.torchy.entity.Authority;
import it.challenging.torchy.entity.User;
import it.challenging.torchy.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @CrossOrigin(origins = "*")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Login");

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        logger.debug("Autenticazione passata");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        logger.debug("Token generato");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = new ArrayList<>();

        roles.add(authorityRepository.findByUsername(loginRequest.getUsername()).getAuthority());

        logger.debug("Login effettuato");

        return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getUsername(),
            roles));
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

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
            encoder.encode(signUpRequest.getPassword()), (byte)1);

        logger.debug("User generato");

        String strRole = signUpRequest.getRole();

        Authority authority = new Authority();

        authority.setAuthority(Objects.requireNonNullElse(strRole, "ROLE_USER"));

        user.setAuthority(authority);
        userRepository.save(user);

        logger.debug("Utenza creata");

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}