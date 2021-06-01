package com.sflpro.cafe.exception;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String resource;

    public NotFoundException(Object resource) {
        super("Resource not found");
        this.resource = String.valueOf(resource);
    }

    public String getResource() {
        return resource;
    }
}
