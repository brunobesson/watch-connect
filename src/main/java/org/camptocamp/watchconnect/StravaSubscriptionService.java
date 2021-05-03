package org.camptocamp.watchconnect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StravaSubscriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StravaSubscriptionService.class);

    private final StravaClient stravaClient;
    private final String backendBaseUrl;
    private final String subscriptionVerifyToken;
    private String subscriptionId;

    @Autowired
    public StravaSubscriptionService(
            final StravaClient stravaClient,
            @Value("${backend.base-url}") final String backendBaseUrl,
            @Value("${strava.subscription.verify-token}") final String subscriptionVerifyToken,
            @Value("${strava.subscription.id}") final String subscriptionId // FIXME in db instead
    ) {
        this.stravaClient = stravaClient;
        this.backendBaseUrl = backendBaseUrl;
        this.subscriptionId = subscriptionId;
        this.subscriptionVerifyToken = subscriptionVerifyToken;
        LOGGER.info("Subscription verify token: {}", subscriptionVerifyToken);
    }

    @Async
    public void setupWebhook() throws IOException {
        if (subscriptionId.isBlank()) {
            requestSubscription();
        } else {
            checkSubscription();
        }
    }

    private void checkSubscription() throws IOException {
        try {
            final List<StravaSubscription> subscriptions = stravaClient.getSubscriptions();
            final String subscriptionId = subscriptions.stream()
                    .filter(subscription -> subscription.getCallbackUrl().equals(callbackUrl()))
                    .map(StravaSubscription::getId)
                    .findFirst()
                    .orElseThrow(() -> new IOException("Invalid subscription id, not found"));
        } catch (IOException e) {
            requestSubscription();
        }
    }

    private void requestSubscription() throws IOException {
        // FIXME if subscription already exists => boom
        final StravaSubscription subscription
                = stravaClient.requestSubscriptionCreation(callbackUrl(), subscriptionVerifyToken);
        LOGGER.info("New subscription: {}", subscription.getId());
    }

    private String callbackUrl() {
        return backendBaseUrl + "/strava/webhook";
    }
}
