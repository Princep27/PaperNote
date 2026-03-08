package com.note.service;

import com.note.dto.NoteDto;
import com.note.dto.TagDto;
import com.note.entity.Note;
import com.note.entity.Tag;
import com.note.entity.User;
import com.note.exception.BadRequestException;
import com.note.exception.ResourceNotFoundException;
import com.note.repository.NoteRepository;
import com.note.repository.TagRepository;
import com.note.util.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    @Transactional(readOnly = true)
    public List<TagDto.Response> getUserTags() {
        User user = noteService.getCurrentUserEntity();
        return tagRepository.findByUserId(user.getId())
                .stream().map(NoteMapper::toTagResponse).collect(Collectors.toList());
    }

    @Transactional
    public NoteDto.Response addTagToNote(Long noteId, TagDto.CreateRequest request) {
        User user = noteService.getCurrentUserEntity();
        Note note = noteService.getOwnedNoteEntity(noteId);

        // Find or create tag for user
        Tag tag = tagRepository.findByNameAndUserId(request.getName(), user.getId())
                .orElseGet(() -> {
                    Tag newTag = Tag.builder()
                            .name(request.getName())
                            .user(user)
                            .build();
                    return tagRepository.save(newTag);
                });

        note.getTags().add(tag);
        return NoteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional
    public NoteDto.Response removeTagFromNote(Long noteId, Long tagId) {
        Note note = noteService.getOwnedNoteEntity(noteId);
        User user = noteService.getCurrentUserEntity();

        Tag tag = tagRepository.findByIdAndUserId(tagId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId));

        note.getTags().remove(tag);
        return NoteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional(readOnly = true)
    public Page<NoteDto.Response> getNotesByTag(Long tagId, int page, int size) {
        User user = noteService.getCurrentUserEntity();
        tagRepository.findByIdAndUserId(tagId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        return noteRepository.findByTagIdAndUserId(tagId, user.getId(), pageable)
                .map(NoteMapper::toResponse);
    }
}
