package org.camptocamp.watchconnect;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public StravaController(final StravaService stravaService) {
        this.stravaService = stravaService;
    }

    @GetMapping("/exchange_token")
    public ModelAndView exchangeTokens(
            @RequestParam("code") final String authorizationCode,
            @RequestParam("scope") final List<String> scopes
    ) {
        if (!stravaService.containsRequiredScopes(scopes)) {
            return new ModelAndView("redirect:http://localhost:8080/baaad!"); // FIXME
        }
        try {
            stravaService.requestShortLivedAccessTokenAndSetupUser(authorizationCode);
        } catch (final IOException e) {
            return new ModelAndView("redirect:http://localhost:8080/baaad!"); // FIXME
        }
        return new ModelAndView("redirect:http://localhost:8080");
    }

    // FIXME what happens if requested twice? maybe not here but on POST?
    @GetMapping("/webhook")
    public StravaSubscriptionValidation validateWebhookSubscription(
            @RequestParam("hub.mode") final String mode,
            @RequestParam("hub.challenge") final String challenge,
            @RequestParam("hub.verify_token") final String token
    ) {
        if ("subscribe".equals(mode) || !Objects.equals(token, stravaService.getSubscriptionVerifyToken())) {
            throw new ResponseStatusException(FORBIDDEN);
        }
        return new StravaSubscriptionValidation(challenge);
    }

    @PostMapping("/webhook")
    public void webhookEvent(@Validated @RequestBody final StravaEvent event) {
        // TODO: should return 200 immediatly
        // TODO decide what we should do in such cases. do this async
        event.getEventType();
    }

    // TODO: admin part, e.g. close subscription
}
