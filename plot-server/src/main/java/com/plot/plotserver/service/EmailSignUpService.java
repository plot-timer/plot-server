package com.plot.plotserver.service;

import com.plot.plotserver.domain.RefreshToken;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.email.EmailSignUpReqDto;
import com.plot.plotserver.dto.response.login.LoginResponseDto;
import com.plot.plotserver.repository.EmailTmpRepository;
import com.plot.plotserver.repository.RefreshTokenRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSignUpService {

    private final String BEARER_TYPE = "Bearer";

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final EmailTmpRepository emailTmpRepository;

    private final BCryptPasswordEncoder passwordEncoder;



    public LoginResponseDto emailSignUp(EmailSignUpReqDto req) throws Exception {//회원가입.


        Optional<User> findUser = userRepository.findByUsername(req.getEmail());
        User userEntity = null;

        if(!findUser.isPresent()){//회원가입 진행.
            //일반 회원가입 로직 동작시키지.

        }else{// 이미 존재하는 계정입니다.!!
            //예외의 경우이다.
        }

        UUID refreshTokenId = UUID.randomUUID();

        String accessToken = JwtUtil.createAccessToken(userEntity, refreshTokenId);
        String refreshToken = JwtUtil.createRefreshToken(userEntity);

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByUserId(userEntity.getId());
        if(oldRefreshToken.isPresent()){ // 해당 사용자의 리프레쉬 토큰이 이미 있다면 삭제한다.
            refreshTokenRepository.delete(oldRefreshToken.get());
        }
        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(refreshTokenId)
                .refreshToken(refreshToken)
                .userId(userEntity.getId())
                .createdAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(newRefreshToken);

        return LoginResponseDto.builder()
                .token_type(BEARER_TYPE)
                .access_token(accessToken)
                .build();
    }
}
