package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.Tag;
import com.plot.plotserver.domain.TagCategory;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagCategoryRepository extends JpaRepository<TagCategory, Long> {

    @Comment("tag_category 테이블을 통해 유저가 태그한 이름을 가지는 카테고리 검색")
    @Query("SELECT DISTINCT tc.category FROM TagCategory tc WHERE tc.tag.tagName = :tagName AND tc.category.categoryGroup.user.id = :userId")
    List<Category> findByUserIdAndTagName(@Param("userId") Long userId, @Param("tagName") String tagName);


    @Comment("해당 카테고리와 태그 이름을 가지는 태그_카테고리를 검색")
    @Query("SELECT DISTINCT tc FROM TagCategory tc WHERE tc.category.id = :categoryId AND tc.tag.tagName = :tagName")
    Optional<TagCategory> findByCategoryIdAndTagName(@Param("categoryId") Long categoryId, @Param("tagName") String tagName);


    @Query("SELECT DISTINCT tc FROM TagCategory tc WHERE tc.category.id = :categoryId")
    List<TagCategory> findByCategoryId(@Param("categoryId") Long categoryId);

    @Comment("유저가 등록한 태그를 모두 검색")
    @Query("SELECT DISTINCT tc.tag FROM TagCategory tc WHERE tc.category.categoryGroup.user.id = :userId")
    List<Tag> findByUserId(@Param("userId") Long userId);

}
