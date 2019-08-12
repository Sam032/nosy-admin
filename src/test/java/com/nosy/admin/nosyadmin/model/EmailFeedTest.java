package com.nosy.admin.nosyadmin.model;

import com.nosy.admin.nosyadmin.exceptions.EmailFeedNameInvalidException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmailFeedTest {

    @Test(expected = EmailFeedNameInvalidException.class)
    public void onCreate() {
        EmailFeed emailFeed = new EmailFeed();
        emailFeed.onCreate();
    }

    @Test(expected = EmailFeedNameInvalidException.class)
    public void onCreateEmpty() {
        EmailFeed emailFeed = new EmailFeed();
        emailFeed.setEmailFeedName("");
        emailFeed.onCreate();
    }

    @Test()
    public void onCreateSuccess() {
        EmailFeed emailFeed = new EmailFeed();
        emailFeed.setEmailFeedName("emailFeedName");
        emailFeed.onCreate();
    }

}
