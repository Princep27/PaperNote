package com.note.scheduler;

import com.note.entity.Note;
import com.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrashCleanupScheduler {

    private final NoteRepository noteRepository;

    //Runs every day at 2:00 AM. Permanently deletes notes
    // that have been in trash for more than 30 days

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupOldTrashedNotes() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        List<Note> oldNotes = noteRepository.findNotesDeletedBefore(cutoff);

        if (!oldNotes.isEmpty()) {
            log.info("Trash cleanup: permanently deleting {} notes deleted before {}", oldNotes.size(), cutoff);
            noteRepository.deleteAll(oldNotes);
        } else {
            log.info("Trash cleanup: no notes to delete");
        }
    }
}
