package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Optional<Category> findById(Long id);

    public Optional<Category> findByNameAndCategoryGroupId(String name, Long categoryGroupId);

}
