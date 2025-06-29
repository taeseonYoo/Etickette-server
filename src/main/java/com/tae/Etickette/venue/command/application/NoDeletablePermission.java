package com.tae.Etickette.venue.command.application;

import org.springframework.security.access.AccessDeniedException;

public class NoDeletablePermission extends AccessDeniedException {
    public NoDeletablePermission(String msg) {
        super(msg);
    }

    public NoDeletablePermission(String msg, Throwable cause) {
        super(msg, cause);
    }
}
