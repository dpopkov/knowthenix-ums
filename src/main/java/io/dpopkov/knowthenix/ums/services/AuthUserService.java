package io.dpopkov.knowthenix.ums.services;

import io.dpopkov.knowthenix.ums.domain.AuthUser;
import io.dpopkov.knowthenix.ums.exceptions.domain.EmailExistsException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UsernameExistsException;

import java.util.List;
import java.util.Optional;

public interface AuthUserService {

    AuthUser register(String firstName, String lastName, String username, String email)
            throws UsernameExistsException, EmailExistsException;

    List<AuthUser> getAllUsers();

    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByEmail(String email);
}
