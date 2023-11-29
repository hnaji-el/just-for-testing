package com.lims.user;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lims.user.dto.LocalUser;
import com.lims.oauth2.OAuth2AuthenticationProcessingException;
import com.lims.role.Role;
import com.lims.role.RoleRepository;
import com.lims.oauth2.user.OAuth2UserInfo;
import com.lims.oauth2.user.OAuth2UserInfoFactory;

/**
 * @author Chinna
 * @since 26/3/18
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;



	@Transactional(value = "transactionManager")
	public User addNewUser(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
		User user = buildUser(oAuth2UserInfo, registrationId);
		Date now = Calendar.getInstance().getTime();
		user.setCreatedDate(now);
		user.setModifiedDate(now);
		user = userRepository.save(user);
		userRepository.flush();
		return user;

	}

	private User buildUser(final OAuth2UserInfo oAuth2UserInfo,  String registrationId) {
		User user = new User();
		user.setDisplayName(oAuth2UserInfo.getName());
		user.setEmail(oAuth2UserInfo.getEmail());
		final HashSet<Role> roles = new HashSet<Role>();
		roles.add(roleRepository.findByName(Role.ROLE_USER));
		user.setRoles(roles);
		user.setProvider(registrationId);
		user.setEnabled(true);
		user.setProviderUserId(oAuth2UserInfo.getId());
		return user;
	}

	@Override
	public User findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
		if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
			throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
		} else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		User user = findUserByEmail(oAuth2UserInfo.getEmail());
		if (user != null) {
			if (!user.getProvider().equals(registrationId)) {
				throw new OAuth2AuthenticationProcessingException(
						"Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
			}
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = addNewUser(oAuth2UserInfo, registrationId);
		}
		return LocalUser.create(user, attributes, idToken, userInfo);
	}

	private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
		existingUser.setDisplayName(oAuth2UserInfo.getName());
		return userRepository.save(existingUser);
	}



	@Override
	public Optional<User> findUserById(Long id) {

		return userRepository.findById(id);
	}


	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}
