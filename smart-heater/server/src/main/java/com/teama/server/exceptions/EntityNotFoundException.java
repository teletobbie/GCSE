package com.teama.server.exceptions;

public class EntityNotFoundException extends Exception {
    private String entity;
    private Long id;

    public EntityNotFoundException(String entity, Long id) {
        this.entity = entity;
        this.id = id;
    }
}