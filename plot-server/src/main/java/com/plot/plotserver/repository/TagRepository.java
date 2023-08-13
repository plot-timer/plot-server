package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long > {

    Optional<Tag> findById(Long id);

    @Query("SELECT t FROM Tag t WHERE t.userId = :userId and t.category.id = :categoryId")
    List<Tag> findByUserAndCategory(Long userId, Long categoryId);

    @Query("SELECT t FROM Tag t WHERE t.userId = :userId and t.tagName = :tagName")
    List<Tag> findByUserAndTagName(Long userId, String tagName);

    @Query("SELECT t FROM Tag t WHERE t.userId = :userId")
    List<Tag> findByUserId(Long userId);
}
