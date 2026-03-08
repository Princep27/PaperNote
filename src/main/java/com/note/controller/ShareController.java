package com.note.controller;

import com.note.dto.NoteDto;
import com.note.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    @PostMapping("/api/notes/{id}/share")
    public ResponseEntity<NoteDto.Response> createShareLink(@PathVariable Long id) {
        return ResponseEntity.ok(shareService.createShareLink(id));
    }

    @GetMapping("/api/share/{publicId}")
    public ResponseEntity<NoteDto.Response> getSharedNote(@PathVariable String publicId) {
        return ResponseEntity.ok(shareService.getSharedNote(publicId));
    }

    @DeleteMapping("/api/notes/{id}/share")
    public ResponseEntity<Void> deleteShareLink(@PathVariable Long id) {
        shareService.deleteShareLink(id);
        return ResponseEntity.noContent().build();
    }
}
