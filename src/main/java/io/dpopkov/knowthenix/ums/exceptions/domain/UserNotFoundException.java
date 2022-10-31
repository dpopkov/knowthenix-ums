package io.dpopkov.knowthenix.ums.exceptions.domain;

public class UserNotFoundException extends AppDomainException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
