package com.vs.openskyclient.service;

import org.opensky.model.OpenSkyStates;


public interface OpenSkyService {

    OpenSkyStates getStates(int time, String[] icao24, String originCountry);
}
