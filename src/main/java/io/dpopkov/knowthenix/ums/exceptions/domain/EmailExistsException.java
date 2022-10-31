package io.dpopkov.knowthenix.ums.exceptions.domain;

public class EmailExistsException extends AppDomainException {

    public EmailExistsException(String message) {
        super(message);
    }
}
