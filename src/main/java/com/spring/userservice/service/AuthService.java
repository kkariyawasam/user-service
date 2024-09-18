package com.spring.userservice.service;

import com.spring.userservice.config.JwtService;
import com.spring.userservice.dto.AuthenticationRequest;
import com.spring.userservice.dto.AuthenticationResponse;
import com.spring.userservice.dto.RegisterRequest;
import com.spring.userservice.entity.User;
import com.spring.userservice.exception.CustomAuthenticationException;
import com.spring.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication and registration operations.
 * 
 * This class is responsible for user registration and authentication.
 * It interacts with the UserRepository to persist user details, the 
 * AuthenticationManager to verify user credentials, and the JwtService 
 * to generate JWT tokens. It ensures that only authenticated users 
 * can access protected resources.
 */

@Service
@RequiredArgsConstructor
public class AuthService {
	
    private  final UserRepository userRepository;
    private  final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    
    /**
     * Registers a new user in the system.
     * 
     * This method takes a RegisterRequest, encodes the user's password, 
     * and saves the user to the repository. After saving, it generates 
     * a JWT token for the newly registered user.
     * 
     * @param registerRequest The user registration request containing user details.
     * @return An AuthenticationResponse with the JWT access token.
     */
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .role(registerRequest.getRole())
            .build();
        var savedUser = userRepository.save(user);
        return createAuthenticationResponse(savedUser);
    }
    
    /**
     * Authenticates a user based on email and password.
     * 
     * This method takes an AuthenticationRequest, validates the provided 
     * credentials using the AuthenticationManager, and generates a JWT token 
     * if the user is authenticated successfully. If authentication fails, it 
     * throws a BadCredentialsException.
     * 
     * @param request The authentication request containing email and password.
     * @return An AuthenticationResponse with the JWT access token.
     */
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

            return createAuthenticationResponse(user);
            
        } catch (AuthenticationException e) {
        	   throw new CustomAuthenticationException("Invalid username or password"); 
        }
    }
    
    /**
     * Helper method to create an AuthenticationResponse containing a JWT token.
     * 
     * This method generates a JWT token for a given user and returns 
     * an AuthenticationResponse containing the token.
     * 
     * @param user The user for whom the token is generated.
     * @return An AuthenticationResponse with the JWT access token.
     */
    private AuthenticationResponse createAuthenticationResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }


    


}

