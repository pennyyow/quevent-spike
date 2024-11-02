package com.quevent.backend.service;

import com.quevent.backend.exception.UsernameExistsException;
import com.quevent.backend.model.*;
import com.quevent.backend.repository.UserRepository;
import com.quevent.backend.util.JwtUtil;
import io.jsonwebtoken.security.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    // This will be field injection if annotated each by Autowired and no constructor injection (less recommended)
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // This is a constructor injection
    @Autowired
    public AuthenticationService (
            UserRepository userRepository,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getUsername().isBlank() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("Username and Password are required!");
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch(BadCredentialsException ex) {
            throw new BadCredentialsException("Either username or password is incorrect for: " + loginRequest.getUsername());
        }

        // For generating tokens
        String accessToken = jwtUtil.generateAccessToken(authentication.getName());
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

        // For getting the userId
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("User not found: " + authentication.getName()));

        return new LoginResponse(user.getId(), accessToken, refreshToken);
    }

    public RegistrationResponse register(RegistrationRequest registrationRequest) {
        if(userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new UsernameExistsException(registrationRequest.getUsername() + " username already exist!");
        }

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userRepository.save(user);
        return new RegistrationResponse("User Registration Successful");
    }
}
