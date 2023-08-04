package com.plot.plotserver.service;

import com.plot.plotserver.domain.RefreshToken;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.user.UserReqDto;
import com.plot.plotserver.dto.response.user.UserResponseDto;
import com.plot.plotserver.exception.user.PasswordFormatException;
import com.plot.plotserver.exception.user.SecurityContextUserNotFoundException;
import com.plot.plotserver.exception.user.UserNotFoundException;
import com.plot.plotserver.exception.user.UsernameExistException;
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

        if(userRepository.findByUsername(reqDto.getUsername()).isPresent())
            throw new UsernameExistException("이미 존재하는 이메일입니다.");

        String password = reqDto.getPassword();

        if(password.length() < 4 || password.length() > 16)
            throw new PasswordFormatException("비밀번호의 길이는 4~16자여야 합니다.");

        UUID salt = UUID.randomUUID();
        String encodedPassword = encoder.encode(password + salt.toString());

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
        Optional<User> findUser = userRepository.findById(id);
        if(!findUser.isPresent()) {
            throw new UserNotFoundException("해당 [id]를 갖는 유저를 찾을 수 없습니다.");
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
        if(!findUser.isPresent()){
            throw new SecurityContextUserNotFoundException("유저를 찾을 수 없습니다.");
        }
        User user = findUser.get();
        userRepository.delete(user);

        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserId(user.getId());

        if(findRefreshToken.isPresent()){
            refreshTokenRepository.delete(findRefreshToken.get());
        }
        return true;
    }
}