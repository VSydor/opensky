package com.vs.openskyclient.service.impl;

import com.vs.openskyclient.queue.KafkaClient;
import com.vs.openskyclient.service.OpenSkyService;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;
import org.opensky.model.StateVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OpenSkyServiceImpl implements OpenSkyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenSkyService.class);

    @Autowired
    OpenSkyApi openSkyApi;

    @Autowired
    KafkaClient kafkaClient;

    @Override
    public OpenSkyStates getStates(int time, String[] icao24) {
        OpenSkyStates states = new OpenSkyStates();
        try {
            states = openSkyApi.getStates(time, icao24);
            LOGGER.info("Got {} states.", states.getStates().size());

            // Sending to kafka
            sendToKafka(states);

        } catch (Exception e) {
            LOGGER.error("Oops! Something went wrong!", e);
        }
        return states;
    }

    private void sendToKafka(OpenSkyStates states) {
        if (states == null) {
            LOGGER.info("Have nothing to send!");
            return;
        }
        // TODO: add filtering here?
        List<StateVector> austrian = states.getStates()
                .stream().parallel()
                .filter(s -> s.getOriginCountry().equals("Austria"))
                .collect(Collectors.toList());

        if (austrian.isEmpty()) {
            LOGGER.info("Have no Austria planes to send!");
            return;
        }

        austrian.forEach(kafkaClient::send);
        LOGGER.info("Sent {} items to Kafka.", austrian.size());
    }
}
