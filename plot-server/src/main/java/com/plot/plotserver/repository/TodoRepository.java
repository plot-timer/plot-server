package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Todo;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {

    public Optional<Todo> findById(Long id);


    @Comment("Todo의 Category, CategoryGroup 까지 모두 join해서 가져오기")
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.category c " +
            "JOIN FETCH c.categoryGroup cg " +
            "WHERE t.id = :id")
    public Optional<Todo> findByIdJoinCategoryAndCategoryGroup(@Param("id") Long id);


    @Query("SELECT t FROM Todo t WHERE t.category.id = :categoryId")
    public List<Todo> findByCategory(Long categoryId);

    @Query("SELECT t FROM Todo t WHERE t.category.categoryGroup.user.id =:userId")
    public List<Todo> findByUserId(Long userId);


    @Comment("바로 위의 함수와 동일, fetch join 한 버전.")
    @Query("SELECT t FROM Todo t " +
            "JOIN FETCH t.category c " +
            "JOIN FETCH c.categoryGroup cg " +
            "WHERE t.category.categoryGroup.user.id =:userId")
    public List<Todo> findByUserIdJoinCategoryAndCategoryGroup(Long userId);

}



