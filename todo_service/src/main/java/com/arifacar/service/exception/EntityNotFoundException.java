package com.arifacar.service.exception;


public class EntityNotFoundException extends RuntimeException {

    private final Class entity;

    private final int id;

    public EntityNotFoundException(Class entity, int id) {
        this.entity = entity;
        this.id = id;
    }

    public Class getEntity() {
        return entity;
    }

    public int getId() {
        return id;
    }
}
