package ru.kazimir.bortnik.online_market.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.validators.AddUserValidatorImpl;
import ru.kazimir.bortnik.online_market.validators.RoleValidatorImpl;
import ru.kazimir.bortnik.online_market.validators.UpdatePasswordValidatorImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UsersWebControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private AddUserValidatorImpl addUserValidatorImpl;
    @Mock
    private RoleValidatorImpl roleValidatorImpl;
    @Mock
    private UpdatePasswordValidatorImpl updatePasswordValidatorImpl;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private
    RedirectAttributes redirectAttributes;

    private MockMvc mockMvc;
    private UsersWebController usersWebController;
    private List<UserDTO> userDTOS = new ArrayList<>();
    private List<RoleDTO> roleDTOS = new ArrayList<>();

    @Before
    public void init() {
        usersWebController = new UsersWebController(userService, roleService, addUserValidatorImpl, roleValidatorImpl, updatePasswordValidatorImpl);
        mockMvc = MockMvcBuilders.standaloneSetup(usersWebController).build();

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setEmail("Test@mail.ru");
        userDTO1.setSurname("SurnameTest");
        userDTO1.setName("NameTest");
        userDTO1.setPatronymic("PatronymicTest");
        RoleDTO roleDTO1 = new RoleDTO();
        roleDTO1.setName("ADMINISTRATOR");
        userDTO1.setRoleDTO(roleDTO1);

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setEmail("Test@mail.ru");
        userDTO2.setSurname("SurnameTest");
        userDTO2.setName("NameTest");
        userDTO2.setPatronymic("PatronymicTest");
        RoleDTO roleDTO2 = new RoleDTO();
        roleDTO2.setName("ADMINISTRATOR");
        userDTO2.setRoleDTO(roleDTO2);

        userDTOS.add(userDTO1);
        userDTOS.add(userDTO2);

        RoleDTO roleDTO11 = new RoleDTO();
        roleDTO1.setName("ADMINISTRATOR");
        RoleDTO roleDTO22 = new RoleDTO();
        roleDTO2.setName("CUSTOMER_USER");
        RoleDTO roleDTO3 = new RoleDTO();
        roleDTO3.setName("SALE_USER");
        RoleDTO roleDTO4 = new RoleDTO();
        roleDTO4.setName("SECURE_API_USER");
        roleDTOS.add(roleDTO11);
        roleDTOS.add(roleDTO22);
        roleDTOS.add(roleDTO3);
        roleDTOS.add(roleDTO4);
    }

    @Test
    public void shouldGetAUserPageWithAFilledTable() throws Exception {
        when(userService.getUsers(10L, 0L)).thenReturn(userDTOS);
        mockMvc.perform(get("/private/users/showing.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", equalTo(userDTOS)))
                .andExpect(forwardedUrl("private_users"));
    }

    @Test
    public void shouldGetAPageWithAListOfRoles() throws Exception {
        when(roleService.getRoles()).thenReturn(roleDTOS);
        mockMvc.perform(get("/private/users/showing.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", equalTo(roleDTOS)))
                .andExpect(forwardedUrl("private_users"));
    }

    @Test
    public void shouldGetAPageWithFilledPaginationFields() throws Exception {
        when(userService.getNumberOfPages(10L)).thenReturn(5L);
        mockMvc.perform(get("/private/users/showing.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("SizePage", equalTo(5L)))
                .andExpect(model().attribute("currentPage", equalTo(1L)))
                .andExpect(forwardedUrl("private_users"));
    }

    @Test
    public void shouldReturnTheAddUserPageWithTheRoleFieldsFilled() throws Exception {
        when(roleService.getRoles()).thenReturn(roleDTOS);
        mockMvc.perform(get("/private/users/form_add_users.html"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", equalTo(roleDTOS)))
                .andExpect(forwardedUrl("private_addUsers"));
    }

    @Test
    public void ifTheIncomingSheetIdIsNullThenTheMethodForDeletionShouldNotBeCaused() {
        List<Long> longList = new ArrayList<>();
        usersWebController.deleteUsers(null, null);
        verify(userService, never()).deleteUsersById(longList);
    }

    @Test
    public void ifTheSheetIdOfUsersIsNotNullButTheEmptyMethodOfDeletingUsersShouldNotBeCalled() {
        List<Long> longList = new ArrayList<>();
        Long[] ids = {};
        usersWebController.deleteUsers(ids, null);
        verify(userService, never()).deleteUsersById(longList);

    }

    @Test
    public void ifTheSheetIdOfUsersIsNotNullButAndNotEmptyMethodOfDeletingUsersShouldCalled() {
        Long[] ids = {1L, 3L};
        usersWebController.deleteUsers(ids, redirectAttributes);
        List<Long> longList = new ArrayList<>();
        longList.add(1L);
        longList.add(3L);
        verify(userService, Mockito.times(1)).deleteUsersById(longList);
    }


    @Test
    public void ifTheUserComesWithAnEmptyRoleSendToRedirect() {
        when(bindingResult.hasErrors()).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleDTO(new RoleDTO());
        String result = usersWebController.updateRole(userDTO, bindingResult, redirectAttributes);
        assertEquals("redirect:/private/users/showing", result);
    }


    @Test
    public void ifTheUserComesWithAnEmptyRoleTheRoleChangeMethodShouldNotBeExecuted() {
        when(bindingResult.hasErrors()).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleDTO(new RoleDTO());
        usersWebController.updateRole(userDTO, bindingResult, redirectAttributes);
        verify(userService, never()).updateRole(userDTO);
    }

    @Test
    public void ifAUserComeSWithANonEmptyRoleTheRoleChangeMustBeExecuted() {
        when(bindingResult.hasErrors()).thenReturn(false);
        UserDTO userDTO = new UserDTO();
        RoleDTO roleADMINISTRATOR = new RoleDTO();
        roleADMINISTRATOR.setName("ADMINISTRATOR");
        userDTO.setRoleDTO(roleADMINISTRATOR);
        usersWebController.updateRole(userDTO, bindingResult, redirectAttributes);
        verify(userService, Mockito.times(1)).updateRole(userDTO);
    }

    @Test
    public void ifTheUserComesWithAnEmptyEmailSendToRedirect() {
        when(bindingResult.hasFieldErrors("email")).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        String result = usersWebController.updatePassword(userDTO, bindingResult, redirectAttributes);
        assertEquals("redirect:/private/users/showing", result);
    }

    @Test
    public void ifTheUserComesWithAnEmptyEmailThePasswordChangeMethodShouldNotBeExecuted() {
        when(bindingResult.hasFieldErrors("email")).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@mail.ru");
        usersWebController.updatePassword(userDTO, bindingResult, redirectAttributes);
        verify(userService, never()).updatePasswordByEmail(userDTO.getEmail());
    }

    @Test
    public void ifAUserComeSWithANonEmptyEmailThePasswordChangeMustBeExecuted() {
        when(bindingResult.hasFieldErrors("email")).thenReturn(false);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@mail.ru");
        usersWebController.updatePassword(userDTO, bindingResult, redirectAttributes);
        verify(userService, Mockito.times(1)).updatePasswordByEmail(userDTO.getEmail());
    }

    @Test
    public void ifAnEmptyUserHasArrivedSendToRedirect() {
        when(bindingResult.hasErrors()).thenReturn(false);
        UserDTO userDTO = new UserDTO();
        String result = usersWebController.addUsers(userDTO, bindingResult, redirectAttributes);
        assertEquals("redirect:/private/users/form_add_users", result);
    }

    @Test
    public void ifAnEmptyUserHasComeTheAddMethodShouldNotBeCalled() {
        when(bindingResult.hasErrors()).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        usersWebController.addUsers(userDTO, bindingResult, redirectAttributes);
        verify(userService, never()).add(userDTO);
    }

    @Test
    public void ifTheAddMethodIsNotEmptyTheAddMethodMustBeCalled() {
        when(bindingResult.hasErrors()).thenReturn(false);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@mail.ru");
        RoleDTO roleADMINISTRATOR = new RoleDTO();
        roleADMINISTRATOR.setName("ADMINISTRATOR");
        userDTO.setRoleDTO(roleADMINISTRATOR);
        usersWebController.addUsers(userDTO, bindingResult, redirectAttributes);
        verify(userService, Mockito.times(1)).add(userDTO);
    }
}