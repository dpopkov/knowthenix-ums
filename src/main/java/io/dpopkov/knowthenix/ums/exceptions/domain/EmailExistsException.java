package io.dpopkov.knowthenix.ums.exceptions.domain;

public class EmailExistsException extends Exception {

    public EmailExistsException(String message) {
        super(message);
    }
}
