package io.dpopkov.knowthenix.ums.services;

import io.dpopkov.knowthenix.ums.domain.AuthUser;
import io.dpopkov.knowthenix.ums.exceptions.domain.EmailExistsException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UserNotFoundException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UsernameExistsException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AuthUserService {

    /** Used in case when a user is not logged in and needs to register itself. */
    AuthUser register(String firstName, String lastName, String username, String email)
            throws UsernameExistsException, EmailExistsException;

    List<AuthUser> getAllUsers();

    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByEmail(String email);

    // todo: for next 2 methods add MultipartFile for profile image

    /** Used in case when a user is logged in and needs to add another user. */
    AuthUser addNewUser(String firstName, String lastName, String username, String email,
                        String role, boolean isNotLocked, boolean isActive)
            throws EmailExistsException, UsernameExistsException;

    AuthUser updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                        String newEmail, String role, boolean isNotLocked, boolean isActive)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException;

    void deleteUser(Long id);

    default void resetPassword(String email) {
        // todo: implement resetting password after Notification by Email is finished
        throw new UnsupportedOperationException("Reset password is not implemented yet");
    }

    default AuthUser updateProfileImage(String username, MultipartFile profileImage) {
        // todo: implement updating image after profile image is finished
        throw new UnsupportedOperationException("Updating image is not implemented yet");
    }
}
