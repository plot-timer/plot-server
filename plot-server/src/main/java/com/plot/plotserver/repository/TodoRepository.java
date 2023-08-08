package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {

    public Optional<Todo> findById(Long id);

}



