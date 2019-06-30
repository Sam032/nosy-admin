package com.nosy.admin.nosyadmin.utils;

import com.nosy.admin.nosyadmin.dto.InputSystemDto;
import com.nosy.admin.nosyadmin.model.EmailTemplate;
import com.nosy.admin.nosyadmin.model.InputSystem;
import com.nosy.admin.nosyadmin.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class InputSystemMapperTest {
    private InputSystemDto inputSystemDto=new InputSystemDto();
    private InputSystem inputSystem=new InputSystem();
    @Before
    public void setUp(){
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        User user=new User();
        user.setEmail("testUser");
        inputSystem.setUser(user);
        inputSystem.setInputSystemName("inputSystemName");
        EmailTemplate emailTemplate=new EmailTemplate();
        emailTemplate.setEmailTemplateName("dadasd");
        Set<EmailTemplate> emailTemplateSet=new HashSet<>();
        emailTemplateSet.add(emailTemplate);
        inputSystem.setInputSystemId("dadasdsa");
        inputSystem.setEmailTemplate(emailTemplateSet);

        InputSystemDto inputSystemDto=new InputSystemDto();
        inputSystemDto.setId("inputSystemDtoId");
        inputSystemDto.setName("inputSystemDtoName");
    }


    @Test
    public void toInputSystemDto(){
        assertEquals(inputSystem.getInputSystemId(),InputSystemMapper.INSTANCE.toInputSystemDto(inputSystem).getId());
        assertEquals(inputSystem.getInputSystemName(),InputSystemMapper.INSTANCE.toInputSystemDto(inputSystem).getName());
    }

    @Test
    public void toInputSystem(){
        assertEquals(inputSystemDto.getId(),InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto).getInputSystemId());
        assertEquals(inputSystemDto.getName(),InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto).getInputSystemName());
    }
}
