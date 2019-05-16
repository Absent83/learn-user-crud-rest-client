package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> get(long id);
    Optional<Role> getByAuthority(String authority);

    List<Role> getAll();
}

