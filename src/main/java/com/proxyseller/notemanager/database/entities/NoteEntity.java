package com.proxyseller.notemanager.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Database entity of note
 */
@Document(collection = "notes")
public class NoteEntity {
    @Id @JsonProperty("_id") public String id;
    @JsonProperty("lines") public String lines;
    @JsonProperty("created") public Date created = new Date();
    @JsonProperty("creator") public String creator;

    public NoteEntity() {
    }

    public NoteEntity(String lines) {
        this.lines = lines;
    }

    public NoteEntity(String lines, String creator){
        this.lines = lines;
        this.creator = creator;
    }
}
