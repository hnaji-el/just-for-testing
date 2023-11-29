package com.lims.auth;

import lombok.Getter;

/**
 * @author Chinna
 * @since 26/3/18
 */
@Getter
public enum SocialProvider {

	OUTLOOK("outlook"), LOCAL("local");

	private final String providerType;

	SocialProvider(final String providerType) {
		this.providerType = providerType;
	}

}
