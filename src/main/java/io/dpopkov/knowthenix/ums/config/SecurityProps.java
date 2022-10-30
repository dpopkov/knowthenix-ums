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
}
