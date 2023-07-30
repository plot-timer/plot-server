package com.plot.plotserver.dto.response.user;

import com.plot.plotserver.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String profileName;
    private String profileImagePath;
    private String profileBirth;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profileName(user.getProfileName())
                .profileImagePath(user.getProfileImagePath())
                .profileBirth(user.getProfileBirth())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
