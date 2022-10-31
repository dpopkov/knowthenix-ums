package io.dpopkov.knowthenix.ums.rest;

import io.dpopkov.knowthenix.ums.domain.AuthUser;
import io.dpopkov.knowthenix.ums.exceptions.domain.AppDomainException;
import io.dpopkov.knowthenix.ums.services.AuthUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final AuthUserService authUserService;

    public UserRestController(AuthUserService authUserService) {
        this.authUserService = authUserService;
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
}
