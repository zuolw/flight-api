package com.example.flightapi.service;

import com.example.flightapi.dto.LoginRequestDTO;
import com.example.flightapi.entity.User;
import com.example.flightapi.repository.UserRepository;
import com.example.flightapi.security.UserDetailsImpl;
import com.example.flightapi.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public String login(LoginRequestDTO loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );
        logger.info("Authentication successful for user: {}", authentication.getName());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("Security context set for user: {}", authentication.getName());

        // Get principal, which is actually an instance of UserDetailsImpl
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            // If principal is UserDetailsImpl type, safely cast and get User entity
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            User user = userDetails.getUser();
            logger.debug("Generating JWT for user: {}", user.getEmail());
            return jwtUtils.generateToken(user);
        } else {
            // Handle unexpected case if principal is not the expected type
            logger.error("Unexpected principal type during authentication: {}", principal.getClass().getName());
            throw new RuntimeException("Authentication principal type error");
        }
    }

    public User register(User user) {
        logger.info("Attempting to register user: {}", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed: Email {} already registered", user.getEmail());
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }
}
