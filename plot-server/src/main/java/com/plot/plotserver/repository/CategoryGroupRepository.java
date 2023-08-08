package com.plot.plotserver.repository;

import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {

    public Optional<CategoryGroup> findById(Long id);
}
