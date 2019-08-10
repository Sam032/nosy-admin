package com.nosy.admin.nosyadmin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosy.admin.nosyadmin.model.ReadyEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ArtemisConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("_type", ReadyEmail.class);
        converter.setTypeIdMappings(typeIdMappings);
        converter.setObjectMapper(new ObjectMapper());
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setMessageConverter(messageConverter());
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
