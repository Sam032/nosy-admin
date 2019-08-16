package com.nosy.admin.nosyadmin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosy.admin.nosyadmin.dto.EmailCollectionDto;
import com.nosy.admin.nosyadmin.service.EmailCollectionService;
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

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@RunWith(SpringRunner.class)
@WebMvcTest(EmailCollectionController.class)
@TestPropertySource(properties = {"keycloak.enabled = false"})
public class EmailCollectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmailCollectionService emailCollectionService;

    private EmailCollectionDto emailCollectionDto = new EmailCollectionDto();

    @Before
    public void beforeTest() {
        emailCollectionDto.setId("TestEmailCollectionId");
        emailCollectionDto.setName("TestEmailCollectionName");
        emailCollectionDto.setEmails(Collections.singletonList("TestEmailCollectionEmail"));
    }

    @Test
    public void uploadEmailCollectionTest() throws Exception {
        String json = objectMapper.writeValueAsString(emailCollectionDto);

        mockMvc.perform(post("/api/v1/nosy/emailgroups")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void replaceEmailCollectionTest() throws Exception {
        String json = objectMapper.writeValueAsString(emailCollectionDto);

        mockMvc.perform(put("/api/v1/nosy/emailgroups")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addToEmailCollectionTest() throws Exception {
        String json = objectMapper.writeValueAsString(emailCollectionDto);

        mockMvc.perform(patch("/api/v1/nosy/emailgroups")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createEmailCollectionTest() throws Exception {
        String json = objectMapper.writeValueAsString(emailCollectionDto);

        mockMvc.perform(post("/api/v1/nosy/emailgroups/list")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void getEmailCollectionByIdTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/emailgroups/{emailCollectionId}", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllEmailCollectionsTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/emailgroups"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmailCollectionByIdTest() throws Exception {
        mockMvc.perform(delete("/api/v1/nosy/emailgroups/{emailCollectionId}", "1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

}
