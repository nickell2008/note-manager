package com.proxyseller.notemanager.database.services;

import com.proxyseller.notemanager.database.entities.NoteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteEntityRepository extends MongoRepository<NoteEntity, String> {
}
