package com.spring.userservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthFilter is a custom filter that intercepts requests to validate JWT tokens and
 * set up authentication within the SecurityContext. It extends the OncePerRequestFilter,
 * ensuring the filter is executed only once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Constants for headers and token prefixes
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Filters incoming requests to validate the JWT and set the authentication details
     * in the SecurityContext if the token is valid.
     *
     * @param request the HTTP request object
     * @param response the HTTP response object
     * @param filterChain the filter chain to pass the request along
     * @throws ServletException in case of servlet-related issues
     * @throws IOException in case of I/O errors
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Retrieve Authorization header
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        final String email;

        // Proceed if header is invalid or does not start with 'Bearer '
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token by removing 'Bearer ' prefix
        jwt = authHeader.substring(BEARER_PREFIX.length());
        email = jwtService.extractUsername(jwt); // Extract email from token

        // If the user email exists and authentication is not already set
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            // Validate the token and set authentication
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Determines whether the filter should be skipped for certain paths.
     *
     * @param request the HTTP request object
     * @return true if the request path matches whitelisted paths, false otherwise
     * @throws ServletException in case of servlet-related issues
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
   
        return request.getServletPath().contains("/auth/");
    }
}

