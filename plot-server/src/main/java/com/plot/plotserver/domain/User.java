package com.plot.plotserver.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "email", nullable = false, length = 40, columnDefinition = "varchar")
    private String email;

    @Column(name = "nickname", nullable = false, length = 16, columnDefinition = "varchar")
    private String nickname;

    @Column(name = "password", nullable = true, length = 36, columnDefinition = "varchar")
    private String password;

    @Column(name = "gender", nullable = true, columnDefinition = "bit")
    private boolean gender;

    @Column(name = "profile_birth", nullable = true, length = 16, columnDefinition = "varchar")
    private String profileBirth;

    @Column(name = "profile_image_path", nullable = true, columnDefinition = "text")
    private String profileImagePath;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "datetime")
    private LocalDateTime updatedAt;

}
