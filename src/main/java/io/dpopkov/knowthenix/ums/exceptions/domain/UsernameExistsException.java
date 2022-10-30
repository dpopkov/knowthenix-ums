package io.dpopkov.knowthenix.ums.exceptions.domain;

public class UsernameExistsException extends Exception {

    public UsernameExistsException(String message) {
        super(message);
    }
}
