package com.matera.bootcamp.digitalbank.exception;

public class ServiceException extends RuntimeException{

    private static final Long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }
}
