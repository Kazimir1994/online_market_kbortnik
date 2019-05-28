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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ItemAPIControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    final private String password = "user";
    final private String login = "Api@mail.ru";

    @Before
    public void init() {
        restTemplate.withBasicAuth(login, password);
    }

    @Test
    public void shouldReturnTheSheetWithTheItems() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/items";
        ItemDTO[] itemDTOS =
                restTemplate.withBasicAuth(login, password).getForObject(Url, ItemDTO[].class);
        Assert.assertNotNull(itemDTOS);
    }

    @Test
    public void mustReturnOneItems() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/items/2";
        ItemDTO itemDTO = restTemplate
                .withBasicAuth(login, password)
                .getForObject(Url, ItemDTO.class);
        Long id = 2L;
        Assert.assertEquals(id, itemDTO.getId());
    }

    @Test
    public void shouldReturnTheStatusNotFound() {
        final String uri = "http://localhost:" + randomServerPort + "/api/v1/items/2444";
        ResponseEntity<ItemDTO> responseEntity = restTemplate.withBasicAuth(login, password).getForEntity(uri, ItemDTO.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void ifYouDeleteTheItemWhichDoesNotExistShouldCome() {
        final String uri = "http://localhost:" + randomServerPort + "/api/v1/items/2444";
        ResponseEntity responseEntity = restTemplate
                .withBasicAuth(login, password).exchange(uri, HttpMethod.DELETE,null,ResponseEntity.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void shouldDeleteItems() {
        final String uri = "http://localhost:" + randomServerPort + "/api/v1/items/1";
        ResponseEntity responseEntity = restTemplate
                .withBasicAuth(login, password).exchange(uri, HttpMethod.DELETE,null,ResponseEntity.class);
        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldSaveArticle() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/items";
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("Name1");
        itemDTO.setPrice("23.4");
        itemDTO.setShortDescription("test test tes tes trs");
        ResponseEntity responseEntity = restTemplate
                .withBasicAuth(login, password)
                .postForEntity(Url, itemDTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
