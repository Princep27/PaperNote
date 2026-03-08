package com.note.repository;

import com.note.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByUserId(Long userId);
    Optional<Tag> findByIdAndUserId(Long id, Long userId);
    Optional<Tag> findByNameAndUserId(String name, Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
}
