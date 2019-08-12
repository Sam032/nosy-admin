package com.nosy.admin.nosyadmin.utils;

import com.nosy.admin.nosyadmin.dto.EmailFeedDto;
import com.nosy.admin.nosyadmin.model.EmailFeed;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmailFeedMapperTest {

    private EmailFeed emailFeed = new EmailFeed();
    private EmailFeedDto emailFeedDto = new EmailFeedDto();

    @Before
    public void setUp(){
        emailFeed = new EmailFeed();
        emailFeed.setEmailFeedId("emailFeedId");
        emailFeed.setEmailFeedName("emailFeedName");

        emailFeedDto = new EmailFeedDto();
        emailFeedDto.setId("emailFeedDtoId");
        emailFeedDto.setName("emailFeedDtoName");
    }

    @Test
    public void toEmailFeedDto(){
        assertEquals(emailFeed.getEmailFeedId(), EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getId());
        assertEquals(emailFeed.getEmailFeedName(),EmailFeedMapper.INSTANCE.toEmailFeedDto(emailFeed).getName());
    }

    @Test
    public void toEmailFeed(){
        assertEquals(emailFeedDto.getId(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getEmailFeedId());
        assertEquals(emailFeedDto.getName(), EmailFeedMapper.INSTANCE.toEmailFeed(emailFeedDto).getEmailFeedName());
    }

}
