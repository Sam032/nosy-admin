package com.nosy.admin.nosyadmin.controller;

import com.nosy.admin.nosyadmin.dto.EmailFeedDto;
import com.nosy.admin.nosyadmin.dto.EmailTemplateDto;
import com.nosy.admin.nosyadmin.dto.InputSystemDto;
import com.nosy.admin.nosyadmin.model.EmailProviderProperties;
import com.nosy.admin.nosyadmin.model.EmailTemplate;
import com.nosy.admin.nosyadmin.model.ReadyEmail;
import com.nosy.admin.nosyadmin.service.EmailFeedService;
import com.nosy.admin.nosyadmin.service.EmailTemplateService;
import com.nosy.admin.nosyadmin.service.InputSystemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailAdminControllerTest {

    @InjectMocks
    EmailAdminController emailAdminController;

    @Mock
    EmailTemplateService emailTemplateService;

    @Mock
    InputSystemService inputSystemService;

    @Mock
    EmailFeedService emailFeedService;

    @Test
    public void emailTemplatePost() {
        EmailTemplateDto emailTemplateDto=new EmailTemplateDto();
        emailTemplateDto.setSubject("TestSubject");
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                emailTemplatePost("dasda", "dasdas", null,
                        principal).getStatusCode());
        assertEquals("TestSubject",emailTemplateDto.getSubject());
    }

    @Test
    public void emailPost() {
        ReadyEmail readyEmail = new ReadyEmail();
        readyEmail.setEmailTemplate(new EmailTemplate());
        readyEmail.getEmailTemplate().setEmailTemplateSubject("TestSubject");

        assertEquals(HttpStatus.OK, emailAdminController.emailPost(readyEmail).getStatusCode());
    }

    @Test
    public void newType() {
        InputSystemDto inputSystemDto=new InputSystemDto();
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailAdminController.newType(inputSystemDto,principal).getStatusCode());

    }

    @Test
    public void getInputSystems() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.getInputSystems(principal).getStatusCode());
    }

    @Test
    public void getEmailAllProviders() {
        Principal principal=mock(Principal.class);
        when(emailTemplateService.getAllEmailProviders()).thenReturn(null);
        assertEquals(HttpStatus.OK, emailAdminController.getEmailAllProviders(principal).getStatusCode());
    }

    @Test
    public void updateInputSystemName() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.updateInputSystemName("inputSystemId", null, principal).getStatusCode());
    }

    @Test
    public void newEmailTemplate() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailAdminController.
                newEmailTemplate(principal, "inputSystemId",null).getStatusCode());
    }

    @Test
    public void getEmailTemplateByInputSystemAndEmailTemplateId() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailTemplateByInputSystemAndEmailTemplateId("emailTemplateId","inputSystemId",principal).getStatusCode());
    }

    @Test
    public void updateEmailTemplate() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                updateEmailTemplate( "inputSystemId","emailTemplateId", null, principal)
                .getStatusCode());
    }


    @Test
    public void getEmailTemplates() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailTemplates("inputSystemId", principal).getStatusCode());
    }

    @Test
    public void deleteEmailTemplate() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteEmailTemplate("inputSystemId", "emailTemplateId",principal).getStatusCode());
    }

    @Test
    public void deleteInputSystem() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteInputSystem("inputSystemId", principal).getStatusCode());
    }

    @Test
    public void newEmailFeed() {
        EmailFeedDto emailFeedDto = new EmailFeedDto();
        emailFeedDto.setId("emailFeedId");
        emailFeedDto.setName("emailFeedName");
        Principal principal = mock(Principal.class);

        assertEquals(HttpStatus.CREATED, emailAdminController.
                newEmailFeed("inputSystemId", emailFeedDto, principal).getStatusCode());
    }

    @Test
    public void updateEmailFeedTest() {
        EmailFeedDto emailFeedDto = new EmailFeedDto();
        Principal principal = mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController
                .updateEmailFeed("inputSystemId", "emailFeedId", emailFeedDto, principal).getStatusCode());
    }

    @Test
    public void deleteEmailFeed() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteEmailFeed("inputSystemId", "emailFeedId", principal).getStatusCode());
    }

    @Test
    public void subscribeToEmailFeed() {
        Principal principal = mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                subscribeToEmailFeed("inputSystemId", "emailFeedId", principal).getStatusCode());
    }

    @Test
    public void unsubscribeToEmailFeed() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                unsubscribeToEmailFeed("inputSystemId", "emailFeedId", principal).getStatusCode());
    }

    @Test
    public void addEmailFeedToEmailTemplate() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                addEmailFeedToEmailTemplate("inputSystemId", "emailTemplateId", "emailFeedId", principal).getStatusCode());
    }

    @Test
    public void getEmailFeeds() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailFeeds("inputSystemId", principal).getStatusCode());
    }

    @Test
    public void getEmailFeedByInputSystemIdAndEmailFeedId() {
        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailFeedByInputSystemIdAndEmailFeedId("inputSystemId", "emailFeedId").getStatusCode());
    }

    @Test
    public void postEmailFeed() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                postEmailFeed("inputSystemId", "emailFeedId", new EmailProviderProperties(), principal).getStatusCode());
    }

}


