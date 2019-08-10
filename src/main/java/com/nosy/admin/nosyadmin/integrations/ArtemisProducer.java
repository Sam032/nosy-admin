package com.nosy.admin.nosyadmin.integrations;

import com.nosy.admin.nosyadmin.model.ReadyEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArtemisProducer {

    private static final Logger logger = LoggerFactory.getLogger(ArtemisProducer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${ARTEMIS_BROKER_QUEUE}")
    private String artemisQueue;

    public void sendReadyEmail(ReadyEmail readyEmail) {
        logger.info("Message produced: {}", readyEmail);
        jmsTemplate.convertAndSend(artemisQueue, readyEmail);
    }

}
