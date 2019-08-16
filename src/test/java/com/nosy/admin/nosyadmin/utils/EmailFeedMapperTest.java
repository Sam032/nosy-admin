package com.nosy.admin.nosyadmin.utils;

import com.nosy.admin.nosyadmin.dto.EmailFeedDto;
import com.nosy.admin.nosyadmin.model.EmailFeed;
import com.nosy.admin.nosyadmin.model.EmailTemplate;
import com.nosy.admin.nosyadmin.model.InputSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EmailFeedMapperTest {

    private EmailFeed emailFeed = new EmailFeed();
    private EmailFeedDto emailFeedDto = new EmailFeedDto();

    @Before
    public void setUp(){
        emailFeed = new EmailFeed();
        emailFeed.setEmailFeedId("emailFeedId");
        emailFeed.setEmailFeedName("emailFeedName");
        emailFeed.setEmailFeedAddress("emailFeedAddress");
        emailFeed.setEmailFeedSubscribers(Collections.singleton("emailFeedSubscriber"));
        emailFeed.setInputSystem(new InputSystem());
        emailFeed.setEmailTemplate(new EmailTemplate());

        emailFeedDto = new EmailFeedDto();
        emailFeedDto.setId("emailFeedDtoId");
        emailFeedDto.setName("emailFeedDtoName");
        emailFeedDto.setAddress("emailFeedDtoAddress");
        emailFeedDto.setSubscribers(Collections.singleton("emailFeedDtoSubscriber"));
        emailFeedDto.setInputSystem(new InputSystem());
        emailFeedDto.setEmailTemplate(new EmailTemplate());
    }

    @Test
    public void toEmailFeedDto(){
        assertEquals(emailFeed.getEmailFeedId(), EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getId());
        assertEquals(emailFeed.getEmailFeedName(),EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getName());
        assertEquals(emailFeed.getEmailFeedAddress(), EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getAddress());
        assertEquals(emailFeed.getEmailFeedSubscribers(), EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getSubscribers());
        assertEquals(emailFeed.getInputSystem(), EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getInputSystem());
        assertEquals(emailFeed.getEmailTemplate(), EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getEmailTemplate());
    }

    @Test
    public void toEmailFeed(){
        assertEquals(emailFeedDto.getId(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getEmailFeedId());
        assertEquals(emailFeedDto.getName(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getEmailFeedName());
        assertEquals(emailFeedDto.getAddress(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getEmailFeedAddress());
        assertEquals(emailFeedDto.getSubscribers(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getEmailFeedSubscribers());
        assertEquals(emailFeedDto.getInputSystem(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getInputSystem());
        assertEquals(emailFeedDto.getEmailTemplate(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getEmailTemplate());
    }

}
