package com.proxyseller.notemanager.services.implementation;

import com.proxyseller.notemanager.database.entities.NoteEntity;
import com.proxyseller.notemanager.database.entities.UserEntity;
import com.proxyseller.notemanager.database.services.NoteEntityRepository;
import com.proxyseller.notemanager.database.services.UserEntityRepository;
import com.proxyseller.notemanager.model.Note;
import com.proxyseller.notemanager.model.request.CreateNewNoteRequest;
import com.proxyseller.notemanager.services.definitions.NoteManaging;
import com.proxyseller.notemanager.services.mappers.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoteService implements NoteManaging {

    @Autowired
    NoteEntityRepository noteRepository;

    @Autowired
    UserEntityRepository userRepository;

    @Override
    public Note getNote(String id) {
        Optional<NoteEntity> note = noteRepository.findById(id);
        if (note.isPresent())
            return NoteMapper.mapEntity(note.get(), userRepository.numOfLikes(note.get().id));
        else throw new NoSuchElementException();
    }

    @Override
    public List<Note> getNotes(PageRequest paging){
        return noteRepository.findAll(paging)
                .get().map(noteEntity ->
                        NoteMapper.mapEntity(noteEntity, userRepository.numOfLikes(noteEntity.id)))
                .collect(Collectors.toList());
    }

    @Override
    public Note createNote(CreateNewNoteRequest request, Optional<String> user) {
        return NoteMapper.mapEntity(
                noteRepository.insert(NoteMapper.createEntity(request, user.orElse(null))), 0);
    }

    @Override
    public void likeNote(String id, String user) {
        userRepository.findByUsername(user).ifPresentOrElse(u -> {
            u.likedNoteIds.add(id);
            userRepository.save(u);
        }, () -> userRepository.insert(new UserEntity(user,Set.of(id))));
    }

    @Override
    public void unlikeNote(String id, String user) {
        userRepository.findByUsername(user).ifPresent(u -> {
            u.likedNoteIds.remove(id);
            userRepository.save(u);
        });
    }
}
