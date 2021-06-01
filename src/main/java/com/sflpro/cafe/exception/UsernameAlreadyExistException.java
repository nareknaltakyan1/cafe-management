package com.sflpro.cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException() {
        super("User with such username is already registered");
    }
}
