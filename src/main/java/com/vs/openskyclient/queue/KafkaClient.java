package com.vs.openskyclient.queue;

import org.opensky.model.StateVector;

public interface KafkaClient {

    void send(StateVector message);
}
