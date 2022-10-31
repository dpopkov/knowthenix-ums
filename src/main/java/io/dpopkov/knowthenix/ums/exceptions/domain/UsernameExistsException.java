package io.dpopkov.knowthenix.ums.exceptions.domain;

public class UsernameExistsException extends AppDomainException {

    public UsernameExistsException(String message) {
        super(message);
    }
}
