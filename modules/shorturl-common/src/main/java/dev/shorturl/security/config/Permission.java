package dev.shorturl.security.config;

import lombok.Getter;

@Getter
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    ;

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}