package com.example.security;

public final class Roles {
    public static final String ADMIN = "ADMIN";
    public static final String USER= "USER";

    private Roles() {
    }

    public static boolean isAdmin(String role) {
        return ADMIN.equalsIgnoreCase(role);
    }
}
