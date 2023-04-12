package com.proxyseller.notemanager.database.services;

import com.proxyseller.notemanager.database.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends MongoRepository<UserEntity, String> {

    @Query(value = "{'likedNoteIds' : ?0}", count = true)
    long numOfLikes(String noteId);

    Optional<UserEntity> findByUsername(String username);

}
