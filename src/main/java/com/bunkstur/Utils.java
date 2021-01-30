package com.bunkstur;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;

public class Utils {
    public static DefaultJWTCallerPrincipal GetUser(Principal user) {
        return (DefaultJWTCallerPrincipal) user;
    }

    public static DefaultJWTCallerPrincipal GetUser(SecurityContext securityContext) {
        return GetUser(securityContext.getUserPrincipal());
    }
}
