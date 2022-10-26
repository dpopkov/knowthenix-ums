package io.dpopkov.knowthenix.ums.utility;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
public class VerificationResult {

    private final boolean success;
    private final String username;
    private final List<GrantedAuthority> authorities;

    private VerificationResult(boolean success, String username, List<GrantedAuthority> authorities) {
        this.success = success;
        this.username = username;
        this.authorities = authorities;
    }

    public static VerificationResult verified(String username, List<GrantedAuthority> authorities) {
        return new VerificationResult(true, username, authorities);
    }

    public static VerificationResult failed() {
        return new VerificationResult(false, "", List.of());
    }
}
