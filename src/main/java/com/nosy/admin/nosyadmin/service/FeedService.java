package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.exceptions.*;
import com.nosy.admin.nosyadmin.model.*;
import com.nosy.admin.nosyadmin.repository.FeedRepository;
import com.nosy.admin.nosyadmin.repository.InputSystemRepository;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FeedService {

    private FeedRepository feedRepository;
    private InputSystemRepository inputSystemRepository;
    private UserRepository userRepository;
    private SenderService senderService;

    @Value("${default.nosy.from.address}")
    private String defaultNosyFromAddress;

    @Autowired
    public FeedService(
            FeedRepository feedRepository,
            InputSystemRepository inputSystemRepository,
            UserRepository userRepository,
            SenderService senderService
    ) {
        this.feedRepository = feedRepository;
        this.inputSystemRepository = inputSystemRepository;
        this.userRepository = userRepository;
        this.senderService = senderService;
    }

    public Feed newFeed(String inputSystemId, Feed feed, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        InputSystem inputSystem = getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        if (feedRepository.findFeedByFeedNameAndInputSystemId(feed.getFeedName(), inputSystemId) != null) {
            throw new FeedExistException();
        }

        feed.setInputSystem(inputSystem);
        feedRepository.save(feed);

        return feed;
    }

    public List<Feed> getListOfFeeds(String inputSystemId, String email) {
        getInputSystemByEmailAndInputSystemId(email, inputSystemId);
        return feedRepository.findFeedsByInputSystemId(inputSystemId);
    }

    public Feed getFeedByInputSystemIdAndFeedId(String inputSystemId, String feedId) {
        Feed feed = feedRepository.findFeedByIdAndInputSystemId(feedId, inputSystemId);
        if (feed == null) {
            throw new FeedNotFoundException();
        }
        return feed;
    }

    public Feed updateFeed(String inputSystemId, String feedId, Feed feed, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        Feed currentFeed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);

        if (feed == null) {
            throw new FeedNotFoundException();
        }

        if (
            feed.getFeedName() == null || feed.getFeedName().isEmpty() ||
            (!currentFeed.getFeedName().equals(feed.getFeedName()) &&
            feedRepository.findFeedByFeedNameAndInputSystemId(feed.getFeedName(), inputSystemId) != null)
        ) {
            throw new FeedNameInvalidException();
        }

        currentFeed.setFeedName(feed.getFeedName());
        currentFeed.setFeedSubscribers(feed.getFeedSubscribers());
        currentFeed.setEmailTemplate(feed.getEmailTemplate());
        feedRepository.save(currentFeed);

        return currentFeed;
    }

    public void deleteFeed(String inputSystemId, String feedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);
        feedRepository.deleteById(feed.getFeedId());
    }

    public Feed subscribeToFeed(String inputSystemId, String feedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);
        if (feed.getFeedSubscribers().contains(email)) {
            throw new FeedAlreadySubscribedException();
        }

        feed.getFeedSubscribers().add(email);
        feedRepository.save(feed);

        return feed;
    }

    public Feed unsubscribeToFeed(String inputSystemId, String feedId, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);

        if (!feed.getFeedSubscribers().contains(email)) {
            throw new FeedNotSubscribedException();
        }

        feed.getFeedSubscribers().remove(email);
        feedRepository.save(feed);

        return feed;
    }

    public Feed postFeed(String inputSystemId, String feedId, EmailProviderProperties emailProviderProperties, String email) {
        if (!userRepository.findById(email).isPresent()) {
            throw new NotAuthenticatedException();
        }

        getInputSystemByEmailAndInputSystemId(email, inputSystemId);

        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);

        EmailTemplate emailTemplate = feed.getEmailTemplate();
        if(emailTemplate == null) {
            feed.setEmailTemplate(createEmailTemplateFromFeed(feed, email));
        }

        ReadyEmail readyEmail = createReadyEmailFromFeed(feed, emailProviderProperties, email);

        senderService.sendReadyEmail(readyEmail);

        return feed;
    }

    private ReadyEmail createReadyEmailFromFeed(Feed feed, EmailProviderProperties emailProviderProperties, String email) {
        EmailTemplate emailTemplate = feed.getEmailTemplate();
        emailTemplate.setEmailTemplateTo(Collections.singleton(email));
        emailTemplate.setInputSystem(feed.getInputSystem());

        ReadyEmail readyEmail = new ReadyEmail();
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        readyEmail.setEmailTemplate(emailTemplate);

        return readyEmail;
    }

    private EmailTemplate createEmailTemplateFromFeed(Feed feed, String email) {
        EmailTemplate emailTemplate = new EmailTemplate();

        emailTemplate.setEmailTemplateName(feed.getFeedName());
        emailTemplate.setEmailTemplateSubject(feed.getFeedName());
        emailTemplate.setEmailTemplateText(feed.getFeedName());
        emailTemplate.setEmailTemplateFromAddress(defaultNosyFromAddress);
        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        emailTemplate.setEmailTemplateTo(Collections.singleton(email));
        emailTemplate.setFeeds(Collections.singleton(feed));
        emailTemplate.setInputSystem(feed.getInputSystem());

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
