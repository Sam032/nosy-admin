package com.nosy.admin.nosyadmin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosy.admin.nosyadmin.dto.TokenCollectionDto;
import com.nosy.admin.nosyadmin.dto.UserDto;
import com.nosy.admin.nosyadmin.service.KeycloakService;
import com.nosy.admin.nosyadmin.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@TestPropertySource(properties = {"keycloak.enabled = false"})
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private KeycloakService keycloakService;

    private UserDto userDto = new UserDto();
    private TokenCollectionDto tokenCollectionDto = new TokenCollectionDto();

    @Before
    public void beforeTest() {
        userDto.setEmail("TestUserEmail");
        userDto.setPassword("TestUserPassword");
        userDto.setFirstName("TestUserFirstName");
        userDto.setLastName("TestUserLastName");

        tokenCollectionDto.setAccessToken("TestTokenCollectionToken");
        tokenCollectionDto.setRefreshToken("TestTokenCollectionRefreshToken");
        tokenCollectionDto.setExpiresIn(10);
    }

    @Test
    public void logoutTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/auth/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void isAuthenticatedTest() throws Exception {
        String json = objectMapper.writeValueAsString(tokenCollectionDto);

        mockMvc.perform(post("/api/v1/nosy/auth/status")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getTokenTest() throws Exception {
        String json = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/v1/nosy/auth/token")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void newUserTest() throws Exception {
        String json = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/v1/nosy/users")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteUsernameTest() throws Exception {
        mockMvc.perform(delete("/api/v1/nosy/users")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getUserProfileTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/users"))
                .andExpect(status().isOk());
    }

}
