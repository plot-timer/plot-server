package com.plot.plotserver.domain;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, columnDefinition = "varchar (36)")
    private String email;

    @Comment("일반 로그인시 사용자가 직접 입력, 소셜 로그인시 해당 계정의 프로필 이름으로 설정됨 (추후에 변경 가능)")
    @Column(name = "nickname", nullable = false, columnDefinition = "varchar (16)")
    private String nickname;

    @Column(name = "password", nullable = true, columnDefinition = "varchar (36)")
    private String password;

    @Column(name = "gender", nullable = true, columnDefinition = "bit (1)")
    private boolean gender;

    @Column(name = "profile_birth", nullable = true, columnDefinition = "varchar (16)")
    private String profileBirth;

    @Column(name = "profile_image_path", nullable = true, columnDefinition = "text")
    private String profileImagePath;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @Comment("소셜 로그인시 갱신됨 (네이버, 카카오, 구글 중 하나)")
    @Column(name = "social_login", nullable = true, columnDefinition = "varchar(36)")
    private String socialLogin;
}
