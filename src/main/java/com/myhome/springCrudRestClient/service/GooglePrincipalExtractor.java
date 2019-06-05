package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Component
public class GooglePrincipalExtractor implements PrincipalExtractor {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String email = (String) map.get("email");

        User user = userService.getByEmail(email).orElseThrow(IllegalArgumentException::new);

        System.out.println("===> extractPrincipal <===");
        System.out.println("user: " + user.getId() + " " + user.getUsername());

        return user;
    }
}