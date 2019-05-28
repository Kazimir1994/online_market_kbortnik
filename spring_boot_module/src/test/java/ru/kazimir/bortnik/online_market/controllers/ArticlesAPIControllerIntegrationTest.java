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
import ru.kazimir.bortnik.online_market.service.model.ArticleDTO;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

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
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/articles/2";
        ArticleDTO articleDTO = restTemplate
                .withBasicAuth(login, password)
                .getForObject(Url, ArticleDTO.class);
        Long id = 2L;
        Assert.assertEquals(id, articleDTO.getId());
    }

    @Test
    public void shouldDeleteArticle() {

        final String Url = "http://localhost:" + randomServerPort + "/api/v1/articles/1";
        ArticleDTO articleDTO = restTemplate
                .withBasicAuth(login, password)
                .getForObject(Url, ArticleDTO.class);
        Long id = 1L;
        Assert.assertEquals(id, articleDTO.getId());

        restTemplate
                .withBasicAuth(login, password)
                .delete(Url, ResponseEntity.class);

        ArticleDTO articleDTO2 = restTemplate
                .withBasicAuth(login, password)
                .getForObject(Url, ArticleDTO.class);

        Assert.assertNull(articleDTO2);
    }

    @Test
    public void shouldSaveArticle() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/articles";
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setContent("Test_Test_Test_Test");
        articleDTO.setTitle("Test_Test_Test_Test");
        ThemeDTO themeDTO=new ThemeDTO();
        themeDTO.setId(1L);
        articleDTO.setThemeDTO(themeDTO);

        ResponseEntity responseEntity = restTemplate
                .withBasicAuth(login, password)
                .postForEntity(Url, articleDTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }
}
