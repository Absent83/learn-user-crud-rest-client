package com.myhome.springCrudRestClient.controllers;

import com.myhome.springCrudRestClient.model.Role;
import com.myhome.springCrudRestClient.model.User;
import com.myhome.springCrudRestClient.model.dto.UserForm;
import com.myhome.springCrudRestClient.service.RoleService;
import com.myhome.springCrudRestClient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
public class UserRestController {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping(path = "/api/users")
    public List<User> getAllUsers(@RequestParam(name = "username", required = false) String username){

        List<User> users;

        if (username == null || username.isEmpty()){
            users = userService.getAll();
        }
        else {
            users = Collections.singletonList(userService.getByUsername(username).orElseThrow(IllegalArgumentException::new));
        }

        return users;
    }


    @PostMapping(path="/api/users")
    public ResponseEntity<?> addUser(@RequestBody UserForm userForm) {

        User userNew = new User();

        updateUserData(userForm, userNew);

        userNew.setId(0L); //todo

        userService.add(userNew);

        return ResponseEntity.ok("{\"result\" : \"ok\"}");
    }


    @GetMapping(path = "/api/users/{user-id}")
    public User getUser(@PathVariable("user-id") Integer userId){
        return userService.get(userId).orElseThrow(IllegalArgumentException::new);
    }


    @PutMapping(path = "/api/users/{user-id}")
    public User updateUser(@PathVariable("user-id") Integer userId, @RequestBody UserForm userForm) {

        System.out.println("username: " + userForm.getUsername() + "" +
                "firstname:" + userForm.getFirstName() + ""
        );

        User user = userService.get(userId).orElseThrow(IllegalArgumentException::new);

        updateUserData(userForm, user);

        userService.update(user);

        return user;
    }


    @DeleteMapping(path = "/api/users/{user-id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user-id") Integer userId) {

        userService.delete(userId);

        return ResponseEntity.ok("{\"result\" : \"ok\"}");
    }


    private void updateUserData(UserForm userForm, User user) {
        user.setUsername(userForm.getUsername());
        user.setFirstName(userForm.getFirstName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());

        Set<Role> roles = new HashSet<>();

        for (Integer roleId : userForm.getRoles()) {
            roles.add(roleService.get(roleId).orElseThrow(IllegalArgumentException::new));
        }
        user.setRoles(roles);
    }
}
