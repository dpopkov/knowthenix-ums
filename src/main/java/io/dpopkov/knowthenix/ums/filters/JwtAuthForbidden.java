package io.dpopkov.knowthenix.ums.filters;

import io.dpopkov.knowthenix.ums.domain.AppHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.dpopkov.knowthenix.ums.constants.SecurityMessages.FORBIDDEN_MESSAGE;
import static io.dpopkov.knowthenix.ums.filters.HttpResponseUtils.writeToHttpResponse;

/**
 * Custom entry point to take control over the Access Forbidden situation.
 */
@Component
public class JwtAuthForbidden extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationException ex)
            throws IOException {
        AppHttpResponse appResponse = new AppHttpResponse(HttpStatus.FORBIDDEN, FORBIDDEN_MESSAGE);
        writeToHttpResponse(httpResponse, appResponse);
    }
}
