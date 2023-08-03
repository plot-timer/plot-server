package com.plot.plotserver.service;

import com.plot.plotserver.domain.RefreshToken;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.user.UserReqDto;
import com.plot.plotserver.dto.response.user.UserResponseDto;
import com.plot.plotserver.repository.RefreshTokenRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserResponseDto createOne(UserReqDto.CreateOne reqDto) throws Exception{

        UUID salt = UUID.randomUUID();
        String password = reqDto.getPassword() + salt.toString();
        String encodedPassword = encoder.encode(password);

        if(userRepository.findByUsername(reqDto.getUsername()).isPresent()){
            throw new Exception("해당 유저네임이 이미 존재합니다. 다른 이름을 입력해주세요.");
        }

        User user = User.builder()
                .username(reqDto.getUsername())
                .password(encodedPassword)
                .salt(salt)
                .roles("ROLE_USER")
                .profileName(reqDto.getProfileName())
                .profileBirth(reqDto.getProfileBirth())
                .profileImagePath(reqDto.getProfileImagePath())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return UserResponseDto.of(user);
    }

    public User findOne(Long id) throws Exception {
        if(id==null) {
            throw new IllegalArgumentException("parameter:[id] is null");
        }
        Optional<User> findUser = userRepository.findById(id);
        if(!findUser.isPresent()) {
            throw new Exception("parameter:[id] is wrong");
        }
        return findUser.get();
    }

    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userResDtos = new ArrayList<>();
        users.forEach(user->userResDtos.add(UserResponseDto.of(user)));

        return userResDtos;
    }
    
    public boolean deleteOne() throws Exception {
        String username = SecurityContextHolderUtil.getUsername();

        Optional<User> findUser = userRepository.findByUsername(username);
        User user = findUser.get();

        userRepository.delete(user);
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserId(user.getId());

        if(findRefreshToken.isPresent()){
            refreshTokenRepository.delete(findRefreshToken.get());
        }
        return true;
    }
}