package com.tae.Etickette;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReactController {

    @GetMapping("/api")
    public String hello() {
        return "안녕";
    }
}
