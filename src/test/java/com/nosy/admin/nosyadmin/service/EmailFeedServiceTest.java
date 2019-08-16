package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.exceptions.*;
import com.nosy.admin.nosyadmin.model.EmailFeed;
import com.nosy.admin.nosyadmin.model.EmailProviderProperties;
import com.nosy.admin.nosyadmin.model.InputSystem;
import com.nosy.admin.nosyadmin.model.User;
import com.nosy.admin.nosyadmin.repository.EmailFeedRepository;
import com.nosy.admin.nosyadmin.repository.InputSystemRepository;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.constraints.Email;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailFeedServiceTest {

    @InjectMocks
    private EmailFeedService emailFeedServiceMock;

    @Mock
    private EmailFeedRepository emailFeedRepository;

    @Mock
    private InputSystemRepository inputSystemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SenderService senderService;

    private EmailFeed emailFeed;
    private InputSystem inputSystem;
    private User user;

    private String email = "test@nosy.tech";
    private String emailFeedId = "emailFeedId";
    private String inputSystemId = "inputSystemId";

    @Before
    public void beforeEmailFeed() {
        user = new User();
        user.setEmail(email);
        user.setFirstName("Test");
        user.setLastName("Nosy");
        user.setPassword("dajsndjasn");

        inputSystem = new InputSystem();
        inputSystem.setInputSystemName("testInputSystem");
        inputSystem.setInputSystemId(inputSystemId);
        inputSystem.setUser(user);

        emailFeed = new EmailFeed();
        emailFeed.setEmailFeedId(emailFeedId);
        emailFeed.setEmailFeedName("testEmailFeed");
        emailFeed.setInputSystem(inputSystem);
        emailFeed.setEmailFeedSubscribers(new HashSet<>());
    }

    @Test
    public void newEmailFeed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.newEmailFeed(inputSystemId, emailFeed, email).getEmailFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void newEmailFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        assertEquals(emailFeedId, emailFeedServiceMock.newEmailFeed(inputSystemId, emailFeed, email).getEmailFeedId());
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void newEmailFeedInputSystemNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        assertEquals(emailFeedId, emailFeedServiceMock.newEmailFeed(inputSystemId, emailFeed, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedExistException.class)
    public void newEmailFeedEmailFeedExists() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedNameAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.newEmailFeed(inputSystemId, emailFeed, email).getEmailFeedId());
    }

    @Test
    public void updateEmailFeedTest() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.updateEmailFeed(inputSystemId, emailFeedId, emailFeed, email).getEmailFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void updateEmailFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        assertEquals(emailFeedId, emailFeedServiceMock.updateEmailFeed(inputSystemId, emailFeedId, emailFeed, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedNotFoundException.class)
    public void updateEmailFeedNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        assertEquals(emailFeedId, emailFeedServiceMock.updateEmailFeed(inputSystemId, emailFeedId,null, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedNotFoundException.class)
    public void updateEmailFeedNull() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.updateEmailFeed(inputSystemId, emailFeedId,null, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedNameInvalidException.class)
    public void updateEmailFeedNameInvalid() {
        EmailFeed emailFeedInvalid = new EmailFeed();
        emailFeedInvalid.setEmailFeedName("EmailFeedName");

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedNameAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.updateEmailFeed(inputSystemId, emailFeedId, emailFeedInvalid, email).getEmailFeedId());
    }

    @Test
    public void getListOfEmailFeeds() {
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(Collections.singletonList(emailFeed)).when(emailFeedRepository).findEmailFeedsByInputSystemId(anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.getListOfEmailFeeds(inputSystemId, email).get(0).getEmailFeedId());
    }

    @Test
    public void deleteEmailFeed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        emailFeedServiceMock.deleteEmailFeed(inputSystemId, emailFeedId, email);
    }

    @Test(expected = NotAuthenticatedException.class)
    public void deleteEmailFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        emailFeedServiceMock.deleteEmailFeed(inputSystemId, emailFeedId, email);
    }

    @Test
    public void subscribeToEmailFeed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.subscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void subscribeToEmailFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        assertEquals(emailFeedId, emailFeedServiceMock.subscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void subscribeToEmailFeedInputSystemNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        assertEquals(emailFeedId, emailFeedServiceMock.subscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedNotFoundException.class)
    public void subscribeToEmailFeedEmailFeedNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.subscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedAlreadySubscribedException.class)
    public void subscribeToEmailFeedEmailFeedAlreadySubscribed() {
        emailFeed.getEmailFeedSubscribers().add(email);

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.subscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test
    public void unsubscribeToEmailFeed() {
        emailFeed.getEmailFeedSubscribers().add(email);

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.unsubscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void unsubscribeToEmailFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        assertEquals(emailFeedId, emailFeedServiceMock.unsubscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void unsubscribeToEmailFeedInputSystemNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        assertEquals(emailFeedId, emailFeedServiceMock.unsubscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedNotFoundException.class)
    public void unsubscribeToEmailFeedEmailFeedNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.unsubscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test(expected = EmailFeedNotSubscribedException.class)
    public void unsubscribeToEmailFeedEmailFeedNotSubscribed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.unsubscribeToEmailFeed(inputSystemId, emailFeedId, email).getEmailFeedId());
    }

    @Test
    public void postEmailFeed() {
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setUsername("EmailProviderUsername");
        emailProviderProperties.setPassword("EmailProviderPassword");
        emailProviderProperties.setPlaceholders(Collections.emptyList());

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByIdAndEmail(anyString(), anyString());
        doReturn(emailFeed).when(emailFeedRepository).findEmailFeedByEmailFeedIdAndInputSystemId(anyString(), anyString());
        assertEquals(emailFeedId, emailFeedServiceMock.postEmailFeed(inputSystemId, emailFeedId, emailProviderProperties, email).getEmailFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void postEmailFeedNotAuthenticated() {
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setUsername("EmailProviderUsername");
        emailProviderProperties.setPassword("EmailProviderPassword");
        emailProviderProperties.setPlaceholders(Collections.emptyList());

        when(userRepository.findById(email)).thenReturn(Optional.empty());
        assertEquals(emailFeedId, emailFeedServiceMock.postEmailFeed(inputSystemId, emailFeedId, emailProviderProperties, email).getEmailFeedId());
    }

}
