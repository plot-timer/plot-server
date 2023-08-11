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
@Table(name = "tag_category")
public class TagCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_category_id")
    private Long id;

//    @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
//    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @Column(name = "tag_id", nullable = false, columnDefinition = "bigint")
//    private Long tagId;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;


//    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
//    private Long userId;
}
