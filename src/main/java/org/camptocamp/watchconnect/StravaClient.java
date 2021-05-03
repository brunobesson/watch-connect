package org.camptocamp.watchconnect;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class StravaClient {
    private final String clientId;
    private final String clientSecret;

    private final StravaApi api;

    public interface StravaApi {
        @POST("oauth/token")
        Call<StravaToken> exchangeTokens(
                @Query("client_id") String clientId,
                @Query("client_secret") String clientSecret,
                @Query("code") String code,
                @Query("grant_type") String grantType
        );

        @GET("athlete/activities")
        Call<List<SummaryActivity>> listAthleteActivities(
                @Header("Authorization") String token,
                @Query("before") Integer before,
                @Query("after") Integer after,
                @Query("page") Integer page,
                @Query("per_page") Integer numberOfItemsPerPage
        );

        @POST("push_subscriptions")
        Call<StravaSubscription> requestSubscriptionCreation(
                @Query("client_id") String clientId,
                @Query("client_secret") String clientSecret,
                @Query("callback_url") String callbackUrl,
                @Query("verify_token") String verifyToken
        );

        @GET("push_subscriptions")
        Call<List<StravaSubscription>> viewSubscription(
                @Query("client_id") String clientId,
                @Query("client_secret") String clientSecret
        );

        @Multipart
        @DELETE("push_subscriptions/{id}")
        Call<ResponseBody> deleteSubscription(
                @Path("id") String subscriptionId,
                @Part("client_id") String clientId,
                @Part("client_secret") String clientSecret
        );
    }

    @Autowired
    public StravaClient(
            @Value("${strava.url}") final String baseUrl,
            @Value("${strava.client-id}") final String clientId,
            @Value("${strava.client-secret}") final String clientSecret
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        // FIXME remove when ok
        final var interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        this.api = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build()
                .create(StravaApi.class);
    }

    public String getAuthToken(final String code) throws IOException {
        final Response<StravaToken> response
                = api.exchangeTokens(this.clientId, this.clientSecret, code, "authorization_code").execute();
        return response.body().getAccessToken();
    }

    public List<SummaryActivity> getActivities(final String token) throws IOException {
        final Response<List<SummaryActivity>> response = api.listAthleteActivities(bearer(token), null, null, 1, 30).execute();
        return response.body();
    }

    public StravaSubscription requestSubscriptionCreation(
            final String callbackUrl,
            final String verifyToken
    ) throws IOException {
        final Response<StravaSubscription> response
                = api.requestSubscriptionCreation(this.clientId, this.clientSecret, callbackUrl, verifyToken).execute();
        // TODO properly check status & all, return only on success
        return response.body();
    }

    public List<StravaSubscription> getSubscriptions() throws IOException {
        final Response<List<StravaSubscription>> response = api.viewSubscription(this.clientId, this.clientSecret).execute();
        return response.body();
    }

    public void deleteSubscription(final String subscriptionId) throws IOException {
        final Response<ResponseBody> response
                = api.deleteSubscription(subscriptionId, this.clientId, this.clientSecret).execute();
        if (response.code() != 204) {
            throw new IOException(
                    Optional.ofNullable(response.body())
                            .map(responseBody -> {
                                try {
                                    return responseBody.string();
                                } catch (final IOException e) {
                                    return null;
                                }
                            })
                            .orElse(null)
            );
        }
    }

    private String bearer(final String token) {
        return "Bearer " + token;
    }
}
