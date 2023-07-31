package com.plot.plotserver.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sub_category")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar (36)")
    private String name;

    @Column(name = "star", nullable = false, columnDefinition = "bit (1)")
    private boolean star;

    @Column(name = "color", nullable = true, columnDefinition = "int (6)")
    private int color;

    @Column(name = "icon_image_path", nullable = true, columnDefinition = "text")
    private String iconImagePath;

    @Column(name = "main_category_id", nullable = false, columnDefinition = "bigint")
    private Long mainCategoryId;
}