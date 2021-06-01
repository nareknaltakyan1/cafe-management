package com.sflpro.cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusyTableException extends RuntimeException {

    public BusyTableException() {
        super("Table already has open orders");
    }
}
