package com.spring.userservice.controller;

import com.spring.userservice.dto.AuthenticationRequest;
import com.spring.userservice.dto.AuthenticationResponse;
import com.spring.userservice.dto.RegisterRequest;
import com.spring.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller that provides endpoints for user registration and authentication.
 * Handles user-related authentication requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint to register a new user.
     *
     * @param registerRequest the request object containing user registration data
     * @return a response containing the authentication token for the newly registered user
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);  
    }

    /**
     * Endpoint to authenticate an existing user.
     *
     * @param request the request object containing user authentication data (email and password)
     * @return a response containing the authentication token if authentication is successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}

