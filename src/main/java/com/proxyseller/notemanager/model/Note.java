package com.proxyseller.notemanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Note(@JsonProperty("_id") String _id,
                   @JsonProperty("lines") String lines,
                   @JsonProperty("likes") long likes) {}
