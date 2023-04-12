package com.proxyseller.notemanager.controllers;

import com.proxyseller.notemanager.model.Note;
import com.proxyseller.notemanager.model.request.CreateNewNoteRequest;
import com.proxyseller.notemanager.services.definitions.NoteManaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Endpoints for managing notes
 */
@RestController
@RequestMapping(value = "/notes")
public class NoteController {

    @Autowired
    NoteManaging noteManaging;

    /**
     * Get notes with paging which sorted by date creating
     */
    @GetMapping
    public List<Note> getNotes(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return noteManaging.getNotes(
                PageRequest.of(page, size,
                        Sort.by(Sort.Direction.ASC, "created")));
    }

    /**
     * Get note by id
     */
    @GetMapping(value = "/{id}")
    public Note getNote(@PathVariable String id) {
        return noteManaging.getNote(id);
    }

    /**
     * Create new note with input lines
     * @param request contains inputs for create new note
     * @return new created note
     */
    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody CreateNewNoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteManaging.createNote(request, getUser()));
    }

    /**
     * Add like to some note from auth user
     * @param id of note
     */
    @PostMapping(value = "/like/{id}")
    public ResponseEntity<?> likeNote(@PathVariable String id) {
        noteManaging.likeNote(id, getUser().get());
        return noContent();
    }

    /**
     * Remove like from some note from auth user
     * @param id of note
     */
    @DeleteMapping(value = "/like/{id}")
    public ResponseEntity<?> unlikeNote(@PathVariable String id) {
        noteManaging.unlikeNote(id, getUser().get());
        return noContent();
    }

    Optional<String> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.of(authentication.getName());
        }
        return Optional.of("anonymous");
    }

    ResponseEntity<?> noContent(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
