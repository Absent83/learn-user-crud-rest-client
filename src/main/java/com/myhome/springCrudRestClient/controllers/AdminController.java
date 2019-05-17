package com.myhome.springCrudRestClient.controllers;

import com.myhome.springCrudRestClient.model.User;
import com.myhome.springCrudRestClient.service.RoleService;
import com.myhome.springCrudRestClient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Controller
public class AdminController {

    private final
    UserService userService;

    private final
    RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping(path = "/users/list")
    public ModelAndView getAllUsersList() {

        List<User> users = userService.getAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminpage");
        modelAndView.addObject("usersFromServer", users);
        modelAndView.addObject("rolesFromServer", roleService.getAll());

        return modelAndView;
    }



    @PostMapping(path = "/users/edit")
    public ModelAndView editUserSubmit(User user) {

        userService.update(user);

        RedirectView redirectView = new RedirectView("/users/list");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(redirectView);

        return modelAndView;
    }


    @GetMapping(path = "/users/add")
    public ModelAndView addUserForm() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add");

        modelAndView.addObject("rolesFromServer", roleService.getAll());

        return modelAndView;
    }


    @PostMapping(path = "/users/add")
    public String addUserSubmit(User user) {

        userService.add(user);

        return "redirect:/users/list";
    }


    @RequestMapping(path = "/users/delete")
    public ModelAndView deleteUser(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equals("POST")){

            long userId = Long.parseLong(request.getParameter("userId"));

            userService.delete(userId);

            RedirectView redirectView = new RedirectView("/users/list");
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView(redirectView);

            return modelAndView;
        }
        return null;
    }
}
