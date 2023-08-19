package com.plot.plotserver.repository;

import com.plot.plotserver.domain.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {

    public Optional<CategoryGroup> findById(Long id);

    @Query("SELECT cg FROM CategoryGroup cg WHERE cg.user.id = :userId AND cg.name = :categoryGroupName")
    public Optional<CategoryGroup> findByUserIdAndName(@Param("userId") Long userId, @Param("categoryGroupName") String categoryGroupName);

}
