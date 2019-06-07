package ru.kazimir.bortnik.online_market.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.controllers.web.admin.AdminUsersWebController;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.validators.RoleValidatorImpl;
import ru.kazimir.bortnik.online_market.validators.AddUserValidatorImpl;
import ru.kazimir.bortnik.online_market.validators.UpdateByEmailPasswordValidatorImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AdminUsersWebControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private AddUserValidatorImpl addUserValidatorImpl;
    @Mock
    private RoleValidatorImpl roleValidatorImpl;
    @Mock
    private UpdateByEmailPasswordValidatorImpl updateByEmailPasswordValidatorImpl;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private RedirectAttributes redirectAttributes;
    @Mock
    private Authentication authentication;

    private MockMvc mockMvc;
    private AdminUsersWebController adminUsersWebController;
    private List<UserDTO> userDTOS = new ArrayList<>();
    private List<RoleDTO> roleDTOS = new ArrayList<>();

    @Before
    public void init() {
        adminUsersWebController = new AdminUsersWebController(userService, roleService, addUserValidatorImpl, roleValidatorImpl, updateByEmailPasswordValidatorImpl);
        mockMvc = MockMvcBuilders.standaloneSetup(adminUsersWebController).build();

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setEmail("Test@mail.ru");
        userDTO1.setSurname("SurnameTest");
        userDTO1.setName("NameTest");
        RoleDTO roleDTO1 = new RoleDTO();
        roleDTO1.setName("ADMINISTRATOR");
        userDTO1.setRoleDTO(roleDTO1);

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setEmail("Test@mail.ru");
        userDTO2.setSurname("SurnameTest");
        userDTO2.setName("NameTest");
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
    public void ifTheIncomingSheetIdIsNullThenTheMethodForDeletionShouldNotBeCaused() {
        List<Long> longList = new ArrayList<>();
        adminUsersWebController.deleteUsers(null, null);
        verify(userService, never()).deleteUsersById(longList);
    }

    @Test
    public void ifTheSheetIdOfUsersIsNotNullButTheEmptyMethodOfDeletingUsersShouldNotBeCalled() {
        List<Long> longList = new ArrayList<>();
        Long[] ids = {};
        adminUsersWebController.deleteUsers(ids, null);
        verify(userService, never()).deleteUsersById(longList);

    }

    @Test
    public void ifTheSheetIdOfUsersIsNotNullButAndNotEmptyMethodOfDeletingUsersShouldCalled() {
        Long[] ids = {1L, 3L};
        adminUsersWebController.deleteUsers(ids, redirectAttributes);
        List<Long> longList = new ArrayList<>();
        longList.add(1L);
        longList.add(3L);
        verify(userService, Mockito.times(1)).deleteUsersById(longList);
    }


    @Test
    public void ifTheUserComesWithAnEmptyRoleTheRoleChangeMethodShouldNotBeExecuted() {
        when(bindingResult.hasErrors()).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleDTO(new RoleDTO());
        adminUsersWebController.updateRole(userDTO, bindingResult, redirectAttributes);
        verify(userService, never()).updateRole(userDTO);
    }

    @Test
    public void ifAUserComeSWithANonEmptyRoleTheRoleChangeMustBeExecuted() {
        when(bindingResult.hasErrors()).thenReturn(false);
        UserDTO userDTO = new UserDTO();
        RoleDTO roleADMINISTRATOR = new RoleDTO();
        roleADMINISTRATOR.setName("ADMINISTRATOR");
        userDTO.setRoleDTO(roleADMINISTRATOR);
        adminUsersWebController.updateRole(userDTO, bindingResult, redirectAttributes);
        verify(userService, Mockito.times(1)).updateRole(userDTO);
    }


    @Test
    public void ifAnEmptyUserHasArrivedSendToRedirect() {
        when(bindingResult.hasErrors()).thenReturn(false);
        UserDTO userDTO = new UserDTO();
        String result = adminUsersWebController.addUsers(userDTO, bindingResult, redirectAttributes);
        assertEquals("redirect:/private/users/form_add_users", result);
    }

    @Test
    public void ifAnEmptyUserHasComeTheAddMethodShouldNotBeCalled() {
        when(bindingResult.hasErrors()).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        adminUsersWebController.addUsers(userDTO, bindingResult, redirectAttributes);
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
        adminUsersWebController.addUsers(userDTO, bindingResult, redirectAttributes);
        verify(userService, Mockito.times(1)).add(userDTO);
    }
}