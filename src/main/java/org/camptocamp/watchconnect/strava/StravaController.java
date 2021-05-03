package org.camptocamp.watchconnect.strava;

import org.camptocamp.watchconnect.strava.dto.StravaEvent;
import org.camptocamp.watchconnect.strava.dto.StravaSubscriptionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/strava")
public class StravaController {

    private final StravaService stravaService;
    private final String frontendBaseUrl;
    private final String frontendSubscriptionError;

    @Autowired
    public StravaController(
            final StravaService stravaService,
            @Value("${frontend.base-url}") final String frontendBaseUrl,
            @Value("${frontend.subscription.error}") final String frontendSubscriptionError
    ) {
        this.stravaService = stravaService;
        this.frontendBaseUrl = frontendBaseUrl;
        this.frontendSubscriptionError = frontendSubscriptionError;
    }

    @GetMapping("/exchange_token")
    public ModelAndView exchangeTokens(
            @RequestParam("code") final String authorizationCode,
            @RequestParam("scope") final List<String> scopes
    ) {
        if (!stravaService.containsRequiredScopes(scopes)) {
            return new ModelAndView("redirect:" + frontendBaseUrl + "/" + frontendSubscriptionError);
        }
        try {
            stravaService.requestShortLivedAccessTokenAndSetupUser(authorizationCode);
        } catch (final IOException e) {
            return new ModelAndView("redirect:" + frontendBaseUrl + "/" + frontendSubscriptionError);
        }
        return new ModelAndView("redirect:" + frontendBaseUrl);
    }

    @GetMapping("/webhook")
    public StravaSubscriptionValidation validateWebhookSubscription(
            @RequestParam("hub.mode") final String mode,
            @RequestParam("hub.challenge") final String challenge,
            @RequestParam("hub.verify_token") final String token,
            @Value("${strava.subscription.verify-token}") final String subscriptionVerifyToken
    ) {
        if (!"subscribe".equals(mode) || !Objects.equals(token, subscriptionVerifyToken)) {
            throw new ResponseStatusException(FORBIDDEN);
        }
        return new StravaSubscriptionValidation(challenge);
    }

    @PostMapping("/webhook")
    public void webhookEvent(@Validated @RequestBody final StravaEvent event) {
        stravaService.handleWebhookEvent(event);
    }

    // TODO: admin part, e.g. close subscription?
}
