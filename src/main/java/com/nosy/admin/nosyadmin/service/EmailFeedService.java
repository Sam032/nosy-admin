package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.exceptions.*;
import com.nosy.admin.nosyadmin.model.EmailFeed;
import com.nosy.admin.nosyadmin.model.InputSystem;
import com.nosy.admin.nosyadmin.repository.EmailFeedRepository;
import com.nosy.admin.nosyadmin.repository.InputSystemRepository;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailFeedService {

    private EmailFeedRepository emailFeedRepository;
    private InputSystemRepository inputSystemRepository;
    private UserRepository userRepository;

    @Autowired
    public EmailFeedService(
            EmailFeedRepository emailFeedRepository,
            InputSystemRepository inputSystemRepository,
            UserRepository userRepository
    ) {
        this.emailFeedRepository = emailFeedRepository;
        this.inputSystemRepository = inputSystemRepository;
        this.userRepository = userRepository;
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

    public void deleteEmailFeed(String inputSystemId, String emailFeedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        EmailFeed emailFeed = getEmailFeedByEmailFeedIdAndInputSystemId(emailFeedId, inputSystemId);
        emailFeedRepository.deleteById(emailFeed.getEmailFeedId());
    }

    public EmailFeed subscribeToEmailFeed(String inputSystemId, String emailFeedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        EmailFeed emailFeed = getEmailFeedByEmailFeedIdAndInputSystemId(emailFeedId, inputSystemId);
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

        EmailFeed emailFeed = getEmailFeedByEmailFeedIdAndInputSystemId(emailFeedId, inputSystemId);

        if (!emailFeed.getEmailFeedSubscribers().contains(email)) {
            throw new EmailFeedNotSubscribedException();
        }

        emailFeed.getEmailFeedSubscribers().remove(email);
        emailFeedRepository.save(emailFeed);

        return emailFeed;
    }

    private EmailFeed getEmailFeedByEmailFeedIdAndInputSystemId(String emailFeedId, String inputSystemId) {
        EmailFeed emailFeed = emailFeedRepository.findEmailFeedByEmailFeedIdAndInputSystemId(emailFeedId, inputSystemId);
        if (emailFeed == null) {
            throw new EmailFeedNotFoundException();
        }
        return emailFeed;
    }

    private InputSystem getInputSystemByEmailAndInputSystemId(String email, String inputSystemId) {
        InputSystem inputSystem = inputSystemRepository.findByIdAndEmail(email, inputSystemId);
        if (inputSystem == null) {
            throw new InputSystemNotFoundException();
        }
        return inputSystem;
    }

}
