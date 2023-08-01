package com.plot.plotserver.service;

import com.plot.plotserver.domain.RefreshToken;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.login.SocialLoginReqDto;
import com.plot.plotserver.dto.response.login.LoginResponseDto;
import com.plot.plotserver.dto.response.login.OauthTokenResponseDto;
import com.plot.plotserver.oauth2.OAuth2UserInfo;
import com.plot.plotserver.oauth2.OauthTokenProvider;
import com.plot.plotserver.oauth2.OauthUserInfoProvider;
import com.plot.plotserver.repository.RefreshTokenRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {

    private final String BEARER_TYPE = "Bearer";
    private final ClientRegistrationRepository clientRegistrationRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDto socialLogin(SocialLoginReqDto req) throws Exception {

        ClientRegistration provider = clientRegistrationRepository.findByRegistrationId(req.getProvider());

        OauthTokenResponseDto tokenResponse = OauthTokenProvider.getTokenFromAuthServer(req, provider);
        String providerName = req.getProvider();
        Map<String, Object> userAttributes = OauthUserInfoProvider.getUserInfoFromAuthServer(providerName, provider, tokenResponse);

        OAuth2UserInfo oauth2UserInfo = OauthUserInfoProvider.makeOAuth2UserInfo(providerName, userAttributes);
        providerName = oauth2UserInfo.getProvider();

        String username = providerName +"_"+oauth2UserInfo.getProviderId();
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        Optional<User> findUser = userRepository.findByUsername(username);
        User userEntity = null;

        if(!findUser.isPresent()){
            userEntity = User.builder()
                    .username(username)
                    .password(encodedPassword)
                    .roles("ROLE_USER")
                    .provider(providerName)
                    .profileImagePath(oauth2UserInfo.getProfileImagePath())
                    .profileName(oauth2UserInfo.getProfileName())
                    .profileBirth(oauth2UserInfo.getProfileBirth())
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(userEntity);
        }else{
            userEntity = findUser.get();
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