package com.tae.Etickette;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/test")
    public String testP() {
        return "test Page";
    }

    @PostMapping("/admin")
    public String adminP() {
        return "admin Page";
    }
}
