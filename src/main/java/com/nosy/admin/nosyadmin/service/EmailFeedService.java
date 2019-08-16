package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.exceptions.*;
import com.nosy.admin.nosyadmin.model.*;
import com.nosy.admin.nosyadmin.repository.EmailFeedRepository;
import com.nosy.admin.nosyadmin.repository.InputSystemRepository;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EmailFeedService {

    private EmailFeedRepository emailFeedRepository;
    private InputSystemRepository inputSystemRepository;
    private UserRepository userRepository;
    private SenderService senderService;

    @Autowired
    public EmailFeedService(
            EmailFeedRepository emailFeedRepository,
            InputSystemRepository inputSystemRepository,
            UserRepository userRepository,
            SenderService senderService
    ) {
        this.emailFeedRepository = emailFeedRepository;
        this.inputSystemRepository = inputSystemRepository;
        this.userRepository = userRepository;
        this.senderService = senderService;
    }

    public EmailFeed newEmailFeed(String inputSystemId, EmailFeed emailFeed, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        InputSystem inputSystem = getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        if (emailFeedRepository.findEmailFeedByEmailFeedNameAndInputSystemId(emailFeed.getEmailFeedName(), inputSystemId) != null) {
            throw new EmailFeedExistException();
        }

        emailFeed.setEmailFeedAddress(email);
        emailFeed.setInputSystem(inputSystem);
        emailFeedRepository.save(emailFeed);

        return emailFeed;
    }

    public List<EmailFeed> getListOfEmailFeeds(String inputSystemId, String email) {
        getInputSystemByEmailAndInputSystemId(email, inputSystemId);
        return emailFeedRepository.findEmailFeedsByInputSystemId(inputSystemId);
    }

    public EmailFeed getEmailFeedByInputSystemIdAndEmailFeedId(String inputSystemId, String emailFeedId) {
        EmailFeed emailFeed = emailFeedRepository.findEmailFeedByEmailFeedIdAndInputSystemId(emailFeedId, inputSystemId);
        if (emailFeed == null) {
            throw new EmailFeedNotFoundException();
        }
        return emailFeed;
    }

    public EmailFeed updateEmailFeed(String inputSystemId, String emailFeedId, EmailFeed emailFeed, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        EmailFeed currentEmailFeed = getEmailFeedByInputSystemIdAndEmailFeedId(inputSystemId, emailFeedId);

        if (emailFeed == null) {
            throw new EmailFeedNotFoundException();
        }

        if (
            emailFeed.getEmailFeedName() == null || emailFeed.getEmailFeedName().isEmpty() ||
            (!currentEmailFeed.getEmailFeedName().equals(emailFeed.getEmailFeedName()) &&
            emailFeedRepository.findEmailFeedByEmailFeedNameAndInputSystemId(emailFeed.getEmailFeedName(), inputSystemId) != null)
        ) {
            throw new EmailFeedNameInvalidException();
        }

        currentEmailFeed.setEmailFeedName(emailFeed.getEmailFeedName());
        currentEmailFeed.setEmailFeedAddress(emailFeed.getEmailFeedAddress());
        currentEmailFeed.setEmailFeedSubscribers(emailFeed.getEmailFeedSubscribers());
        currentEmailFeed.setEmailTemplate(emailFeed.getEmailTemplate());

        emailFeedRepository.save(currentEmailFeed);

        return currentEmailFeed;
    }

    public void deleteEmailFeed(String inputSystemId, String emailFeedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        EmailFeed emailFeed = getEmailFeedByInputSystemIdAndEmailFeedId(inputSystemId, emailFeedId);
        emailFeedRepository.deleteById(emailFeed.getEmailFeedId());
    }

    public EmailFeed subscribeToEmailFeed(String inputSystemId, String emailFeedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        EmailFeed emailFeed = getEmailFeedByInputSystemIdAndEmailFeedId(inputSystemId, emailFeedId);
        if (emailFeed.getEmailFeedSubscribers().contains(email)) {
            throw new EmailFeedAlreadySubscribedException();
        }

        emailFeed.getEmailFeedSubscribers().add(email);
        emailFeedRepository.save(emailFeed);

        return emailFeed;
    }

    public EmailFeed unsubscribeToEmailFeed(String inputSystemId, String emailFeedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        EmailFeed emailFeed = getEmailFeedByInputSystemIdAndEmailFeedId(inputSystemId, emailFeedId);

        if (!emailFeed.getEmailFeedSubscribers().contains(email)) {
            throw new EmailFeedNotSubscribedException();
        }

        emailFeed.getEmailFeedSubscribers().remove(email);
        emailFeedRepository.save(emailFeed);

        return emailFeed;
    }

    public EmailFeed postEmailFeed(String inputSystemId, String emailFeedId, EmailProviderProperties emailProviderProperties, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        EmailFeed emailFeed = getEmailFeedByInputSystemIdAndEmailFeedId(inputSystemId, emailFeedId);

        EmailTemplate emailTemplate = emailFeed.getEmailTemplate();
        if(emailTemplate == null) {
            emailFeed.setEmailTemplate(createEmailTemplateFromEmailFeed(emailFeed));
        }

        ReadyEmail readyEmail = createReadyEmailFromEmailFeed(emailFeed, emailProviderProperties);

        senderService.sendReadyEmail(readyEmail);

        return emailFeed;
    }

    private ReadyEmail createReadyEmailFromEmailFeed(EmailFeed emailFeed, EmailProviderProperties emailProviderProperties) {
        EmailTemplate emailTemplate = emailFeed.getEmailTemplate();
        emailTemplate.setEmailTemplateTo(emailFeed.getEmailFeedSubscribers());
        emailTemplate.setInputSystem(emailFeed.getInputSystem());

        ReadyEmail readyEmail = new ReadyEmail();
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        readyEmail.setEmailTemplate(emailTemplate);

        return readyEmail;
    }

    private EmailTemplate createEmailTemplateFromEmailFeed(EmailFeed emailFeed) {
        EmailTemplate emailTemplate = new EmailTemplate();

        emailTemplate.setEmailTemplateName(emailFeed.getEmailFeedName());
        emailTemplate.setEmailTemplateSubject(emailFeed.getEmailFeedName());
        emailTemplate.setEmailTemplateText(emailFeed.getEmailFeedName());
        emailTemplate.setEmailTemplateFromAddress(emailFeed.getEmailFeedAddress());
        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        emailTemplate.setEmailTemplateTo(emailFeed.getEmailFeedSubscribers());
        emailTemplate.setEmailFeeds(Collections.singleton(emailFeed));
        emailTemplate.setInputSystem(emailFeed.getInputSystem());

        return emailTemplate;
    }

    private InputSystem getInputSystemByEmailAndInputSystemId(String email, String inputSystemId) {
        InputSystem inputSystem = inputSystemRepository.findByIdAndEmail(email, inputSystemId);
        if (inputSystem == null) {
            throw new InputSystemNotFoundException();
        }
        return inputSystem;
    }

}
