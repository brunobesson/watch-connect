package org.camptocamp.watchconnect.strava;

import org.camptocamp.watchconnect.strava.api.StravaClient;
import org.camptocamp.watchconnect.strava.dto.StravaEvent;
import org.camptocamp.watchconnect.strava.dto.StravaEventAspect;
import org.camptocamp.watchconnect.strava.dto.SummaryActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StravaUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StravaUserService.class);

    private final StravaClient stravaClient;

    @Autowired
    public StravaUserService(final StravaClient stravaClient) {
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

    @Async
    public void handleWebhookEvent(final StravaEvent event) {
        switch(event.getEventType()) {
            case ATHLETE:
                if (event.getAspect() == StravaEventAspect.DELETE) {
                    // TODO clear user data
                }
                break;
            case ACTIVITY:
                switch (event.getAspect()) {
                    case CREATE:
                        // TODO retrieve activity, add
                        break;
                    case UPDATE:
                        // TODO update of title / type / private
                        break;
                    case DELETE:
                        // TODO
                        break;
                }
                break;
        }
    }
}
