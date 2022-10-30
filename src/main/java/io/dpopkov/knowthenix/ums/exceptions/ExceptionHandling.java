package io.dpopkov.knowthenix.ums.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.dpopkov.knowthenix.ums.domain.AppHttpResponse;
import io.dpopkov.knowthenix.ums.exceptions.domain.EmailExistsException;
import io.dpopkov.knowthenix.ums.exceptions.domain.EmailNotFoundException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UserNotFoundException;
import io.dpopkov.knowthenix.ums.exceptions.domain.UsernameExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionHandling {

    private static final String ACCOUNT_LOCKED_MSG = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED_FMT = "This request method is not allowed on this endpoint. Please send %s request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS_MSG = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED_MSG = "Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE_MSG = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION_MSG = "You do not have enough permission";

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<AppHttpResponse> accountDisabledException() {
        return createResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED_MSG);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppHttpResponse> badCredentialsException() {
        return createResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS_MSG);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AppHttpResponse> accessDeniedException() {
        return createResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION_MSG);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<AppHttpResponse> lockedException() {
        return createResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED_MSG);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<AppHttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<AppHttpResponse> emailExistsException(EmailExistsException exception) {
        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<AppHttpResponse> usernameExistsException(UsernameExistsException exception) {
        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<AppHttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<AppHttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<AppHttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        Set<HttpMethod> httpMethods = exception.getSupportedHttpMethods();
        return createResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                String.format(METHOD_IS_NOT_ALLOWED_FMT, httpMethods != null ? httpMethods.toString() : "[]")
        );
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<AppHttpResponse> notFoundException(NoResultException exception) {
        return createResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<AppHttpResponse> iOException() {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE_MSG);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppHttpResponse> internalServerErrorException(Exception exception) {
        log.error("Server Exception: ", exception);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    private ResponseEntity<AppHttpResponse> createResponse(HttpStatus status, String message) {
        log.error("Error status: '{}', message: '{}'", status.value(), message);
        return new ResponseEntity<>(new AppHttpResponse(status, message), status);
    }
}
