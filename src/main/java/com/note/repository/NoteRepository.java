package com.note.repository;

import com.note.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Note> findByUserIdAndDeletedTrue(Long userId);

    Optional<Note> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND n.deleted = false " +
           "AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Note> searchByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.deleted = true AND n.deletedAt < :cutoff")
    List<Note> findNotesDeletedBefore(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT n FROM Note n JOIN n.tags t WHERE t.id = :tagId AND n.user.id = :userId AND n.deleted = false")
    Page<Note> findByTagIdAndUserId(@Param("tagId") Long tagId, @Param("userId") Long userId, Pageable pageable);
}
