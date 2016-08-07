package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {
    // main mockMvc object
    private MockMvc mockMvc;

    // mock role service
    @Mock
    private RoleService roleService;

    // inject mocks into controller, that is build with mockMvc
    @InjectMocks
    private RoleController roleController;

    // first role member used in tests
    private Role mFirstRole;
    // second role member used in tests
    private Role mSecondRole;
    // List with roles that will serve us as a db with
    // roles
    private List<Role> mListOfRolesAsDatabase;

    // set up first role
    private void setUpFirstRole() {
        mFirstRole = new Role();
        mFirstRole.setId(1);
        mFirstRole.setName("Role 1");
        // no collaborators just yet
    }
    // set up second role
    private void setUpSecondRole() {
        mSecondRole = new Role();
        mSecondRole.setId(2);
        mSecondRole.setName("Role 2");
        // no collaborators just yet
    }

    // set up list of roles that will serve us as database
    private void setUpListWithTwoRolesAsDatabase() {
        mListOfRolesAsDatabase = Arrays.asList(
                mFirstRole, mSecondRole
        );
    }


    @Before
    public void setUp() throws Exception {
        // set up mock mvc with role controller
        mockMvc = MockMvcBuilders
                .standaloneSetup(roleController)
                .build();
        // initialize test roles
        setUpFirstRole();
        setUpSecondRole();
        // initialize list of roles as database
        setUpListWithTwoRolesAsDatabase();
    }

    @Test
    public void rolesPage_shouldReturnPageWithListOfRoles()
            throws Exception {
        // Arrange Mock objects

        // When roleService will be called, list of roles as
        // database will be returned
        when(roleService.findAll()).thenReturn(
                mListOfRolesAsDatabase
        );

        // Then when get request to "/roles" is made,
        // - OK status should be returned
        // - thymeleaf template "role/roles" should be rendered
        // - model should have attribute "roles" with our test database
        // - model should have attribute "newRole" with `new Role()`
        //   object that is used to be filled with empty data, upon
        //   successfull post request later on.
        // - and findAll() method should be called in service
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(view().name("role/roles"))
                .andExpect(
                        model().attribute("roles", mListOfRolesAsDatabase))
                .andExpect(
                        model().attribute("newRole", new Role())
        );
        verify(roleService).findAll();
    }

    // detail role page is rendered properly

    @Test
    public void detailedRolePageIsRenderedProperly()
            throws Exception {
        // Arrange variables for mockMvc
        int firstRoleId = mFirstRole.getId();

        // When get request is made to roles details page
        // and we use service to return first role found by id
        when(roleService.findById(firstRoleId))
                .thenReturn(mFirstRole);

        // Then:
        // - status should be OK
        // - "role/role-details" template should be rendered
        // - model should contain attribute "role" with firstRole
        // - findById method should be called
        mockMvc.perform(
                get("/roles/" + firstRoleId))
                .andExpect(status().isOk())
                .andExpect(
                        view().name("role/role-details"))
                .andExpect(model().attribute("role", mFirstRole)
                );
        verify(roleService).findById(firstRoleId);
    }
}