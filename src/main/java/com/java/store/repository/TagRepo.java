package com.java.store.repository;

import com.java.store.module.Tags;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tags, Long> {
    boolean existsByTitle(String tagTitle);
    Tags getByTitle(String tagTitle);
}
