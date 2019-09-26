package com.vs.openskyclient.queue.impl;

import com.vs.openskyclient.queue.KafkaClient;
import org.apache.kafka.clients.producer.Producer;
import org.opensky.model.StateVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClientImpl implements KafkaClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<Long, StateVector> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    @Override
    public void send(StateVector message) {
        LOGGER.info(String.format("Sending message --> %s", message));
        kafkaTemplate.send(topic, message);
    }

}
