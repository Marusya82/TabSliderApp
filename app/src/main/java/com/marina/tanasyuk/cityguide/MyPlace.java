package com.marina.tanasyuk.cityguide;

/**
 * Model representing Place object.
 */

public class MyPlace {

    enum Type {
        BAR, BISTRO, CAFE
    }

    private final String name;
    private final Type type;
    private final int rating;
    private final String placeID;

    MyPlace(String name, Type type, int rating, String placeId) {
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.placeID = placeId;
    }

    String getName() {
        return name;
    }

    Type getType() {
        return type;
    }

    int getRating() {
        return rating;
    }

    public String getPlaceID() {
        return placeID;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type, name);
    }
}
