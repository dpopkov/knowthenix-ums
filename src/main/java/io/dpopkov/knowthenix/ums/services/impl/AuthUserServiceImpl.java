package io.dpopkov.knowthenix.ums.services.impl;

import io.dpopkov.knowthenix.ums.domain.AuthUser;
import io.dpopkov.knowthenix.ums.domain.UserPrincipal;
import io.dpopkov.knowthenix.ums.enumerations.Role;
import io.dpopkov.knowthenix.ums.exceptions.domain.EmailExistsException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UserNotFoundException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UsernameExistsException;
import io.dpopkov.knowthenix.ums.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.ums.services.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static io.dpopkov.knowthenix.ums.constants.SecurityMessages.USER_NOT_FOUND_BY_USERNAME;
import static io.dpopkov.knowthenix.ums.constants.AuthUserServiceImplConstants.*;

@Slf4j
@Transactional
@Service
public class AuthUserServiceImpl implements AuthUserService, UserDetailsService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserServiceImpl(AuthUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUser> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            log.trace("{} {}", USER_NOT_FOUND_BY_USERNAME, username);
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME);
        }
        AuthUser user = byUsername.get();
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        UserPrincipal principal = new UserPrincipal(user);
        log.trace("User found by username {}" ,username);
        return principal;
    }

    @Override
    public AuthUser register(String firstName, String lastName, String username, String email)
            throws UsernameExistsException, EmailExistsException {
        validateRegisteringUsernameAndEmail(username, email);
        AuthUser newUser = AuthUser.builder()
                .publicId(generatePublicId())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(encodePassword(generatePassword()))
                .email(email)
                .profileImageUrl(getTemporaryProfileImageUrl())
                .joinDate(new Date())
                .role(Role.ROLE_USER.name())
                .authorities(Arrays.asList(Role.ROLE_USER.getAuthorities()))
                .active(true)
                .notLocked(true)
                .build();
        AuthUser savedUser = userRepository.save(newUser);
        log.trace("Saved user '{}'", savedUser.getUsername());
        return savedUser;
    }

    private String generatePublicId() {
        return RandomStringUtils.randomNumeric(16);
    }

    private String generatePassword() {
        String password = RandomStringUtils.randomAlphanumeric(16);
        // todo: remove logging of generated password when the complete functionality is ready.
        log.trace("Generated password: {}", password);
        return password;
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + "/temp")
                .toUriString();
    }

    private void validateRegisteringUsernameAndEmail(String newUsername, String newEmail)
            throws UsernameExistsException, EmailExistsException {
        if (findByUsername(newUsername).isPresent()) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
        if (findByEmail(newEmail).isPresent()) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
    }

    private AuthUser validateUpdatingUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        var byCurrentUsername = findByUsername(currentUsername);
        if (byCurrentUsername.isEmpty()) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
        }
        AuthUser currentUser = byCurrentUsername.get();
        checkForOtherUserWithThisUsername(currentUser, newUsername);
        checkForOtherUserWithThisEmail(currentUser, newEmail);
        return currentUser;
    }

    private void checkForOtherUserWithThisUsername(AuthUser currentUser, String newUsername) throws UsernameExistsException {
        var byNewUsername = findByUsername(newUsername);
        if (byNewUsername.isPresent() && currentUser.isNotSameById(byNewUsername.get())) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
    }

    private void checkForOtherUserWithThisEmail(AuthUser currentUser, String newEmail) throws EmailExistsException {
        var byByNewEmail = findByEmail(newEmail);
        if (byByNewEmail.isPresent() && currentUser.isNotSameById(byByNewEmail.get())) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public List<AuthUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<AuthUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
