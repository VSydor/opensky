package com.vs.openskyclient.config;

import org.opensky.api.OpenSkyApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSkyAppConfiguration {

    @Value("${opensky.api.username}")
    private String username;
    @Value("${opensky.api.username}")
    private String password;

    @Bean
    public OpenSkyApi openSkyApi() {
        return new OpenSkyApi("xinob", "airMAX90");
    }
}
