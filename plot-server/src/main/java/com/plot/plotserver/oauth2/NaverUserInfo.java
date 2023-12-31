package com.plot.plotserver.oauth2;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProfileName() {
        return (String) attributes.get("nickname");
    }

    @Override
    public String getProfileImagePath() {
        return (String) attributes.get("profile_image");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getProfileBirth(){
        String birthYear = (String) attributes.get("birthyear");
        String birthDay = (String) attributes.get("birthday");
        return birthYear+"-"+birthDay;
    }
}
