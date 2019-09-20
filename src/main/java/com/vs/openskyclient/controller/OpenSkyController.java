package com.vs.openskyclient.controller;

import com.vs.openskyclient.service.OpenSkyService;
import org.opensky.model.OpenSkyStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenSkyController {

    @Autowired
    OpenSkyService openSkyService;

    @RequestMapping("/states")
    public OpenSkyStates getOpenSkyStates() {
        // TODO: Use query parameters for filtering
        return openSkyService.getStates(0, null);
    }

}
