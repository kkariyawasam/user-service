package com.spring.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.userservice.repository.UserRepository;

/**
 * ApplicationConfig class provides the configuration for user authentication
 * and authorization in the Spring Boot application.
 * <p>
 * This configuration includes password encoding, user details service, 
 * authentication provider, and authentication manager. It integrates with
 * the UserRepository to fetch user data for authentication and sets up
 * the necessary beans to support Spring Security functionality.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    private final UserRepository userRepository;

    /**
     * Bean for password encoding using BCrypt hashing algorithm.
     * <p>
     * This method sets up a PasswordEncoder that uses BCrypt for securely
     * hashing passwords. This is used during user registration and authentication.
     *
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean to provide a UserDetailsService implementation.
     * <p>
     * This method returns a lambda that fetches user details from the
     * database using the email address. If the user is not found, 
     * a UsernameNotFoundException is thrown.
     *
     * @return a UserDetailsService implementation.
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    /**
     * Bean to configure an AuthenticationProvider.
     * <p>
     * This method sets up a DaoAuthenticationProvider with a user details 
     * service and password encoder. This provider is used to authenticate
     * users based on the information stored in the database.
     *
     * @return an AuthenticationProvider instance.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean to configure the AuthenticationManager.
     * <p>
     * This method sets up the AuthenticationManager by retrieving the 
     * authentication manager from the provided AuthenticationConfiguration.
     * The AuthenticationManager handles the authentication process within
     * Spring Security.
     *
     * @param config the AuthenticationConfiguration provided by Spring Security.
     * @return an AuthenticationManager instance.
     * @throws Exception if any error occurs while retrieving the AuthenticationManager.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

