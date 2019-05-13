package ru.kazimir.bortnik.online_market.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersWebControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private UserDTO userTestDTO;
    @Before
    public void init() {
        userTestDTO = new UserDTO();
        userTestDTO.setEmail("kAzImImR@mail.ru");
        userTestDTO.setName("Kazimir");
        userTestDTO.setSurname("Bortnik");
        RoleDTO correctRole = new RoleDTO();
        correctRole.setName("SALE_USER");
        userTestDTO.setRoleDTO(correctRole);
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldBePositiveIfYouCanAddAUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleDTO(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add_users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userTestDTO.getEmail())
                .param("name", userTestDTO.getName())
                .param("surname", userTestDTO.getSurname())
                .param("roleDTO.name", userTestDTO.getRoleDTO().getName())
                .requestAttr("userDTO", userDTO)
        )
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldBePositiveIfYouCanDeleteAUser() throws Exception {
        Long id = 10L;
        this.mockMvc.perform(post("/private/users/delete_users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id_delete_users", id.toString()))
                .andExpect(status().isFound());
    }

}
