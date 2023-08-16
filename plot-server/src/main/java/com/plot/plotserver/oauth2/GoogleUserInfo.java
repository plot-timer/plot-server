package com.plot.plotserver.oauth2;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {

        this.attributes = attributes;
    }
    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProfileName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProfileImagePath() {
        return (String) attributes.get("picture");
    }

    @Override
    public String getProfileBirth() {
        return null;
    }

    @Override
    public String getEmail() {
       return (String) attributes.get("email");
    }
}
