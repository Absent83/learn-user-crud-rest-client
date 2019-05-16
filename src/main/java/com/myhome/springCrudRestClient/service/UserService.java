package com.myhome.springCrudRestClient.service;


import com.myhome.springCrudRestClient.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> get(long id);
    Optional<User> getByUsername(String userName);

    List<User> getAll();

    void add(User user);
    void update(User user);
    void delete(long id);
}

