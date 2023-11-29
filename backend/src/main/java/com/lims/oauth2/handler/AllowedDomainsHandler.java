package com.lims.oauth2.handler;

import com.lims.auth.ForbiddenException;
import com.lims.auth.UnauthorizedException;
import com.lims.user.dto.LocalUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class AllowedDomainsHandler extends AuthenticationHandlerChain {
    public final static String[] ALLOWED_DOMAINS = {"um6p.ma"};

    @Override
    public void handle(String email) {
        String emailDomain = getDomainFromEmail(email);

        boolean isDomainAllowed = Arrays.asList(ALLOWED_DOMAINS).contains(emailDomain);

        if (!isDomainAllowed)
            throw new UnauthorizedException("this Domain " + emailDomain + " is Not Allowed");
        this.handleNext(email);
    }


    private String getDomainFromEmail(String email) {

        // Define a regular expression pattern for extracting the domain
        String regex = "@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        // Create a matcher and find the domain
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            return matcher.group(1);
        }

        // If no match is found, return null or handle accordingly
        return null;
    }
}