package io.dpopkov.knowthenix.ums.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dpopkov.knowthenix.ums.domain.AppHttpResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class HttpResponseUtils {
    public static void writeToHttpResponse(HttpServletResponse response, AppHttpResponse appHttpResponse)
            throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(appHttpResponse.getHttpStatusCode());
        ServletOutputStream outputStream = response.getOutputStream();
        new ObjectMapper().writeValue(outputStream, appHttpResponse);
        outputStream.flush();
    }
}
