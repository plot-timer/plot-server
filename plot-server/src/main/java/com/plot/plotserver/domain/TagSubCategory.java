package com.plot.plotserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tag_sub_category")
public class TagSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sub_category_id", nullable = false, columnDefinition = "bigint")
    private Long subCategoryId;

    @Column(name = "tag_id", nullable = false, columnDefinition = "bigint")
    private Long tagId;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;
}
