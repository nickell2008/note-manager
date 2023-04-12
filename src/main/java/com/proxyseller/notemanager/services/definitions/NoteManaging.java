package com.proxyseller.notemanager.services.definitions;

import com.proxyseller.notemanager.model.Note;
import com.proxyseller.notemanager.model.request.CreateNewNoteRequest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing of notes
 */
public interface NoteManaging {

    /**
     * Return existing note by id of throw exception
     */
    Note getNote(String id);

    /**
     * Get list of notes by paging
     */
    List<Note> getNotes(PageRequest paging);

    /**
     * Create new note for current user(or anonym)
     */
    Note createNote(CreateNewNoteRequest request, Optional<String> user);

    /**
     * Add like to some note from auth user
     * Create user entity in db if it is not exist
     * @param id of note
     */
    void likeNote(String id, String user);

    /**
     * Remove like from some note from auth user
     * @param id of note
     */
    void unlikeNote(String id, String user);
}
