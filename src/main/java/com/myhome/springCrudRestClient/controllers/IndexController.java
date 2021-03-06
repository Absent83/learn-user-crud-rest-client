package com.myhome.springCrudRestClient.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Controller
public class IndexController {

    @GetMapping(path = {"", "/"})
    public String getLoginPage() {
        return "index";
    }
}
