package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.integrations.ArtemisProducer;
import com.nosy.admin.nosyadmin.integrations.KafkaProducer;
import com.nosy.admin.nosyadmin.model.ReadyEmail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SenderServiceTest {

    @InjectMocks
    private SenderService senderService;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ArtemisProducer artemisProducer;

    @Test
    public void sendReadyEmailKafka() {
        ReflectionTestUtils.setField(senderService, "nosyBrokerType", "kafka");
        senderService.sendReadyEmail(new ReadyEmail());
        verify(kafkaProducer).sendMessage(anyString());
    }

    @Test
    public void sendReadyEmailArtemis() {
        ReflectionTestUtils.setField(senderService, "nosyBrokerType", "artemis");
        senderService.sendReadyEmail(new ReadyEmail());
        verify(artemisProducer).sendReadyEmail(any(ReadyEmail.class));
    }

}
