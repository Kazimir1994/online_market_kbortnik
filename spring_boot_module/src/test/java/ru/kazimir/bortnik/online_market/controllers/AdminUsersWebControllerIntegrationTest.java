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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kazimir.bortnik.online_market.constant.RoleConstants.ADMIN_ROLE_NAME;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminUsersWebControllerIntegrationTest {
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
        correctRole.setId(1L);
        correctRole.setName("SALE_USER");
        userTestDTO.setRoleDTO(correctRole);
    }


    @WithMockUser(authorities = {ADMIN_ROLE_NAME})
    @Test
    public void aUserMustBeAddedAndRedirectedToTheAddUserPage() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleDTO(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add_users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userTestDTO.getEmail())
                .param("name", userTestDTO.getName())
                .param("surname", userTestDTO.getSurname())
                .param("roleDTO.id", String.valueOf(userTestDTO.getRoleDTO().getId()))
                .requestAttr("userDTO", userDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/form_add_users"));
    }

    @WithMockUser(authorities = {ADMIN_ROLE_NAME})
    @Test
    public void ifWhenCreatingAUserANonExistingRoleComesUserShouldNotBeCreatedAndAddAttributeError() throws Exception {
        Long idRole = 1010L;
        this.mockMvc.perform(post("/private/users/add_users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userTestDTO.getEmail())
                .param("name", userTestDTO.getName())
                .param("surname", userTestDTO.getSurname())
                .param("roleDTO.id", idRole.toString())
        )
                .andExpect(MockMvcResultMatchers.flash().attributeExists("error"))
                .andExpect(redirectedUrl("/private/users/form_add_users"));
    }

    @WithMockUser(authorities = {ADMIN_ROLE_NAME})
    @Test
    public void shouldRedirectToPage422IfWeAreTryingToDeleteAUserByIdWhichDoesNotExist() throws Exception {
        Long id = 100L;
        this.mockMvc.perform(post("/private/users/delete_users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id_delete_users", id.toString()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/error/422"));
    }

    @WithMockUser(authorities = {ADMIN_ROLE_NAME})
    @Test
    public void shouldBePositiveIfYouCanDeleteAUser() throws Exception {
        Long id = 1L;
        this.mockMvc.perform(post("/private/users/delete_users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id_delete_users", id.toString()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/showing"));
    }

    @WithMockUser(authorities = {ADMIN_ROLE_NAME})
    @Test
    public void IfTheIncomingEmailIsValidatedUpdateThePasswordAndReturnToThePageDisplayUsers()
            throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("kazimir@mail.ru");
        this.mockMvc.perform(post("/private/users/change_password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userTestDTO.getEmail()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/error/422"));
    }

    @WithMockUser(authorities = {ADMIN_ROLE_NAME})
    @Test
    public void ifANonExistingEmailHasArrivedToUpdateThePasswordSendARedirectTo422()
            throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("kaz9@mari.ru");
        this.mockMvc.perform(post("/private/users/change_password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", userDTO.getEmail()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/error/422"));
    }
}
