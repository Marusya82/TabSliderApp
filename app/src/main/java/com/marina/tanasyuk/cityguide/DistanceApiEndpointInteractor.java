package com.marina.tanasyuk.cityguide;

import android.content.Context;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Interactor for Google Maps Distance Matrix API.
 */

public class DistanceApiEndpointInteractor {

    private static final String BASE_URL = "http://maps.googleapis.com/maps/api/distancematrix/";

    private DistanceApiEndpointInterface apiService;

    private final Context context;

    public DistanceApiEndpointInteractor(MainPresenterImpl presenter, Context context) {
        this.context = context;
        init();
    }

    private void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService =
                retrofit.create(DistanceApiEndpointInterface.class);
    }

    public void getDistance(String origins, String destinations) {
        // TODO add API call
        String apiKey = context.getResources().getString(R.string.API_KEY);
        Call<DistanceResponse> call = apiService.getDistanceResponse(origins, destinations, apiKey);
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DistanceResponse> call, @NonNull Response<DistanceResponse> response) {
                // TODO Handle response
                int statusCode = response.code();
                DistanceResponse distanceResponse = response.body();
                if (distanceResponse != null) {
                    distanceResponse.getDistance();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DistanceResponse> call, @NonNull Throwable t) {
                // TODO Handle errors
            }
        });
    }
}
