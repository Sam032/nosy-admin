package com.nosy.admin.nosyadmin.integrations;

import com.nosy.admin.nosyadmin.model.ReadyEmail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ArtemisProducerTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private ArtemisProducer artemisProducer;

    @Test(expected = Test.None.class)
    public void sendMessage() {
        artemisProducer.sendReadyEmail(new ReadyEmail());
    }

}
