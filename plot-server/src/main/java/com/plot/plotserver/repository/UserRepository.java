package com.plot.plotserver.repository;

import com.plot.plotserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findById(Long id);

    public Optional<User> findByUsername(String username);

}
