package com.marina.tanasyuk.cityguide;

import android.content.Context;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Interactor for Google Maps Distance Matrix API.
 */

public class DistanceApiEndpointInteractor {

    private static final String BASE_URL = "http://maps.googleapis.com/maps/api/distancematrix/";

    private DistanceApiEndpointInterface apiService;

    private final Context context;

    private OnDistanceResponseReceivedListener listener;

    public DistanceApiEndpointInteractor(OnDistanceResponseReceivedListener listener, Context context) {
        this.context = context;
        this.listener = listener;
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
        String apiKey = context.getResources().getString(R.string.API_KEY);
        // TODO Implement RxJava
        Call<DistanceResponse> call = apiService.getDistanceResponse(origins, destinations, apiKey);
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DistanceResponse> call, @NonNull Response<DistanceResponse> response) {
                // TODO Handle response
                Timber.i("Distance response received");
                DistanceResponse distanceResponse = response.body();
                if (distanceResponse != null) {
                    distanceResponse.getDistance();
                    listener.onDistanceResponse(distanceResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DistanceResponse> call, @NonNull Throwable t) {
                Timber.e("Distance API call failed", t.getMessage());
                // TODO update UI to handle failed response
            }
        });
    }

    public interface OnDistanceResponseReceivedListener {
        void onDistanceResponse(DistanceResponse distanceResponse);
    }
}
