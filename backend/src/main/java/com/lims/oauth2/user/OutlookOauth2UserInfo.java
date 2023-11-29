package com.lims.oauth2.user;

import org.springframework.util.StringUtils;

import java.util.Map;

public class OutlookOauth2UserInfo extends  OAuth2UserInfo {
    public OutlookOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {

        String name = null;
        name = ((String) attributes.get("displayName")).strip();

        if (StringUtils.isEmpty(name)) {
            name = this.getEmail();
        }
        return name;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("mail");
    }

    @Override
    public String getImageUrl() {
        return "";
    }
}
