package com.plot.plotserver.repository;

import com.plot.plotserver.domain.CategoryGroup;
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


    @Comment("유저가 가지는 카테고리 그룹을 이름으로 조회")
    @Query("SELECT cg FROM CategoryGroup cg WHERE cg.user.id = :userId AND cg.name = :categoryGroupName")
    public Optional<CategoryGroup> findByUserIdAndName(@Param("userId") Long userId, @Param("categoryGroupName") String categoryGroupName);


    @Comment("userId로 카테고리 그룹만 조회, categoryAdd 할때 쓰임")
    @Query("SELECT cg FROM CategoryGroup cg WHERE cg.user.id = :userId")
    public List<CategoryGroup> findByUserId(@Param("userId") Long userId);

    @Comment("userId로 카테고리의 그룹, 그룹에 속한 카테고리들 조회, todoAdd 할때 쓰임, 카테고리를 그룹별로 태그까지 같이 보여주는 페이지에서 사용")
    @Query("SELECT DISTINCT cg FROM CategoryGroup cg " +
            "JOIN FETCH cg.categories c " +
            "WHERE cg.user.id = :userId ")
    public List<CategoryGroup> findByUserIdWithCategories(@Param("userId") Long userId);

//    @Comment("userId로 카테고리 그룹, 카테고리, 태그 정보 조회")
//    @Query("SELECT DISTINCT cg FROM CategoryGroup cg " +
//            "LEFT JOIN FETCH cg.categories c " +
//            "LEFT JOIN FETCH c.tagCategories tc " +
//            "WHERE cg.user.id = :userId ")
//    public List<CategoryGroup> findByUserIdWithCategoriesAndTags(@Param("userId") Long userId); 이렇게 못 쓴다.
//    둘 이상의 컬렉션을 패치 조인 할 수 없다.
// 컬렉션을 사용하면, 페이징 API를 사용할 수 없다.
// 페이징을 사용하려면 다대 일관계 즉, tag-->tagcategory->...categoryGroup 까지 가면 사용 할 수 있다.



}
