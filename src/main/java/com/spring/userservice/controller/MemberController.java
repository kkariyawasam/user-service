package com.spring.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling member-related operations.
 * 
 * This class manages endpoints related to member functions. 
 * It defines endpoints for handling GET and POST requests within 
 * the '/management' path.
 */
@RestController
@RequestMapping("/management")
public class MemberController {

    /**
     * Handles GET requests for the member endpoint.
     * 
     * This endpoint provides access to member-related data or services.
     * It returns a simple message indicating successful access to the GET endpoint.
     * 
     * @return A string message indicating successful access to the GET endpoint.
     */
    @GetMapping
    public String getMember() {
        return "Secured Endpoint :: GET - Member controller";
    }

    /**
     * Handles POST requests for the member endpoint.
     * 
     * This endpoint is used for submitting data or performing actions 
     * related to members. It returns a simple message indicating successful 
     * access to the POST endpoint.
     * 
     * @return A string message indicating successful access to the POST endpoint.
     */
    @PostMapping
    public String post() {
        return "POST:: management controller";
    }
}
