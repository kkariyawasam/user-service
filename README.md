# **JWT Authentication System**

This project implements JWT-based authentication in a Spring Boot application. It allows users to securely access protected resources by validating JWT tokens and managing user roles and authorities. Below is an overview of the authentication flow:

## **Features**

* **JWT Authentication**: Uses JSON Web Tokens to authenticate users.  
* **Role-based Access Control**: Only authorised users with valid roles can access certain endpoints.  
* **Custom Authentication Filter**: Validates JWT tokens and ensures secure access to the system.  
* **Whitelisted URLs**: Some URLs can be accessed without authentication (e.g., login, registration).  
* **Security Context Holder**: Manages authentication state throughout the session.  
* **UserDetailsService**: Fetches user details from the database.

## **Flow Overview**

1. **Client Request**: A client makes a request to a URL.  
2. **Whitelist Check**: If the URL is whitelisted, the request is processed without authentication.  
3. **JWTAuthFilter**: Checks if a JWT exists in the request header.  
   * If no JWT is present, an error is returned.  
4. **UserDetailsService**: If a JWT is present, the system fetches user details from the database.  
   * If the user is valid, the JWT is validated.  
   * If validation succeeds, the user's authentication is set in the **SecurityContextHolder**.  
5. **Controller**: The request proceeds to the controller, which handles the business logic.  
6. **Error Handling**: Invalid JWT or non-existent users result in an error response.

## **Project Structure**

* `Controller`: Handles incoming requests and returns responses.  
* `JWTAuthFilter`: Intercepts requests to validate JWT tokens.  
* `UserDetailsService`: Loads user-specific data from the database.  
* `JwtAuthService`: Service to generate and validate JWT tokens.  
* `SecurityContextHolder`: Stores the authentication details after successful validation.

## **How to Run the Project**

1. Clone the repository.  
2. Configure the database connection in `application.properties`.  
3. Run the application using Maven.  
4. Use Postman or another tool to test the authentication endpoints.

## **Dependencies**

* Spring Boot  
* Spring Security  
* JWT  
* Lombok  
* MySQL Databas

