package com.plot.plotserver.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("logout success");

        response.setStatus(message.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        ResponseCookie cookies = ResponseCookie.from("plot_token", null)
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookies.toString());

        new ObjectMapper().writeValue(response.getOutputStream(), message);
    }
}