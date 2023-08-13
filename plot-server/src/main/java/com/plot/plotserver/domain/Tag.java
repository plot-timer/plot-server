package com.plot.plotserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Comment("태그 이름")
    @Column(name = "tag_name", nullable = false, columnDefinition = "varchar(36)")
    private String tagName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Long userId;
}