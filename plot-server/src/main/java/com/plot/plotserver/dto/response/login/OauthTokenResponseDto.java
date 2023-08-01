package com.plot.plotserver.dto.response.login;

import lombok.Data;

@Data
public class OauthTokenResponseDto {

    String access_token;
    String token_type;
    String refresh_token;
    String expires_in;
    String scope;
    String refresh_token_expires_in;

}
