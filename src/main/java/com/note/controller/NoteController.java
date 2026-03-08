package com.note.controller;

import com.note.dto.NoteDto;
import com.note.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    // ─── Core CRUD ────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<NoteDto.Response> createNote(@Valid @RequestBody NoteDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(request));
    }

    @GetMapping
    public ResponseEntity<Page<NoteDto.Response>> getNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(noteService.getNotes(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto.Response> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteDto.Response> updateNote(@PathVariable Long id,
                                                        @Valid @RequestBody NoteDto.UpdateRequest request) {
        return ResponseEntity.ok(noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteNote(@PathVariable Long id) {
        noteService.softDeleteNote(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Trash ────────────────────────────────────────────────────────────────
    @GetMapping("/trash")
    public ResponseEntity<List<NoteDto.Response>> getTrash() {
        return ResponseEntity.ok(noteService.getTrash());
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<NoteDto.Response> restoreNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.restoreNote(id));
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentDelete(@PathVariable Long id) {
        noteService.permanentDelete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Pin & Favorite ───────────────────────────────────────────────────────
    @PatchMapping("/{id}/pin")
    public ResponseEntity<NoteDto.Response> togglePin(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.togglePin(id));
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<NoteDto.Response> toggleFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.toggleFavorite(id));
    }

    // ─── Search ───────────────────────────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<Page<NoteDto.Response>> searchNotes(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(noteService.searchNotes(q, page, size));
    }
}
