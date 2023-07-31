package com.plot.plotserver.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "main_category")
public class MainCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = true, columnDefinition = "varchar (36)")
    private String name;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long user_id;

}