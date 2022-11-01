package io.dpopkov.knowthenix.ums.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProps {

    /** Allows access to all URLs of the application. */
    private boolean permitall = false;

    /** Maximum number of login attempts before locking user account. */
    private int maximumLoginAttempts = 5;

    /** Number of minutes after which login attempts cache expires for an entry. */
    private int loginAttemptsExpiration = 15;

    /** The maximum number of entries the login attempts cache may contain. */
    private int loginAttemptsCacheLimit = 100;
}
