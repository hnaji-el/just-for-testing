package com.lims.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lims.oauth2.user.OAuth2UserInfo;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import com.lims.user.dto.LocalUser;

/**
 * @author Chinna
 * @since 26/3/18
 */
public interface UserService {


	public  User addNewUser(final OAuth2UserInfo oAuth2UserInfom, String registrationId);
	User findUserByEmail(String email);

	Optional<User> findUserById(Long id);

	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

	List<User> getAllUsers();
}
