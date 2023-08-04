package com.plot.plotserver.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "email_tmp")
public class EmailTmp {//3분동안 보관하다 삭제됨.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false, columnDefinition = "varchar(255)")
    private String userEmail;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "email_code", nullable = false, columnDefinition = "varchar(36)")
    private String code;

    @PrePersist
    public void prePersist() {
        this.createdAt=LocalDateTime.now();
    }

}
