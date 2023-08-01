package com.plot.plotserver.oauth2;

public interface OAuth2UserInfo {

    String getProvider();
    String getProviderId();
    String getProfileName();
    String getProfileImagePath();

    String getProfileBirth();
}
