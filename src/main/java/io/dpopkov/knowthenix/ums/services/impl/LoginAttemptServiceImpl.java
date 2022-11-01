package io.dpopkov.knowthenix.ums.services.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.dpopkov.knowthenix.ums.config.SecurityProps;
import io.dpopkov.knowthenix.ums.services.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final int ATTEMPT_INCREMENT = 1;

    private final int maximumNumberOfAttempts;
    private LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptServiceImpl(SecurityProps securityProps) {
        maximumNumberOfAttempts = securityProps.getMaximumLoginAttempts();
        initializeCache(securityProps);
    }

    private void initializeCache(SecurityProps props) {
        loginAttemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(props.getLoginAttemptsExpiration(), TimeUnit.MINUTES)
                .maximumSize(props.getLoginAttemptsCacheLimit())
                .build(new CacheLoader<>() {
            @Override
            public Integer load(String key) {
                return 0;
            }
        });
    }

    @Override
    public void evictUser(String username) {
        loginAttemptCache.invalidate(username);
    }

    @Override
    public void addUserAttempt(String username) {
        try {
            int attempts = loginAttemptCache.get(username) + ATTEMPT_INCREMENT;
            log.trace("User '{}' made {} attempts", username, attempts);
            loginAttemptCache.put(username, attempts);
        } catch (Exception e) {
            log.error("Error adding user to login attempts cache:", e);
        }
    }

    @Override
    public boolean exceededAttemptsLimit(String username) {
        try {
            boolean exceeded = loginAttemptCache.get(username) > maximumNumberOfAttempts;
            if (exceeded) {
                log.info("User '{}' exceeded maximum number of login attempts", username);
            }
            return exceeded;
        } catch (Exception e) {
            log.error("Error checking number of login attempts:", e);
            return true;
        }
    }
}
