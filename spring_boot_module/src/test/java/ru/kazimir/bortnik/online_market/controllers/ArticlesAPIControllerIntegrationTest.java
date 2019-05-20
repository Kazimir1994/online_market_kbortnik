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
import org.springframework.test.context.junit4.SpringRunner;
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ArticlesAPIControllerIntegrationTest {
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
    public void shouldReturnTheSheetWithTheNews() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/articles";
        ArticleDTO[] articleDTO =
                restTemplate.withBasicAuth(login, password).getForObject(Url, ArticleDTO[].class);
        Assert.assertNotNull(articleDTO);

    }

    @Test
    public void mustReturnOneNews() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/articles/3";
        ArticleDTO articleDTO = restTemplate
                .withBasicAuth(login, password)
                .getForObject(Url, ArticleDTO.class);
        Long id = 3L;
        Assert.assertEquals(id, articleDTO.getId());
    }


}
