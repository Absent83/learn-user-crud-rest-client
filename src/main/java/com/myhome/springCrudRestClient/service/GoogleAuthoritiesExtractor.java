package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.Role;
import com.myhome.springCrudRestClient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Component
public class GoogleAuthoritiesExtractor implements AuthoritiesExtractor {

    @Autowired
    public UserService userService;

    @Autowired
    RoleService roleService;

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        String email = (String) map.get("email");

        User user = userService.getByEmail(email).orElseThrow(IllegalArgumentException::new);

        System.out.println("===> extractAuthorities <===");
        user.getAuthorities().forEach(o -> System.out.println(o.getAuthority()));

        return new ArrayList(user.getAuthorities());

    }
}