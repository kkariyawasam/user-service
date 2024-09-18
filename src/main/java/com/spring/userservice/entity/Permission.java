package com.spring.userservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing various permissions in the application.
 * 
 * This enum defines a set of specific permissions that are used to control 
 * access to different features or actions within the application. Each permission 
 * is associated with a unique string identifier, such as 'admin:read' or 'management:create'. 
 * 
 * The permissions defined here are typically used in conjunction with roles to 
 * manage authorization and access control, allowing specific actions for users 
 * who possess the corresponding authority.
 * 
 * Lombok's @Getter generates a getter for the permission string, and @RequiredArgsConstructor 
 * automatically generates a constructor to assign the permission value.
 */

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    MEMBER_READ("management:read"),
    MEMBER_CREATE("management:create"),

    ;

    @Getter
    private final String permission;
}
