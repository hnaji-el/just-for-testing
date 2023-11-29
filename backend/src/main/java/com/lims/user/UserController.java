package com.lims.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lims.user.annotation.CurrentUser;
import com.lims.user.dto.LocalUser;
import com.lims.shared.util.GeneralUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {

	private final UserService userService;



	@GetMapping("/users/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
		return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
	}


	@GetMapping("/users")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<User>> getUsers () {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/all")
	public ResponseEntity<?> getContent() {
		return ResponseEntity.ok("Public content goes here");
	}

//	@GetMapping("/users")
//	@PreAuthorize("hasRole('USER')")
//	public ResponseEntity<?> getUserContent() {
//		return ResponseEntity.ok("User content goes here");
//	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAdminContent() {
		return ResponseEntity.ok("Admin content goes here");
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<?> getModeratorContent() {
		return ResponseEntity.ok("Moderator content goes here");
	}
}