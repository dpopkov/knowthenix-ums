package io.dpopkov.knowthenix.ums.listeners;

import io.dpopkov.knowthenix.ums.services.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFailureListener {

    private final LoginAttemptService loginAttemptService;

    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        final Object principal = event.getAuthentication().getPrincipal();
        log.trace("Authentication failed for principal {}", principal);
        if (principal instanceof String) {
            String username = (String) principal;
            loginAttemptService.addUserAttempt(username);
        }
    }
}
