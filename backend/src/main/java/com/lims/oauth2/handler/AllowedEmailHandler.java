package com.lims.oauth2.handler;

import com.lims.user.dto.LocalUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AllowedEmailHandler extends AuthenticationHandlerChain {

    private static final String[] ALLOWED_EMAILS = {"khalidlaaroussi202@outlook.com"};
    @Override
    public void handle(String email) {

        System.out.println("emailHandler ==========> " + email);
        boolean isEmailAllowed = Arrays.asList(ALLOWED_EMAILS).contains(email);

        if (!isEmailAllowed)
            this.handleNext(email);
    }
}
