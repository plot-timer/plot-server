package com.plot.plotserver.dto.response.login;

import com.plot.plotserver.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String token_type;
    private String access_token;
    private User user;
}
