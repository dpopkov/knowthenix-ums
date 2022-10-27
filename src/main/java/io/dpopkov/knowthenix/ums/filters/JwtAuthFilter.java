package io.dpopkov.knowthenix.ums.filters;

import io.dpopkov.knowthenix.ums.utility.JwtProvider;
import io.dpopkov.knowthenix.ums.utility.VerificationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static io.dpopkov.knowthenix.ums.constants.SecurityConstants.TOKEN_PREFIX;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.trace("Request {} on {}", request.getMethod(), request.getRequestURL());
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
                String token = authHeader.substring(TOKEN_PREFIX.length());
                VerificationResult result = jwtProvider.verifyToken(token);
                if (result.isSuccess()) {
                    String username = result.getUsername();
                    List<GrantedAuthority> authorities = result.getAuthorities();
                    Authentication authentication = getAuthentication(username, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String username,
                                             List<GrantedAuthority> authorities,
                                             HttpServletRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
