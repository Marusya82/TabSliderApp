package com.marina.tanasyuk.cityguide;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Object class that represents response from Google Maps Distance Matrix API.
 */

public class DistanceResponse {

    @SerializedName("rows")
    List<Element> elementList;

    String getDistance() {
        return elementList.get(0).getDistance().getDistanceText();
    }

    private class Element {

        Distance distance;

        Distance getDistance() {
            return distance;
        }
    }

    private class Distance {

        String distanceText;

        String getDistanceText() {
            return distanceText;
        }
    }
}
