package com.travellog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TravelLogException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();

    public TravelLogException(String message) {
        super(message);
    }

    public TravelLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
