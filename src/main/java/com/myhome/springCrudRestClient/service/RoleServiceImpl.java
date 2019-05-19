package com.myhome.springCrudRestClient.service;

import com.myhome.springCrudRestClient.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

//@Service
@Component
public class RoleServiceImpl implements RoleService {

    private final RestTemplate restTemplate;


    @Autowired
    public RoleServiceImpl(ClientHttpRequestFactory clientHttpRequestFactory) {
        restTemplate = new RestTemplate(clientHttpRequestFactory);
    }


    @Override
    public List<Role> getAll() {

        ResponseEntity<List<Role>> response = restTemplate.exchange(
                "http://localhost:8082/api/roles",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Role>>(){});

        return response.getBody();
    }

    @Override
    public Optional<Role> get(long id) {
        Role role = restTemplate.getForObject("http://localhost:8082/api/roles/" + id, Role.class);
        return Optional.ofNullable(role);
    }

    @Override
    public Optional<Role> getByAuthority(String authority) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/api/roles")
                .queryParam("authority", authority);

        ResponseEntity<Role> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                Role.class);
        Role role = response.getBody();

        return Optional.ofNullable(role);
    }
}
