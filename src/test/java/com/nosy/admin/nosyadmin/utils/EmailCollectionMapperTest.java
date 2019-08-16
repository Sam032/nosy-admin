package com.nosy.admin.nosyadmin.utils;

import com.nosy.admin.nosyadmin.dto.EmailCollectionDto;
import com.nosy.admin.nosyadmin.model.EmailCollection;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EmailCollectionMapperTest {

    private EmailCollection emailCollection = new EmailCollection();
    private EmailCollectionDto emailCollectionDto = new EmailCollectionDto();

    @Before
    public void setUp(){
        emailCollection.setEmailCollectionId("TestEmailCollectionId");
        emailCollection.setEmailCollectionName("TestEmailCollectionName");
        emailCollection.setEmailCollectionEmails(new ArrayList<>());

        emailCollectionDto.setId("TestEmailCollectionDtoId");
        emailCollectionDto.setName("TestEmailCollectionDtoName");
        emailCollectionDto.setEmails(new ArrayList<>());
    }
    @Test
    public void toEmailCollectionDto(){
        assertEquals(emailCollection.getEmailCollectionId(), EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollection).getId());
        assertEquals(emailCollection.getEmailCollectionName(), EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollection).getName());
        assertArrayEquals(emailCollection.getEmailCollectionEmails().toArray(), EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollection).getEmails().toArray());
    }

    @Test
    public void toEmailCollection(){
        assertEquals(emailCollectionDto.getId(), EmailCollectionMapper.INSTANCE.toEmailCollection(emailCollectionDto).getEmailCollectionId());
        assertEquals(emailCollectionDto.getName(), EmailCollectionMapper.INSTANCE.toEmailCollection(emailCollectionDto).getEmailCollectionName());
        assertArrayEquals(emailCollectionDto.getEmails().toArray(), EmailCollectionMapper.INSTANCE.toEmailCollection(emailCollectionDto).getEmailCollectionEmails().toArray());
    }

}
