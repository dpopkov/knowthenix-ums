package io.dpopkov.knowthenix.ums.repositories;

import io.dpopkov.knowthenix.ums.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByEmail(String email);
}
