package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        User user = restTemplate.getForObject("http://localhost:8082/api/users/" + id, User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getByUsername(String username) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/api/users/")
                .queryParam("username", username);

        return Optional.ofNullable(getUser(builder));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/api/users/")
                .queryParam("email", email);

        User user = getUser(builder);

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                "http://localhost:8082/api/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>(){});

        return response.getBody();
    }

    @Override
    public void add(User user) {
        HttpEntity<User> request = new HttpEntity<>(user);
        restTemplate.postForObject("http://localhost:8082/api/users", request, User.class);
    }

    @Override
    public void update(User user) {
        HttpEntity<User> requestUpdate = new HttpEntity<>(user);
        restTemplate.exchange("http://localhost:8082/api/users/" + user.getId(), HttpMethod.PUT, requestUpdate, User.class);
    }

    @Override
    public void delete(long id) {
        restTemplate.delete("http://localhost:8082/api/users/" + id);
    }

    private User getUser(UriComponentsBuilder builder) {
        ResponseEntity<User> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                User.class);
        User user = response.getBody();

        return user;
    }
}
