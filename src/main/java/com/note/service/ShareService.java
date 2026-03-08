package com.note.service;

import com.note.dto.NoteDto;
import com.note.entity.Note;
import com.note.entity.ShareLink;
import com.note.exception.BadRequestException;
import com.note.exception.ResourceNotFoundException;
import com.note.repository.NoteRepository;
import com.note.repository.ShareLinkRepository;
import com.note.util.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final NoteRepository noteRepository;
    private final ShareLinkRepository shareLinkRepository;
    private final NoteService noteService;

    @Transactional
    public NoteDto.Response createShareLink(Long noteId) {
        Note note = noteService.getOwnedNoteEntity(noteId);

        if (note.getShareLink() != null && note.getShareLink().isActive()) {
            return NoteMapper.toResponse(note);
        }

        if (note.getShareLink() != null) {
            note.getShareLink().setActive(true);
            noteRepository.save(note);
            return NoteMapper.toResponse(note);
        }

        ShareLink shareLink = ShareLink.builder()
                .note(note)
                .publicId(UUID.randomUUID().toString().replace("-", ""))
                .active(true)
                .build();

        shareLinkRepository.save(shareLink);
        note.setShareLink(shareLink);
        return NoteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional(readOnly = true)
    public NoteDto.Response getSharedNote(String publicId) {
        ShareLink shareLink = shareLinkRepository.findByPublicIdAndActiveTrue(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Shared note not found"));
        return NoteMapper.toResponse(shareLink.getNote());
    }

    @Transactional
    public void deleteShareLink(Long noteId) {
        Note note = noteService.getOwnedNoteEntity(noteId);
        if (note.getShareLink() == null) {
            throw new BadRequestException("Note does not have an active share link");
        }
        note.getShareLink().setActive(false);
        noteRepository.save(note);
    }
}
