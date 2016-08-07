package com.techdegree.instateam.web.controller;

import com.techdegree.instateam.model.Role;
import com.techdegree.instateam.service.RoleService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {
    // main mockMvc object
    private MockMvc mockMvc;

    // inject mock into roleService
    @InjectMocks
    private RoleService roleService;

    // controller to insert into mockMvc
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
                .standaloneSetup(
                        roleController
                ).build();
        // initialize test roles
        setUpFirstRole();
        setUpSecondRole();
        // initialize list of roles as database
        setUpListWithTwoRolesAsDatabase();
    }

}