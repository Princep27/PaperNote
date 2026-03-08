package com.note.controller;

import com.note.dto.NoteDto;
import com.note.dto.TagDto;
import com.note.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public ResponseEntity<List<TagDto.Response>> getUserTags() {
        return ResponseEntity.ok(tagService.getUserTags());
    }

    @GetMapping("/api/tags/{tagId}/notes")
    public ResponseEntity<Page<NoteDto.Response>> getNotesByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(tagService.getNotesByTag(tagId, page, size));
    }

    @PostMapping("/api/notes/{id}/tags")
    public ResponseEntity<NoteDto.Response> addTagToNote(
            @PathVariable Long id,
            @Valid @RequestBody TagDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.addTagToNote(id, request));
    }

    @DeleteMapping("/api/notes/{id}/tags/{tagId}")
    public ResponseEntity<NoteDto.Response> removeTagFromNote(
            @PathVariable Long id,
            @PathVariable Long tagId) {
        return ResponseEntity.ok(tagService.removeTagFromNote(id, tagId));
    }
}
