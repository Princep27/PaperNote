package com.note.service;

import com.note.dto.NoteDto;
import com.note.entity.Note;
import com.note.entity.User;
import com.note.exception.ResourceNotFoundException;
import com.note.exception.UnauthorizedException;
import com.note.repository.NoteRepository;
import com.note.repository.UserRepository;
import com.note.util.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    // ─── Helpers ───────────────────────────────────────────────
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    private Note getOwnedNote(Long noteId) {
        User user = getCurrentUser();
        return noteRepository.findByIdAndUserId(noteId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + noteId));
    }

    // ─── CRUD ────────────────────────────────────────────────────────────────
    @Transactional
    public NoteDto.Response createNote(NoteDto.CreateRequest request) {
        User user = getCurrentUser();
        Note note = Note.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return NoteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional(readOnly = true)
    public Page<NoteDto.Response> getNotes(int page, int size) {
        User user = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("pinned").descending().and(Sort.by("updatedAt").descending()));
        return noteRepository.findByUserIdAndDeletedFalse(user.getId(), pageable)
                .map(NoteMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public NoteDto.Response getNoteById(Long id) {
        Note note = getOwnedNote(id);
        if (note.isDeleted()) {
            throw new ResourceNotFoundException("Note not found with id: " + id);
        }
        return NoteMapper.toResponse(note);
    }

    @Transactional
    public NoteDto.Response updateNote(Long id, NoteDto.UpdateRequest request) {
        Note note = getOwnedNote(id);
        if (note.isDeleted()) throw new ResourceNotFoundException("Note not found with id: " + id);

        if (StringUtils.hasText(request.getTitle())) note.setTitle(request.getTitle());
        if (request.getContent() != null) note.setContent(request.getContent());

        return NoteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional
    public void softDeleteNote(Long id) {
        Note note = getOwnedNote(id);
        note.setDeleted(true);
        note.setDeletedAt(LocalDateTime.now());
        noteRepository.save(note);
    }

    // ─── Trash ────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<NoteDto.Response> getTrash() {
        User user = getCurrentUser();
        return noteRepository.findByUserIdAndDeletedTrue(user.getId())
                .stream().map(NoteMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public NoteDto.Response restoreNote(Long id) {
        Note note = getOwnedNote(id);
        note.setDeleted(false);
        note.setDeletedAt(null);
        return NoteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional
    public void permanentDelete(Long id) {
        Note note = getOwnedNote(id);
        noteRepository.delete(note);
    }

    // ─── Pin & Favorite ───────────────────────────────────────────────────────
    @Transactional
    public NoteDto.Response togglePin(Long id) {
        Note note = getOwnedNote(id);
        note.setPinned(!note.isPinned());
        return NoteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional
    public NoteDto.Response toggleFavorite(Long id) {
        Note note = getOwnedNote(id);
        note.setFavorite(!note.isFavorite());
        return NoteMapper.toResponse(noteRepository.save(note));
    }

    // ─── Search ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public Page<NoteDto.Response> searchNotes(String keyword, int page, int size) {
        User user = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        return noteRepository.searchByKeyword(user.getId(), keyword, pageable)
                .map(NoteMapper::toResponse);
    }

    // ─── Get Note Entity (internal use) ───────────────────────────────────────
    public Note getOwnedNoteEntity(Long noteId) {
        return getOwnedNote(noteId);
    }

    public User getCurrentUserEntity() {
        return getCurrentUser();
    }
}
