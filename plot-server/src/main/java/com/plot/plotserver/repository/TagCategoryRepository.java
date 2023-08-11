package com.plot.plotserver.repository;

import com.plot.plotserver.domain.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagCategoryRepository extends JpaRepository<TagCategory,Long> {
}
