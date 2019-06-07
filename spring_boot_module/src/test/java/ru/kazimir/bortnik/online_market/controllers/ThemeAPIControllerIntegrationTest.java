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
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ThemeAPIControllerIntegrationTest {
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
    public void testShouldAddThemeAPI() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/themes";
        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setName("JAVA_CORE");

        ResponseEntity responseEntity =
                restTemplate.withBasicAuth(login, password).
                        postForEntity(Url, themeDTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testNotShouldAdd() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/themes";
        ThemeDTO theme = new ThemeDTO();
        theme.setName("JAVA_CORE");

        ResponseEntity response =
                restTemplate.withBasicAuth(login, password).
                        postForEntity(Url, theme, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void theTestShouldNotPerformAnEmptyName() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/themes";
        ThemeDTO themeDTO = new ThemeDTO();
        ResponseEntity responseEntity =
                restTemplate.withBasicAuth(login, password).
                        postForEntity(Url, themeDTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReturnTheSheetWithTheThemes() {
        final String Url = "http://localhost:" + randomServerPort + "/api/v1/themes";
        ThemeDTO[] articleDTO =
                restTemplate.withBasicAuth(login, password).getForObject(Url, ThemeDTO[].class);
        Assert.assertNotNull(articleDTO);

    }
}
