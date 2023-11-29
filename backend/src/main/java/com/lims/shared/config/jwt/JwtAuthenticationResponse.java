package com.lims.shared.config.jwt;

import com.lims.user.dto.UserInfo;
import lombok.Value;

@Value
public class JwtAuthenticationResponse {
	private String accessToken;
	private UserInfo user;
}