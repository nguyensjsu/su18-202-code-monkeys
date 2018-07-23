package edu.sjsu.cmpe202.starbucks.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthResource {
    @RequestMapping("/_ah/health")
    public String healthy() {
        return "I'm alive!";
    }
}
