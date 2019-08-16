package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.EmailCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EmailCollectionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmailCollectionRepository emailCollectionRepository;

    private EmailCollection emailCollection;

    @Before
    public void beforeTest() {
        emailCollection = new EmailCollection();
        emailCollection.setEmailCollectionName("emailCollectionName");
    }

    @Test
    public void whenFindByEmailCollectionName_thenReturnEmailCollection() {
        emailCollection = entityManager.persist(emailCollection);
        entityManager.flush();

        EmailCollection found = emailCollectionRepository.findByEmailCollectionName(emailCollection.getEmailCollectionName());

        assertThat(found.getEmailCollectionId()).isEqualTo(emailCollection.getEmailCollectionId());
        assertThat(found.getEmailCollectionName()).isEqualTo(emailCollection.getEmailCollectionName());
    }

}
