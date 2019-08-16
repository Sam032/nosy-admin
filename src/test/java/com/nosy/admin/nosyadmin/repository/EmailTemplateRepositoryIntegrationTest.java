package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.EmailFromProvider;
import com.nosy.admin.nosyadmin.model.EmailTemplate;
import com.nosy.admin.nosyadmin.model.InputSystem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EmailTemplateRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    private EmailTemplate emailTemplate;
    private InputSystem inputSystem;

    @Before
    public void beforeTest() {
        inputSystem = new InputSystem();
        inputSystem.setInputSystemName("inputSystemName");

        emailTemplate = new EmailTemplate();
        emailTemplate.setEmailTemplateName("emailTemplateName");
        emailTemplate.setEmailTemplateFromAddress("emailTemplate@From.com");
        emailTemplate.setEmailTemplateText("emailTemplateText");
        emailTemplate.setEmailTemplateSubject("emailTemplateSubject");
        emailTemplate.setEmailTemplateTo(Collections.singleton("emailTemplate@To.com"));
        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        emailTemplate.setInputSystem(inputSystem);
    }

    @Test
    public void whenFindEmailTemplateByEmailTemplateNameAndInputSystemId_thenReturnEmailTemplate() {
        inputSystem = entityManager.persist(inputSystem);
        emailTemplate = entityManager.persist(emailTemplate);
        entityManager.flush();

        EmailTemplate found = emailTemplateRepository.findEmailTemplateByEmailTemplateNameAndInputSystemId(emailTemplate.getEmailTemplateName(), inputSystem.getInputSystemId());

        assertThat(found.getEmailTemplateName()).isEqualTo(emailTemplate.getEmailTemplateName());
        assertThat(found.getEmailTemplateFromAddress()).isEqualTo(emailTemplate.getEmailTemplateFromAddress());
        assertThat(found.getEmailTemplateText()).isEqualTo(emailTemplate.getEmailTemplateText());
        assertThat(found.getInputSystem()).isEqualTo(emailTemplate.getInputSystem());
    }

    @Test
    public void whenFindEmailTemplatesByInputSystemId_thenReturnEmailTemplateList() {
        inputSystem = entityManager.persist(inputSystem);
        emailTemplate = entityManager.persist(emailTemplate);
        entityManager.flush();

        List<EmailTemplate> foundList = emailTemplateRepository.findEmailTemplatesByInputSystemId(inputSystem.getInputSystemId());

        assertThat(foundList.size()).isEqualTo(1);
        assertThat(foundList.get(0).getEmailTemplateName()).isEqualTo(emailTemplate.getEmailTemplateName());
        assertThat(foundList.get(0).getEmailTemplateFromAddress()).isEqualTo(emailTemplate.getEmailTemplateFromAddress());
        assertThat(foundList.get(0).getEmailTemplateText()).isEqualTo(emailTemplate.getEmailTemplateText());
        assertThat(foundList.get(0).getInputSystem()).isEqualTo(emailTemplate.getInputSystem());
    }

    @Test
    public void whenFindEmailTemplatesByInputSystemIdAndEmailTemplateId_thenReturnEmailTemplate() {
        inputSystem = entityManager.persist(inputSystem);
        emailTemplate = entityManager.persist(emailTemplate);
        entityManager.flush();

        EmailTemplate found = emailTemplateRepository.findEmailTemplatesByInputSystemIdAndEmailTemplateId(inputSystem.getInputSystemId(), emailTemplate.getEmailTemplateId());

        assertThat(found.getEmailTemplateName()).isEqualTo(emailTemplate.getEmailTemplateName());
        assertThat(found.getEmailTemplateFromAddress()).isEqualTo(emailTemplate.getEmailTemplateFromAddress());
        assertThat(found.getEmailTemplateText()).isEqualTo(emailTemplate.getEmailTemplateText());
        assertThat(found.getInputSystem()).isEqualTo(emailTemplate.getInputSystem());
    }

}
