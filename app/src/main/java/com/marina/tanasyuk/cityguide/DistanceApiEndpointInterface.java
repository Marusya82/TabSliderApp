package com.marina.tanasyuk.cityguide;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface for Google Maps Distance Matrix API.
 */

public interface DistanceApiEndpointInterface {

    @GET("json?units=imperial&origins={origin}&destinations={destination}&key={api_key")
    Call<DistanceResponse> getDistanceResponse(@Query("origins") String origins,
                                       @Query("destinations") String destinations,
                                       @Query("key") String apiKey);

}
