package com.proxyseller.notemanager.services.mappers;

import com.proxyseller.notemanager.database.entities.NoteEntity;
import com.proxyseller.notemanager.model.Note;
import com.proxyseller.notemanager.model.request.CreateNewNoteRequest;

public class NoteMapper{

    public static Note mapEntity(NoteEntity note, long likes){
        return new Note(note.id, note.lines, likes);
    }

    public static NoteEntity createEntity(CreateNewNoteRequest request, String owner){
        return new NoteEntity(request.lines(), owner);
    }

}
