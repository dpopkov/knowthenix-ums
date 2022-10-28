package io.dpopkov.knowthenix.ums.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @GetMapping("/home")
    public String home() {
        return "This endpoint works.";
    }

    @GetMapping("/login")
    public String login() {
        return "This must be login page";
    }
}
