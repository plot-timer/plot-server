package com.plot.plotserver.dto.request.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginReqDto {

    private String provider;
    private String code;
}
