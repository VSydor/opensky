package com.vs.openskyclient.service.impl;

import com.vs.openskyclient.queue.KafkaClient;
import com.vs.openskyclient.service.OpenSkyService;
import org.glassfish.jersey.internal.guava.Lists;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;
import org.opensky.model.StateVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
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
    public OpenSkyStates getStates(int time, String[] icao24, String originCountry) {
        OpenSkyStates states = new OpenSkyStates();
        try {
            states = openSkyApi.getStates(time, icao24);
            LOGGER.info("Got {} states.", states.getStates().size());

            // Sending to kafka
            sendToKafka(states, originCountry);

        } catch (Exception e) {
            LOGGER.error("Oops! Something went wrong!", e);
        }
        return states;
    }

    private void sendToKafka(OpenSkyStates states, String originCountry) {

        // Filter states using given origin country
        List<StateVector> toSend = filter(states.getStates(), originCountry);

        if (toSend.isEmpty()) {
            LOGGER.info("Have no data to send!");
            return;
        }

        toSend.forEach(kafkaClient::send);
        LOGGER.info("Sent {} items to Kafka.", toSend.size());
    }

    private List<StateVector> filter(Collection<StateVector> collection, String originCountry) {
        List<StateVector> filtered = Lists.newArrayList(collection);
        if (!StringUtils.isEmpty(originCountry)) {
            LOGGER.info("Filtering data using origin '{}'!", originCountry);
            filtered = collection
                    .stream().parallel()
                    .filter(s -> s.getOriginCountry().equalsIgnoreCase(originCountry))
                    .collect(Collectors.toList());
        }
        return filtered;
    }
}
