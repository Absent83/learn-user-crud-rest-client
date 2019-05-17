package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.Role;
import com.myhome.springCrudRestClient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;


@Service
//@Component
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;


    @Autowired
    public UserServiceImpl(ClientHttpRequestFactory clientHttpRequestFactory) {
        restTemplate = new RestTemplate(clientHttpRequestFactory);
    }


    @Override
    public Optional<User> get(long id) {
        User user = restTemplate.getForObject("http://localhost:8081/api/users/" + id, User.class);

        System.out.println("get user by id=" + id + "; response user=" + user.getUsername());

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getByUsername(String username) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/api/users/")
                .queryParam("username", username);

        ResponseEntity<List<User>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>(){});
        List<User> users = response.getBody();

        return Optional.ofNullable(users.get(0));
    }


    @Override
    public List<User> getAll() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                "http://localhost:8081/api/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>(){});
        List<User> users = response.getBody();

        return users;
    }

    @Override
    public void add(User user) {

        System.out.println("add user: username=" + user.getUsername());

        HttpEntity<User> request = new HttpEntity<>(user);
        User foo = restTemplate.postForObject("http://localhost:8081/api/users", request, User.class);
    }

    @Override
    public void update(User user) {
        HttpEntity<User> requestUpdate = new HttpEntity<>(user);
        restTemplate.exchange("http://localhost:8081/api/users/" + user.getId(), HttpMethod.PUT, requestUpdate, User.class);
    }

    @Override
    public void delete(long id) {
        restTemplate.delete("http://localhost:8081/api/users/" + id);
    }
}
