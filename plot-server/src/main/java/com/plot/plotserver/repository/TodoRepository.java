package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {

    public Optional<Todo> findById(Long id);

    @Query("SELECT t FROM Todo t WHERE t.category.id = :categoryId")
    public List<Todo> findByCategory(Long categoryId);

}



