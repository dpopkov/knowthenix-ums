package io.dpopkov.knowthenix.ums.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
public class AppHttpResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Date timestamp = new Date();
    private final HttpStatus httpStatus;
    private final int httpStatusCode;
    private final String reason;
    private final String message;

    /**
     * Constructs the instance using httpStatus only and initializes other fields with values from httpStatus.
     * @param httpStatus standard httpStatus
     */
    public AppHttpResponse(HttpStatus httpStatus) {
        this(httpStatus, httpStatus.value(), httpStatus.getReasonPhrase(), httpStatus.getReasonPhrase());
    }

    /**
     * Constructs the instance using httpStatus and message only.
     * It initializes other fields with values from httpStatus.
     * @param httpStatus standard httpStatus
     * @param message message
     */
    public AppHttpResponse(HttpStatus httpStatus, String message) {
        this(httpStatus, httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

    /**
     * Constructs the instance using httpStatus only and initializes status int code with value from httpStatus.
     * This constructor may be used for i18n purposes.
     * @param httpStatus standard httpStatus
     * @param reason reason phrase
     * @param message message
     */
    public AppHttpResponse(HttpStatus httpStatus, String reason, String message) {
        this(httpStatus, httpStatus.value(), reason, message);
    }

    /**
     * All args constructor that may be used for i18n purposes.
     * @param httpStatus standard httpStatus
     * @param httpStatusCode standard status code
     * @param reason reason phrase
     * @param message message
     */
    public AppHttpResponse(HttpStatus httpStatus, int httpStatusCode, String reason, String message) {
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatusCode;
        this.reason = reason.toUpperCase();
        this.message = message;
    }
}
