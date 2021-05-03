package org.camptocamp.watchconnect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class StravaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StravaService.class);

    private final StravaClient stravaClient;
    private final String subscriptionVerifyToken;

    @Autowired
    public StravaService(final StravaClient stravaClient) {
        this.stravaClient = stravaClient;
        this.subscriptionVerifyToken = UUID.randomUUID().toString();
        LOGGER.info("Subscription verify token: {}", subscriptionVerifyToken);
    }

    public String getSubscriptionVerifyToken() {
        return subscriptionVerifyToken;
    }

    /**
     * Checks whether user authorized scopes match our minimal requirements, i.e. we can install webhooks
     *
     * @param scopes granted scopes list
     * @return {@code true} if requirements are met
     */
    public boolean containsRequiredScopes(final Collection<String> scopes) {
        return scopes.stream()
                .map(Scope::of)
                .anyMatch(scope -> scope == Scope.ACTIVITY_READ || scope == Scope.ACTIVITY_READ_ALL);
    }

    public void requestShortLivedAccessTokenAndSetupUser(final String authorizationCode) throws IOException {
        final String token = stravaClient.getAuthToken(authorizationCode);
        setupUser(token);
        setupWebhook(token); // FIXME only once!
    }

    // FIXME async not working?
    @Async
    void setupUser(final String token) throws IOException {
        LOGGER.info("Setup user: " + Thread.currentThread().getName());
        // TODO store in DB, associate to DB
        // retrieve last 30 outings
        final List<SummaryActivity> activities = stravaClient.getActivities(token);

        LOGGER.info("ok");
    }

    @Async
    void setupWebhook(final String token) {
        LOGGER.info("Setup webhook: " + Thread.currentThread().getName());

    }
}
