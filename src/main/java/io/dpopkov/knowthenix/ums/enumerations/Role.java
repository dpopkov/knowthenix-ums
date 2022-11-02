package io.dpopkov.knowthenix.ums.enumerations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.dpopkov.knowthenix.ums.constants.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    private final String[] authorities;

    Role(String[] authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    /** Returns a standard dynamic array list of Strings representing authorities. */
    public List<String> getAuthoritiesAsList() {
        return new ArrayList<>(Arrays.asList(authorities));
    }
}
