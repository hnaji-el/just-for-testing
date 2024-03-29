package com.lims.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lims.user.dto.LocalUser;
import com.lims.shared.exception.ResourceNotFoundException;
import com.lims.shared.util.GeneralUtils;

/**
 * 
 * @author Chinna
 *
 */
@Service("localUserDetailService")
@RequiredArgsConstructor
public class LocalUserDetailService implements UserDetailsService {

	private final UserService userService;

	@Override
	@Transactional
	public LocalUser loadUserByUsername(final String email) throws UsernameNotFoundException {
		User user = userService.findUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User " + email + " was not found in the database");
		}
		return createLocalUser(user);
	}

	@Transactional
	public LocalUser loadUserById(Long id) {
		System.out.println("id === " + id);
		User user = userService.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		return createLocalUser(user);
	}

	/**
	 * @param user
	 * @return LocalUser
	 */
	private LocalUser createLocalUser(User user) {
		return new LocalUser(user.getEmail(), user.isEnabled(), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities(user.getRoles()), user);
	}
}
