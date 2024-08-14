package com.teama.server.controllers;

import com.teama.server.models.Bungalow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/security_demo")
public class SecurityDemoController {
    @GetMapping("/owner")
    public String getHelloOwner() {
        return "Hello Owner!";
    }

    @GetMapping("/tenant")
    public String getHelloTenant() {
        return "Hello Tenant!";
    }
}
