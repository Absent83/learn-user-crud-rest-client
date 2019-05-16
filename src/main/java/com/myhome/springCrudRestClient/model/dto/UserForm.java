package com.myhome.springCrudRestClient.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Set;

public class UserForm {

    private String username;

    private String firstName;
    private String email;
    private String password;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private Set<Integer> roles;


    public UserForm() {
    }

    public UserForm(String username, String firstName, String email, String password, Set<Integer> roles) {
        this.username = username;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Integer> getRoles() {
        return roles;
    }

    public void setRoles(Set<Integer> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
