package com.lims.oauth2.handler;

import static com.lims.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.lims.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.lims.oauth2.handler.AuthenticationHandlerChain;
import com.lims.shared.config.jwt.JwtService;
import com.lims.user.UserRepository;
import com.lims.user.dto.LocalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.lims.shared.config.AppProperties;
import com.lims.shared.exception.BadRequestException;
import com.lims.shared.util.CookieUtils;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtService	jwtService;

	private final AppProperties appProperties;

	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	private final UserRepository userRepository;
	private  AuthenticationHandlerChain authenticationHandlerChain;

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String email = ((LocalUser) authentication.getPrincipal()).getUser().getEmail();

		String errorMessage = null;
		try {
			//validate if is user that try to login allowed
			authenticationHandlerChain.handle(email);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			// we should delete the user
			userRepository.deleteByEmail(email);
		}

		String targetUrl = determineTargetUrl(request, authentication, errorMessage);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return ;
		}
		clearAuthenticationAttributes(request, response);
		System.out.println("redirect_uri === " + targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, Authentication authentication, String errorMessage) {
		Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
		if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
			throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
		}

		String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
		if (errorMessage != null) {
			return UriComponentsBuilder.fromUriString(targetUrl).queryParam("error", errorMessage).build().toUriString();
		}
		String token = generateNewToken(authentication);
		return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
	}

	private String generateNewToken(Authentication authentication) {
		Map<String, Object> claims = new HashMap<>();
		LocalUser localUser = (LocalUser) authentication.getPrincipal();

		claims.put("id", localUser.getUser().getId());
		return (jwtService.generateToken(claims, localUser));
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}

	private boolean isAuthorizedRedirectUri(String uri) {
		URI clientRedirectUri = URI.create(uri);

		return appProperties.getOauth2().getAuthorizedRedirectUris().stream().anyMatch(authorizedRedirectUri -> {
			// Only validate host and port. Let the clients use different paths if they want
			// to
			URI authorizedURI = URI.create(authorizedRedirectUri);
			if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort()) {
				return true;
			}
			return false;
		});
	}


	@Autowired
	public void setupAuthenticationHandlers(
			AllowedEmailHandler allowedEmailHandler,
			AllowedDomainsHandler allowedDomainsHandler) {
		this.authenticationHandlerChain = allowedEmailHandler;
		this.authenticationHandlerChain.setNextAuthenticationHandler(allowedDomainsHandler);
	}


}