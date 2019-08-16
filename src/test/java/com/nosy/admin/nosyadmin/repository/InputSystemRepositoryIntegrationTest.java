package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.InputSystem;
import com.nosy.admin.nosyadmin.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class InputSystemRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InputSystemRepository inputSystemRepository;

    private User user;
    private InputSystem inputSystem;

    @Before
    public void beforeTest() {
        user = new User();
        user.setEmail("user@email.com");
        user.setPassword("userPassword");
        user.setFirstName("userFirstName");
        user.setLastName("userLastName");

        inputSystem = new InputSystem();
        inputSystem.setInputSystemName("inputSystemName");
        inputSystem.setUser(user);
    }

    @Test
    public void whenFindByInputSystemNameAndEmail_thenReturnInputSystem() {
        user = entityManager.persist(user);
        inputSystem = entityManager.persist(inputSystem);
        entityManager.flush();

        InputSystem found = inputSystemRepository.findByInputSystemNameAndEmail(user.getEmail(), inputSystem.getInputSystemName());

        assertThat(found.getInputSystemId()).isEqualTo(inputSystem.getInputSystemId());
        assertThat(found.getInputSystemName()).isEqualTo(inputSystem.getInputSystemName());
        assertThat(found.getUser().getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getUser().getPassword()).isEqualTo(user.getPassword());
        assertThat(found.getUser().getFirstName()).isEqualTo(user.getFirstName());
        assertThat(found.getUser().getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    public void whenFindByIdAndEmail_thenReturnInputSystem() {
        user = entityManager.persist(user);
        inputSystem = entityManager.persist(inputSystem);
        entityManager.flush();

        InputSystem found = inputSystemRepository.findByIdAndEmail(user.getEmail(), inputSystem.getInputSystemId());

        assertThat(found.getInputSystemId()).isEqualTo(inputSystem.getInputSystemId());
        assertThat(found.getInputSystemName()).isEqualTo(inputSystem.getInputSystemName());
        assertThat(found.getUser().getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getUser().getPassword()).isEqualTo(user.getPassword());
        assertThat(found.getUser().getFirstName()).isEqualTo(user.getFirstName());
        assertThat(found.getUser().getLastName()).isEqualTo(user.getLastName());
    }

}
