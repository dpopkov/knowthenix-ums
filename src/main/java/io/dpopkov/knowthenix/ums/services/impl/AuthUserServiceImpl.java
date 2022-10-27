package io.dpopkov.knowthenix.ums.services.impl;

import io.dpopkov.knowthenix.ums.domain.AuthUser;
import io.dpopkov.knowthenix.ums.domain.UserPrincipal;
import io.dpopkov.knowthenix.ums.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.ums.services.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static io.dpopkov.knowthenix.ums.constants.SecurityMessages.USER_NOT_FOUND_BY_USERNAME;

@Slf4j
@Transactional
@Service
public class AuthUserServiceImpl implements AuthUserService, UserDetailsService {

    private final AuthUserRepository userRepository;

    public AuthUserServiceImpl(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
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
}
