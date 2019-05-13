package ru.kazimir.bortnik.online_market.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootModuleApplication.class)
public class SecureIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldRedirectOnLoginPageForSlashPage() throws Exception {
        mockMvc.perform(get("/private/reviews/showing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }


    @Test
    public void shouldRedirectOnItemsPageForUserWhenLogin() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "kazimir@mail.ru")
                .param("password", "admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/private/users/showing"));
    }

    @WithMockUser(authorities = "ADMINISTRATOR")
    @Test
    public void ifYouGoToThePagePrivateUserShouldCome300() throws Exception {
        mockMvc.perform(get("/private/users/showing"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouGoToThePagePrivateUsersShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/showing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouGoToThePagePrivateUserAddUsersShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/form_add_users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouMakeARequestToAddAUserShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/add_users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouMakeARequestToUpdatePasswordShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/change_password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouMakeARequestToUpdateRoleShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/update_role"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouMakeARequestToDeleteUsersShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/delete_users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "ADMINISTRATOR")
    @Test
    public void ifYouGoToThePagePrivateReviewsShouldCome300() throws Exception {
        mockMvc.perform(get("/private/reviews/showing"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouGoToThePagePrivateReviewsShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/reviews/showing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouMakeARequestToUpdateStatusShowingShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/update_showing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    public void ifYouMakeARequestToDeleteReviewsShouldBeDeniedAccessAndRedirectedTo403() throws Exception {
        mockMvc.perform(get("/private/users/delete_reviews"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }
}
