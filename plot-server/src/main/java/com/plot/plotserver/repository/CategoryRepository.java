package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Category;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Optional<Category> findById(Long id);


    @Comment("카테고리와, 그 카테고리가 가지는 태그들까지 다 가져온다.")
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.tagCategories tc "+
            "LEFT JOIN FETCH tc.tag t " +
            "WHERE c.id = :id")
    public Optional<Category> findByIdJoinTags(Long id);

    @Query("SELECT c FROM Category c WHERE c.categoryGroup.id = :categoryGroupId AND c.name = :name")
    public Optional<Category> findByNameAndCategoryGroupId(String name, Long categoryGroupId);


    @Comment("Category안의 Todos들을 한꺼번에 가져오기.")
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.todos ts " +
            "WHERE c.id = :id")
    public Optional<Category> findByIdJoinTodos(Long id);

    public Optional<Category> findByName(String categoryName);


}
