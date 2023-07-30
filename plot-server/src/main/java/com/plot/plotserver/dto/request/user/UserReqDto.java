package com.plot.plotserver.dto.request.user;

import com.plot.plotserver.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDto {

    private String username;
    private String password;
    private String roles;
    private String profileName;
    private String profileBirth;
    private String profileImagePath;
    private boolean alarmPermission;
    private boolean friendAcceptance;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOne{
        private String username;
        private String password;
        private String profileName;
        private String profileBirth;
        private String profileImagePath;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateProfile{
        private String profileName;
        private String profileBirth;
        private boolean friendAcceptance;
        private boolean alarmPermission;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteUser{
        private String username;
        private String password;
    }

    public static UserReqDto toDto(User user) {

        return UserReqDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .profileName(user.getProfileName())
                .profileBirth(user.getProfileBirth())
                .profileImagePath(user.getProfileImagePath())
                .build();
    }
}
