package com.plot.plotserver.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar (36)")
    private String name;

    @Column(name = "star", nullable = false, columnDefinition = "bit (1)")
    private boolean star;

    @Column(name = "emoji", nullable = true, columnDefinition = "text")
    private String emoji;


//    @Column(name = "category_group_id", nullable = false, columnDefinition = "bigint")
//    private Long CategoryGroupId;

    @ManyToOne
    @JoinColumn(name = "category_group_id",nullable = false)
    private CategoryGroup categoryGroup;
}