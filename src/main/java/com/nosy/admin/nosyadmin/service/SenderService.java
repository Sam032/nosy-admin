package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.integrations.ArtemisProducer;
import com.nosy.admin.nosyadmin.integrations.KafkaProducer;
import com.nosy.admin.nosyadmin.model.ReadyEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SenderService {

    @Value("${NOSY_BROKER_TYPE}")
    private String nosyBrokerType;

    private KafkaProducer kafkaProducer;
    private ArtemisProducer artemisProducer;

    @Autowired
    public SenderService(KafkaProducer kafkaProducer, ArtemisProducer artemisProducer) {
        this.kafkaProducer = kafkaProducer;
        this.artemisProducer = artemisProducer;
    }

    public void sendReadyEmail(ReadyEmail readyEmail) {
        switch (nosyBrokerType) {
            case "artemis": {
                artemisProducer.sendReadyEmail(readyEmail);
                break;
            }
            case "kafka":
            default: {
                kafkaProducer.sendMessage(readyEmail.toString());
                break;
            }
        }
    }

}
