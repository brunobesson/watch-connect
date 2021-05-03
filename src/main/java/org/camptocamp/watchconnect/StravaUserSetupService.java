package org.camptocamp.watchconnect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StravaUserSetupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StravaUserSetupService.class);

    private final StravaClient stravaClient;

    @Autowired
    public StravaUserSetupService(final StravaClient stravaClient) {
        this.stravaClient = stravaClient;
    }

    @Async
    public void setupUser(final String token) throws IOException {
        LOGGER.info("Setup user: " + Thread.currentThread().getName());
        // TODO store in DB, associate to DB
        // retrieve last 30 outings
        final List<SummaryActivity> activities = stravaClient.getActivities(token);

        LOGGER.info("ok");
    }
}
