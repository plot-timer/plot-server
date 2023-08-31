package com.plot.plotserver.repository;

import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.DailyTodo;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {

    public Optional<CategoryGroup> findById(Long id);


    @Comment("유저가 가지는 카테고리 그룹을 이름으로 조히")
    @Query("SELECT cg FROM CategoryGroup cg WHERE cg.user.id = :userId AND cg.name = :categoryGroupName")
    public Optional<CategoryGroup> findByUserIdAndName(@Param("userId") Long userId, @Param("categoryGroupName") String categoryGroupName);


    @Comment("userId로 카테고리 그룹만 조회, categoryAdd 할때 쓰임")
    @Query("SELECT cg FROM CategoryGroup cg WHERE cg.user.id = :userId")
    public List<CategoryGroup> findByUserId(@Param("userId") Long userId);

    @Comment("userId로 카테고리의 그룹, 그룹에 속한 카테고리들 조회, todoAdd 할때 쓰임 + 카테고리를 그룹별로 태그까지 같이 보여주는 페이지에서 사용")
    @Query("SELECT DISTINCT cg FROM CategoryGroup cg JOIN FETCH cg.categories c WHERE cg.user.id = :userId AND c.categoryGroup.id=cg.id")
    public List<CategoryGroup> findByUserIdWithCategories(@Param("userId") Long userId);


}
