package io.dpopkov.knowthenix.ums.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.dpopkov.knowthenix.ums.constants.SecurityConstants.AUTHORITIES;
import static io.dpopkov.knowthenix.ums.constants.SecurityConstants.EXPIRATION_TIME;
import static io.dpopkov.knowthenix.ums.constants.SecurityMessages.*;

@Component
public class JwtProviderImpl implements JwtProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userPrincipal) {
        String[] authorities = getAuthoritiesFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(COMPANY_NAME)
                .withAudience(COMPANY_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm());
    }

    private String[] getAuthoritiesFromUser(UserDetails userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    @Override
    public VerificationResult verifyToken(String token) {
        try {
            JWTVerifier verifier = getJWTVerifier();
            DecodedJWT decoded = verifier.verify(token);
            String username = decoded.getSubject();
            if (!StringUtils.hasText(username)) {
                return VerificationResult.failed();
            }
            Date expiration = decoded.getExpiresAt();
            if (expiration.before(new Date())) {
                return VerificationResult.failed();
            }
            List<GrantedAuthority> authorities = getAuthoritiesFromDecodedToken(decoded);
            return VerificationResult.verified(username, authorities);
        } catch (JWTVerificationException exception) {
            // Not throwing the actual exception so that not to reveal any inner information.
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
    }

    private JWTVerifier getJWTVerifier() {
        return JWT.require(algorithm()).withIssuer(COMPANY_NAME).build();
    }

    private List<GrantedAuthority> getAuthoritiesFromDecodedToken(DecodedJWT decoded) {
        List<String> authorities = decoded.getClaim(AUTHORITIES).asList(String.class);
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC512(secret);
    }
}
