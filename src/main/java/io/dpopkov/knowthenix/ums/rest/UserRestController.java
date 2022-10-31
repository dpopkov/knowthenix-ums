package io.dpopkov.knowthenix.ums.rest;

import io.dpopkov.knowthenix.ums.constants.SecurityConstants;
import io.dpopkov.knowthenix.ums.domain.AuthUser;
import io.dpopkov.knowthenix.ums.domain.UserPrincipal;
import io.dpopkov.knowthenix.ums.exceptions.domain.AppDomainException;
import io.dpopkov.knowthenix.ums.services.AuthUserService;
import io.dpopkov.knowthenix.ums.utility.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final AuthUserService authUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public UserRestController(AuthUserService authUserService,
                              AuthenticationManager authenticationManager,
                              JwtProvider jwtProvider) {
        this.authUserService = authUserService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/home")
    public String home() {
        return "This endpoint works.";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthUser> register(@RequestBody AuthUser user) throws AppDomainException {
        AuthUser registered = authUserService.register(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail());
        // todo: fix this line below - the actual entity should not be sent as response!
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUser> login(@RequestBody AuthUser user) {
        authenticate(user.getUsername(), user.getPassword());
        AuthUser loginUser = authUserService.findByUsername(user.getUsername()).orElseThrow();
        UserPrincipal principal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = createJwtHeader(principal);
        // todo: fix this line below - the actual entity should not be sent as response!
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    private void authenticate(String username, String password)
            throws DisabledException, LockedException, BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders createJwtHeader(UserPrincipal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.JWT_HEADER, jwtProvider.generateToken(principal));
        return headers;
    }
}
