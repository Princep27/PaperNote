package com.note.repository;

import com.note.entity.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {
    Optional<ShareLink> findByPublicIdAndActiveTrue(String publicId);
    Optional<ShareLink> findByNoteId(Long noteId);
}
