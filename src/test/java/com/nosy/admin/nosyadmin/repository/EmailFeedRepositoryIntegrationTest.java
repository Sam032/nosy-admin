package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.EmailFeed;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EmailFeedRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmailFeedRepository emailFeedRepository;

    private EmailFeed emailFeed;
    private InputSystem inputSystem;

    @Before
    public void beforeTest() {
        inputSystem = new InputSystem();
        inputSystem.setInputSystemName("inputSystemName");

        emailFeed = new EmailFeed();
        emailFeed.setEmailFeedName("emailFeedName");
        emailFeed.setEmailFeedAddress("emailFeedAddress");
        emailFeed.setEmailFeedSubscribers(Collections.singleton("emailFeed@Subscriber.com"));
        emailFeed.setInputSystem(inputSystem);
    }

    @Test
    public void whenFindEmailFeedByEmailFeedNameAndInputSystemId_thenReturnEmailFeed() {
        inputSystem = entityManager.persist(inputSystem);
        emailFeed = entityManager.persist(emailFeed);
        entityManager.flush();

        EmailFeed found = emailFeedRepository.findEmailFeedByEmailFeedNameAndInputSystemId(emailFeed.getEmailFeedName(), inputSystem.getInputSystemId());

        assertThat(found.getEmailFeedName()).isEqualTo(emailFeed.getEmailFeedName());
        assertThat(found.getEmailFeedAddress()).isEqualTo(emailFeed.getEmailFeedAddress());
        assertThat(found.getEmailFeedSubscribers()).isEqualTo(emailFeed.getEmailFeedSubscribers());
        assertThat(found.getInputSystem()).isEqualTo(emailFeed.getInputSystem());
    }

    @Test
    public void whenFindEmailFeedByEmailFeedIdAndInputSystemId_thenReturnEmailFeed() {
        inputSystem = entityManager.persist(inputSystem);
        emailFeed = entityManager.persist(emailFeed);
        entityManager.flush();

        EmailFeed found = emailFeedRepository.findEmailFeedByEmailFeedIdAndInputSystemId(emailFeed.getEmailFeedId(), inputSystem.getInputSystemId());

        assertThat(found.getEmailFeedName()).isEqualTo(emailFeed.getEmailFeedName());
        assertThat(found.getEmailFeedAddress()).isEqualTo(emailFeed.getEmailFeedAddress());
        assertThat(found.getEmailFeedSubscribers()).isEqualTo(emailFeed.getEmailFeedSubscribers());
        assertThat(found.getInputSystem()).isEqualTo(emailFeed.getInputSystem());
    }

    @Test
    public void whenFindEmailFeedsByInputSystemId_thenReturnEmailFeedList() {
        inputSystem = entityManager.persist(inputSystem);
        emailFeed = entityManager.persist(emailFeed);
        entityManager.flush();

        List<EmailFeed> foundList = emailFeedRepository.findEmailFeedsByInputSystemId(inputSystem.getInputSystemId());

        assertThat(foundList.size()).isEqualTo(1);
        assertThat(foundList.get(0).getEmailFeedName()).isEqualTo(emailFeed.getEmailFeedName());
        assertThat(foundList.get(0).getEmailFeedAddress()).isEqualTo(emailFeed.getEmailFeedAddress());
        assertThat(foundList.get(0).getEmailFeedSubscribers()).isEqualTo(emailFeed.getEmailFeedSubscribers());
        assertThat(foundList.get(0).getInputSystem()).isEqualTo(emailFeed.getInputSystem());
    }

}
