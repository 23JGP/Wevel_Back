package com.wevel.wevel_server.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class IndexController {

    @GetMapping("/")
    public String home(){
        return "redirect:http://172.18.48.1:5500/index.html";
    }
    @GetMapping("/toekn")
    public OAuth2AuthenticationToken toekn(final OAuth2AuthenticationToken token){
        return token;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
