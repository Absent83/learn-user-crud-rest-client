package com.myhome.springCrudRestClient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    private Long id;

    private String username;

    private String firstName;

    private String email;

    private String password;

    private Set<Role> roles;

    public User() {
    }

    public User(String username, String firstName, String email, String password, Set<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }


    public long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String name) {
        this.firstName = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public Set<Role> getRoles() {
        return roles;
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                username.equals(user.username);
    }
}
