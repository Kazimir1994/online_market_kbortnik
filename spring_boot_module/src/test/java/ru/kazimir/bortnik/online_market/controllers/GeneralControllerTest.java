package ru.kazimir.bortnik.online_market.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GeneralControllerTest {

    private MockMvc mockMvc;

    @Before
    public void init() {
        GeneralController generalController = new GeneralController();
        mockMvc = MockMvcBuilders.standaloneSetup(generalController).build();
    }

    @Test
    public void shouldSucceedWith200ForHomePage() throws Exception {
        this.mockMvc.perform(get("/home.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("home"));
    }

    @Test
    public void shouldSucceedWith200ForLoginPage() throws Exception {
        this.mockMvc.perform(get("/login.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("login"));
    }

}