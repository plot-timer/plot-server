package com.plot.plotserver.repository;

import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {

    public Optional<CategoryGroup> findById(Long id);

    @Query("SELECT cg FROM CategoryGroup cg WHERE cg.user.id = :userId AND cg.name = :categoryGroupName")
    public Optional<CategoryGroup> findByUserIdAndName(@Param("userId") Long userId, @Param("categoryGroupName") String categoryGroupName);
}
