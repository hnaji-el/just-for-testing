package com.lims.user.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.lims.shared.util.GeneralUtils;

/**
 * 
 * @author Chinna
 *
 */
public class LocalUser extends User implements OAuth2User, OidcUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2845160792248762779L;
	private final OidcIdToken idToken;
	private final OidcUserInfo userInfo;
	private Map<String, Object> attributes;
	private com.lims.user.User user;

	public LocalUser(final String userID, final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired,
			final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities, final com.lims.user.User user) {
		this(userID, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, user, null, null);
	}

	public LocalUser(final String userID, final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired,
					 final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities, final com.lims.user.User user, OidcIdToken idToken,
					 OidcUserInfo userInfo) {
		super(userID, "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.user = user;
		this.idToken = idToken;
		this.userInfo = userInfo;
	}

	public static LocalUser create(com.lims.user.User user, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
		LocalUser localUser = new LocalUser(user.getEmail(), user.isEnabled(), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities(user.getRoles()),
				user, idToken, userInfo);
		localUser.setAttributes(attributes);
		return localUser;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getName() {
		return this.user.getDisplayName();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public Map<String, Object> getClaims() {
		return this.attributes;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return this.userInfo;
	}

	@Override
	public OidcIdToken getIdToken() {
		return this.idToken;
	}

	public com.lims.user.User getUser() {
		return user;
	}


}
