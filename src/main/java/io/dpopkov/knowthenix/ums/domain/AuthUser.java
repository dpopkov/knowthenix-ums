package io.dpopkov.knowthenix.ums.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String publicId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String role;    // ROLE_USER, ROLE_EDITOR, ROLE_ADMIN
    @ElementCollection
    private Collection<String> authorities = new ArrayList<>();
    private boolean active;
    private boolean notLocked;

    public boolean isNotSameById(AuthUser other) {
        return other == null || id == null || !id.equals(other.getId());
    }
}
