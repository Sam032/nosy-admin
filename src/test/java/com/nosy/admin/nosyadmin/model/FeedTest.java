package com.nosy.admin.nosyadmin.model;

import com.nosy.admin.nosyadmin.exceptions.FeedNameInvalidException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeedTest {

    @Test(expected = FeedNameInvalidException.class)
    public void onCreate() {
        Feed feed = new Feed();
        feed.onCreate();
    }

    @Test(expected = FeedNameInvalidException.class)
    public void onCreateEmpty() {
        Feed feed = new Feed();
        feed.setFeedName("");
        feed.onCreate();
    }

    @Test()
    public void onCreateSuccess() {
        Feed feed = new Feed();
        feed.setFeedName("feedName");
        feed.onCreate();
    }

}
