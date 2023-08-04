package com.plot.plotserver.service;

import com.plot.plotserver.domain.RefreshToken;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.email.EmailCodeReqDto;
import com.plot.plotserver.dto.response.login.LoginResponseDto;
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
public class EmailLoginService {

    private final String BEARER_TYPE = "Bearer";

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDto emailLogin(EmailCodeReqDto req) throws Exception {


        String encodedPassword = passwordEncoder.encode(req.getPassword());//사용자 비밀번호 암호화.

        Optional<User> findUser = userRepository.findByUsername(req.getEmail());
        User userEntity = null;

        if(!findUser.isPresent()){
        //잘못된 계정입니다.

        }else if(findUser.isPresent()&&findUser.{//비밀번호가 잘못 되었습니다.!!
            userEntity = findUser.get();
        }
        else{//로그인 성공

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
