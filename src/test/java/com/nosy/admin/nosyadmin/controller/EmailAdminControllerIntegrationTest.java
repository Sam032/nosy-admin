package com.nosy.admin.nosyadmin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosy.admin.nosyadmin.model.*;
import com.nosy.admin.nosyadmin.service.EmailFeedService;
import com.nosy.admin.nosyadmin.service.EmailTemplateService;
import com.nosy.admin.nosyadmin.service.InputSystemService;
import com.nosy.admin.nosyadmin.utils.EmailTemplateMapper;
import com.nosy.admin.nosyadmin.utils.InputSystemMapper;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@RunWith(SpringRunner.class)
@WebMvcTest(EmailAdminController.class)
@TestPropertySource(properties = {"keycloak.enabled = false"})
public class EmailAdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private InputSystemService inputSystemService;
    @MockBean
    private EmailTemplateService emailTemplateService;
    @MockBean
    private EmailFeedService emailFeedService;

    private InputSystem inputSystem = new InputSystem();
    private EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
    private EmailTemplate emailTemplate = new EmailTemplate();
    private ReadyEmail readyEmail = new ReadyEmail();

    @Before
    public void beforeTest() {
        inputSystem.setInputSystemId("TestInputSystemId");
        inputSystem.setInputSystemName("TestInputSystemName");

        emailProviderProperties.setUsername("TestEmailProviderPropertiesUsername");
        emailProviderProperties.setPassword("TestEmailProviderPropertiesPassword");
        emailProviderProperties.setPlaceholders(Collections.emptyList());

        emailTemplate.setEmailTemplateId("TestEmailTemplateId");
        emailTemplate.setEmailTemplateName("TestEmailTemplateName");
        emailTemplate.setEmailTemplateSubject("TestEmailTemplateSubject");
        emailTemplate.setEmailTemplateText("TestEmailTemplateText");
        emailTemplate.setEmailTemplateFromAddress("TestEmailTemplateFromAddress");
        emailTemplate.setEmailTemplateTo(Collections.singleton("TestEmailTemplateTo"));
        emailTemplate.setEmailTemplateCc(Collections.singleton("TestEmailTemplateCc"));
        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        emailTemplate.setEmailTemplateRetryTimes(1);
        emailTemplate.setEmailTemplateRetryPeriod(10);
        emailTemplate.setEmailTemplatePriority(1);
        emailTemplate.setInputSystem(inputSystem);

        readyEmail.setEmailTemplate(emailTemplate);
        readyEmail.setEmailProviderProperties(emailProviderProperties);
    }

    @Test
    public void emailTemplatePostTest() throws Exception {
        String json = objectMapper.writeValueAsString(emailProviderProperties);

        when(emailTemplateService.postEmailTemplate(
                anyString(),
                anyString(),
                any(EmailProviderProperties.class),
                anyString()
        )).thenReturn(emailTemplate);

        mockMvc.perform(post("/api/v1/nosy/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}/post", "1", "2")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(emailTemplate.getEmailTemplateId()))
                .andExpect(jsonPath("$.name").value(emailTemplate.getEmailTemplateName()))
                .andExpect(jsonPath("$.fromAddress").value(emailTemplate.getEmailTemplateFromAddress()))
                .andExpect(jsonPath("$.subject").value(emailTemplate.getEmailTemplateSubject()))
                .andExpect(jsonPath("$.fromProvider").value(emailTemplate.getEmailTemplateFromProvider().toString()))
                .andExpect(jsonPath("$.to.length()").value(emailTemplate.getEmailTemplateTo().size()))
                .andExpect(jsonPath("$.cc.length()").value(emailTemplate.getEmailTemplateCc().size()))
                .andExpect(jsonPath("$.text").value(emailTemplate.getEmailTemplateText()))
                .andExpect(jsonPath("$.retryTimes").value(emailTemplate.getEmailTemplateRetryTimes()))
                .andExpect(jsonPath("$.retryPeriod").value(emailTemplate.getEmailTemplateRetryPeriod()))
                .andExpect(jsonPath("$.priority").value(emailTemplate.getEmailTemplatePriority()));
    }

    @Test
    public void emailPostTest() throws Exception {
        String json = objectMapper.writeValueAsString(readyEmail);

        mockMvc.perform(post("/api/v1/nosy/email/post")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void newTypeTest() throws Exception {
        String json = objectMapper.writeValueAsString(InputSystemMapper.INSTANCE.toInputSystemDto(inputSystem));

        when(inputSystemService.saveInputSystem(any(InputSystem.class), anyString())).thenReturn(inputSystem);

        mockMvc.perform(post("/api/v1/nosy/inputsystems")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(inputSystem.getInputSystemId()))
                .andExpect(jsonPath("$.name").value(inputSystem.getInputSystemName()));
    }

    @Test
    public void getInputSystemsTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/inputsystems"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmailAllProvidersTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/inputsystems/emailproviders"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateInputSystemNameTest() throws Exception {
        String json = objectMapper.writeValueAsString(InputSystemMapper.INSTANCE.toInputSystemDto(inputSystem));

        mockMvc.perform(put("/api/v1/nosy/inputsystems/{inputSystemId}", "1")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void newEmailTemplateTest() throws Exception {
        String json = objectMapper.writeValueAsString(EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate));

        mockMvc.perform(post("/api/v1/nosy/inputsystems/{inputSystemId}/emailtemplates", "1")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void getEmailTemplateByInputSystemAndEmailTemplateIdTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}", "1", "2"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateEmailTemplateTest() throws Exception {
        String json = objectMapper.writeValueAsString(EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate));

        mockMvc.perform(put("/api/v1/nosy/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}", "1", "2")
                .with(csrf())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmailTemplatesTest() throws Exception {
        mockMvc.perform(get("/api/v1/nosy/inputsystems/{inputSystemId}/emailtemplates", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmailTemplateTest() throws Exception {
        mockMvc.perform(delete("/api/v1/nosy/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}", "1", "2")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteInputSystemTest() throws Exception {
        mockMvc.perform(delete("/api/v1/nosy/inputsystems/{inputSystemId}", "1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addEmailFeedToEmailTemplateTest() throws Exception {
        mockMvc.perform(put("/api/v1/nosy/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}/emailfeeds/{emailFeedId}", "1", "2", "3")
                .with(csrf()))
                .andExpect(status().isOk());
    }

}
