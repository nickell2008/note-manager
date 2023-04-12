package com.proxyseller.notemanager.database.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;


/**
 * Database entity of user
 */
@Document(collection = "users")
public class UserEntity {

    @Id@JsonProperty("_id") public String _id;
    @JsonProperty("username") public String username;
    @JsonProperty("liked_notes") public Set<String> likedNoteIds;

    public UserEntity() {
    }

    public UserEntity(String username, Set<String> likedNoteIds) {
        this.username = username;
        this.likedNoteIds = likedNoteIds;
    }
}
