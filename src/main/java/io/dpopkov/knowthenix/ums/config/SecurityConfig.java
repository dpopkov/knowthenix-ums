package io.dpopkov.knowthenix.ums.config;

import io.dpopkov.knowthenix.ums.constants.SecurityConstants;
import io.dpopkov.knowthenix.ums.filters.JwtAuthAccessDenied;
import io.dpopkov.knowthenix.ums.filters.JwtAuthFilter;
import io.dpopkov.knowthenix.ums.filters.JwtAuthForbidden;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] ANY_URL = {"**"};

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthAccessDenied jwtAuthAccessDenied;
    private final JwtAuthForbidden jwtAuthForbidden;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityProps securityProps;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, JwtAuthAccessDenied jwtAuthAccessDenied,
                          JwtAuthForbidden jwtAuthForbidden, UserDetailsService userDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder, SecurityProps securityProps) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthAccessDenied = jwtAuthAccessDenied;
        this.jwtAuthForbidden = jwtAuthForbidden;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityProps = securityProps;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(securityProps.isPermitall() ? ANY_URL : SecurityConstants.PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
            .and()
                .exceptionHandling()
                .accessDeniedHandler(jwtAuthAccessDenied)
                .authenticationEntryPoint(jwtAuthForbidden)
            .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // For h2 console
        http.headers().frameOptions().disable();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
}
