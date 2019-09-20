package com.vs.openskyclient.service.impl;

import com.vs.openskyclient.service.OpenSkyService;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OpenSkyServiceImpl implements OpenSkyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenSkyService.class);

    @Autowired
    OpenSkyApi openSkyApi;

    @Override
    public OpenSkyStates getStates(int time, String[] icao24) {
        OpenSkyStates states = new OpenSkyStates();
        try {
            states = openSkyApi.getStates(time, icao24);
            LOGGER.info("Got states: {}", states);
        } catch (Exception e) {
            LOGGER.error("Failed to get states! {}", e);
        }
        return states;
    }
}
