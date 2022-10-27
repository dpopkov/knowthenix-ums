package io.dpopkov.knowthenix.ums.filters;

import io.dpopkov.knowthenix.ums.domain.AppHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.dpopkov.knowthenix.ums.constants.SecurityMessages.ACCESS_DENIED_MESSAGE;
import static io.dpopkov.knowthenix.ums.filters.HttpResponseUtils.writeToHttpResponse;

/**
 * Custom handler to take control over the Access Denied (non-authenticated) situation.
 */
@Component
public class JwtAuthAccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AccessDeniedException ex)
            throws IOException {
        AppHttpResponse appResponse = new AppHttpResponse(HttpStatus.UNAUTHORIZED, ACCESS_DENIED_MESSAGE);
        writeToHttpResponse(httpResponse, appResponse);
    }
}
