package com.lims.oauth2.handler;

import org.springframework.security.core.Authentication;

public abstract class AuthenticationHandlerChain {
    private AuthenticationHandlerChain next = null;


    public  AuthenticationHandlerChain setNextAuthenticationHandler(AuthenticationHandlerChain next) {
        this.next = next;
        return (next);
    }
    public  abstract void handle(String email);


    protected void handleNext(String email) {
        if (next != null)
            next.handle(email);
    }
}
