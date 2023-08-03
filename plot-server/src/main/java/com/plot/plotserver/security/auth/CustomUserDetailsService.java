package com.plot.plotserver.security.auth;

import com.plot.plotserver.domain.User;
import com.plot.plotserver.exception.user.WrongLoginException;
import com.plot.plotserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, WrongLoginException {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if(userOpt.isPresent()) {
            return new CustomUserDetails(userOpt.get());
        }else {
            throw new WrongLoginException("로그인 정보가 올바르지 않습니다.");
        }
    }

}