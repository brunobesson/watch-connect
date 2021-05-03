package org.camptocamp.watchconnect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class StravaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StravaService.class);

    private final StravaUserSetupService userSetupService;
    private final StravaClient stravaClient;
    private final String hostUrl;
    private final String subscriptionVerifyToken;

    private String subscriptionId;

    @Autowired
    public StravaService(
            final StravaUserSetupService userSetupService,
            final StravaClient stravaClient,
            @Value("${host.url}") final String hostUrl,
            @Value("${strava.subscription}") final String subscriptionId // FIXME in db instead
    ) {
        this.userSetupService = userSetupService;
        this.stravaClient = stravaClient;
        this.hostUrl = hostUrl;

        this.subscriptionId = Optional.ofNullable(subscriptionId).map(String::trim).orElse("");

        this.subscriptionVerifyToken = UUID.randomUUID().toString();
        LOGGER.info("Subscription verify token: {}", subscriptionVerifyToken);
    }

    @PostConstruct
    private void setupWebhook() throws IOException {
        if (subscriptionId.isBlank()) {
            requestSubscription();
        } else {
            try {
                final StravaSubscription subscription = stravaClient.viewSubscription();
                LOGGER.info("Subscription is ok: {}", subscription.getId());
            } catch (IOException e) {
                requestSubscription();
            }
        }
    }

    private void requestSubscription() throws IOException {
        final StravaSubscription subscription
                = stravaClient.requestSubscriptionCreation(hostUrl + "/strava/webhook", subscriptionVerifyToken);
        LOGGER.info("New subscription: {}", subscription.getId());
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
        userSetupService.setupUser(token);
    }
}
