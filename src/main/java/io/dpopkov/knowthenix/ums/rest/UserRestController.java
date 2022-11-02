package io.dpopkov.knowthenix.ums.rest;

import io.dpopkov.knowthenix.ums.constants.SecurityConstants;
import io.dpopkov.knowthenix.ums.domain.AppHttpResponse;
import io.dpopkov.knowthenix.ums.domain.AuthUser;
import io.dpopkov.knowthenix.ums.domain.UserPrincipal;
import io.dpopkov.knowthenix.ums.exceptions.domain.AppDomainException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UserNotFoundException;
import io.dpopkov.knowthenix.ums.services.AuthUserService;
import io.dpopkov.knowthenix.ums.utility.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static io.dpopkov.knowthenix.ums.constants.Authority.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private static final String AN_EMAIL_SENT_TO = "An email with a new password sent to: ";

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

    @PostMapping("/register")
    public ResponseEntity<AuthUser> register(@RequestBody AuthUser user) throws AppDomainException {
        // todo: validate email here and in other controller methods
        AuthUser registered = authUserService.register(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail());
        // todo: fix this line below - the actual entity should not be sent as response!
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUser> login(@RequestBody AuthUser user) throws UserNotFoundException {
        authenticate(user.getUsername(), user.getPassword());
        AuthUser loginUser = authUserService.findByUsername(user.getUsername());
        UserPrincipal principal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = createJwtHeader(principal);
        // todo: fix this line below - the actual entity should not be sent as response!
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('" + USER_CREATE + "')")
    public ResponseEntity<AuthUser> addNewUser(@RequestParam("firstName") String firstName,
                                               @RequestParam("lastName") String lastName,
                                               @RequestParam("username") String username,
                                               @RequestParam("email") String email,
                                               @RequestParam("role") String role,
                                               @RequestParam("notLocked") String isNotLocked,
                                               @RequestParam("active") String isActive,
                                               @RequestParam(value = "profileImage",
                                                       required = false) MultipartFile profileImage)
            throws AppDomainException{
        AuthUser newUser = authUserService.addNewUser(firstName, lastName, username, email, role,
                Boolean.parseBoolean(isNotLocked), Boolean.parseBoolean(isActive));
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('" + USER_UPDATE + "')")
    public ResponseEntity<AuthUser> updateUser(@RequestParam("currentUsername") String currentUsername,
                                               @RequestParam("firstName") String firstName,
                                               @RequestParam("lastName") String lastName,
                                               @RequestParam("username") String username,
                                               @RequestParam("email") String email,
                                               @RequestParam("role") String role,
                                               @RequestParam("notLocked") String isNotLocked,
                                               @RequestParam("active") String isActive,
                                               @RequestParam(value = "profileImage",
                                                       required = false) MultipartFile profileImage)
            throws AppDomainException {
        AuthUser updated = authUserService.updateUser(currentUsername, firstName, lastName, username, email, role,
                Boolean.parseBoolean(isNotLocked), Boolean.parseBoolean(isActive));
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/find/{username}")
    @PreAuthorize("hasAuthority('" + USER_READ + "')")
    public ResponseEntity<AuthUser> findByUsername(@PathVariable("username") String username)
            throws UserNotFoundException {
        AuthUser user = authUserService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('" + USER_READ + "')")
    public ResponseEntity<List<AuthUser>> getAllUsers() {
        List<AuthUser> all = authUserService.getAllUsers();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/resetPassword/{email}")
    @PreAuthorize("hasAuthority('" + USER_UPDATE + "')")
    public ResponseEntity<AppHttpResponse> resetPassword(@PathVariable("email") String email) {
        authUserService.resetPassword(email);
        return new ResponseEntity<>(new AppHttpResponse(HttpStatus.OK, AN_EMAIL_SENT_TO + email), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('" + USER_DELETE + "')")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        authUserService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // todo: add methods to update and get user profile image

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
