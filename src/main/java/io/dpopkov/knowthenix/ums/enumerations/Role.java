package io.dpopkov.knowthenix.ums.enumerations;

import static io.dpopkov.knowthenix.ums.constants.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN_(SUPER_ADMIN_AUTHORITIES);

    private final String[] authorities;

    Role(String[] authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
