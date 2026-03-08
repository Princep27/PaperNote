package com.note.util;

import com.note.dto.NoteDto;
import com.note.dto.TagDto;
import com.note.entity.Note;
import com.note.entity.Tag;

import java.util.stream.Collectors;

public class NoteMapper {

    public static NoteDto.Response toResponse(Note note) {
        NoteDto.Response response = new NoteDto.Response();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setPinned(note.isPinned());
        response.setFavorite(note.isFavorite());
        response.setDeleted(note.isDeleted());
        response.setDeletedAt(note.getDeletedAt());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        response.setTags(note.getTags().stream()
                .map(NoteMapper::toTagResponse)
                .collect(Collectors.toSet()));

        if (note.getShareLink() != null && note.getShareLink().isActive()) {
            response.setSharePublicId(note.getShareLink().getPublicId());
        }

        return response;
    }

    public static TagDto.Response toTagResponse(Tag tag) {
        TagDto.Response response = new TagDto.Response();
        response.setId(tag.getId());
        response.setName(tag.getName());
        return response;
    }
}
