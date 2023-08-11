package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long > {
}
