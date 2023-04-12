package com.proxyseller.notemanager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateNewNoteRequest(@JsonProperty("lines") String lines) {}
