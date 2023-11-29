package com.lims.oauth2.user;

import java.util.Map;

import com.lims.auth.SocialProvider;
import com.lims.oauth2.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(SocialProvider.OUTLOOK.getProviderType())) {
			return new OutlookOauth2UserInfo(attributes);
		}
		throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
	}
}