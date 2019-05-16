package com.myhome.springCrudRestClient.controllers;

import com.myhome.springCrudRestClient.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 * created on 04.04.2019
 */

@Controller
public class ProfileController {

    @GetMapping(path = "/profile")
    public ModelAndView getUserPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");

        String name;

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            name = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        } else {
            name = "annnnnnnnonim";
        }

        modelAndView.addObject("userAuthorizedLogin", name);

        return modelAndView;
    }
}
