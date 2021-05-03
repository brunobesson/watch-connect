package org.camptocamp.watchconnect.strava;

import org.camptocamp.watchconnect.strava.api.StravaClient;
import org.camptocamp.watchconnect.strava.dto.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Service
public class StravaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StravaService.class);

    private final StravaUserSetupService userSetupService;
    private final StravaSubscriptionService subscriptionService;
    private final StravaClient stravaClient;
    private final String backendBaseUrl;

    private String subscriptionId;

    @Autowired
    public StravaService(
            final StravaUserSetupService userSetupService,
            final StravaSubscriptionService subscriptionService,
            final StravaClient stravaClient,
            @Value("${backend.base-url}") final String backendBaseUrl,
            @Value("${strava.subscription.id}") final String subscriptionId // FIXME in db instead
    ) {
        this.userSetupService = userSetupService;
        this.subscriptionService = subscriptionService;
        this.stravaClient = stravaClient;
        this.backendBaseUrl = backendBaseUrl;

        this.subscriptionId = Optional.ofNullable(subscriptionId).map(String::trim).orElse("");


    }

    @PostConstruct
    private void setupWebhook() throws IOException {
        subscriptionService.setupWebhook();
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
