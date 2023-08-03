package com.plot.plotserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.login.SocialLoginReqDto;
import com.plot.plotserver.dto.response.login.LoginResponseDto;
import com.plot.plotserver.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/social-login")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping("")
    public void socialLogin(HttpServletResponse response, @RequestBody SocialLoginReqDto req) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        LoginResponseDto loginResponse = null;
        try {
            loginResponse = socialLoginService.socialLogin(req);
        } catch (Exception e) {
            Message message = Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
            om.writeValue(response.getOutputStream(), message);
            return;
        }
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("소셜 로그인 성공. 엑세스 토큰을 발급힙니다.")
                .build();

        String accessToken = loginResponse.getAccess_token();

        ResponseCookie cookie = ResponseCookie.from("plot_token", accessToken)
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(60 * 60 * 3) // 3시간
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        om.writeValue(response.getOutputStream(), message);
    }


}