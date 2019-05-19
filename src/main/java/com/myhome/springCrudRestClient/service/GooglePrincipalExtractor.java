package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.Role;
import com.myhome.springCrudRestClient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */


public class GooglePrincipalExtractor implements PrincipalExtractor {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String email = (String) map.get("email");

        User user = userService.getByEmail(email).orElseGet(() -> {
            User newUser = new User();

            newUser.setUsername((String) map.get("name"));
            newUser.setFirstName((String) map.get("name"));
            newUser.setEmail((String) map.get("email"));

            newUser.setPassword("x");

            Role role = roleService.getByAuthority("USER").orElseThrow(IllegalArgumentException::new);
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            newUser.setRoles(roles);

            userService.add(newUser);

            return newUser;
        });

        return user;
    }
}