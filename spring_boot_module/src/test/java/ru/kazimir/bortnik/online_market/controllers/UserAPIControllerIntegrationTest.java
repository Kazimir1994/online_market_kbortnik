package ru.kazimir.bortnik.online_market.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kazimir.bortnik.online_market.service.model.ProfileDTO;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserAPIControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private UserDTO userTestDTO;

    @LocalServerPort
    int randomServerPort;

    final private String password = "user";
    final private String login = "Api@mail.ru";

    @Before
    public void init() {
        userTestDTO = new UserDTO();
        userTestDTO.setEmail("kAzImImR@mail.ru");
        userTestDTO.setName("Kazimir");
        userTestDTO.setSurname("Bortnik");
        RoleDTO correctRole = new RoleDTO();
        correctRole.setId(1L);
        correctRole.setName("ADMINISTRATOR");
        userTestDTO.setRoleDTO(correctRole);
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setTelephone("+375447598943");
        profileDTO.setResidentialAddress("frolikova 32");
        userTestDTO.setProfileDTO(profileDTO);
    }

    @Test
    public void testShouldAddUserAPI() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/users";
        ResponseEntity responseEntity =
                restTemplate.withBasicAuth(login, password).postForEntity(Url, userTestDTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testNotShouldAdd() {
        UserDTO userTest2DTO = new UserDTO();
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/users";
        ResponseEntity responseEntity =
                restTemplate.withBasicAuth(login, password).postForEntity(Url, userTest2DTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
