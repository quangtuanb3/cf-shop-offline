package com.cg.model.user;

public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_STAFF("STAFF");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
