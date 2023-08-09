package com.plot.plotserver.domain;

import com.plot.plotserver.util.ColorEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category_group")
public class CategoryGroup {

    @Id
    @Column(name = "category_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar (36)")
    private String name;

//    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
//    private Long user_id;

    @Column(name = "color", nullable = false)
    @Enumerated
    private ColorEnum color;

    @ManyToOne
    @JoinColumn(name = "users_id",nullable = false)
    private User user;


}