package com.plot.plotserver.dto.request.email;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailCodeReqDto {

    private String email;

    private String code;
}
