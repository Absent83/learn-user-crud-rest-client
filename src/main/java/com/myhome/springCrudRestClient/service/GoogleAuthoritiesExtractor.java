package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.Role;
import com.myhome.springCrudRestClient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

public class GoogleAuthoritiesExtractor implements AuthoritiesExtractor {

    @Autowired
    public UserService userService;

    @Autowired
    RoleService roleService;

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
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

        System.out.println("===> extractAuthorities <===");
        user.getAuthorities().forEach(o -> System.out.println(o.getAuthority()));

        return new ArrayList(user.getAuthorities());

    }
}