package com.wevel.wevel_server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/")
    public String redirectToReactApp() {
        return "redirect:http://localhost:3000/index.html";
    }
}