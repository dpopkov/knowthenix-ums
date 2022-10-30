package io.dpopkov.knowthenix.ums.rest;

import io.dpopkov.knowthenix.ums.domain.AppHttpResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class AppErrorController implements ErrorController {

    @ResponseBody
    @GetMapping(path = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppHttpResponse> error() {
        AppHttpResponse response = new AppHttpResponse(NOT_FOUND, "There is no mapping for this URL.");
        return new ResponseEntity<>(response, NOT_FOUND);
    }
}
