package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Optional<Category> findById(Long id);

    @Query("SELECT c FROM Category c WHERE c.categoryGroup.id = :categoryGroupId AND c.name = :name")
    public Optional<Category> findByNameAndCategoryGroupId(String name, Long categoryGroupId);

    public Optional<Category> findByName(String categoryName);
}
