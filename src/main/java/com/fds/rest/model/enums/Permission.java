package com.fds.rest.model.enums;

public enum Permission {
    USER_READ("auth:read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
