package com.marina.tanasyuk.cityguide;

/**
 * TODO Add specs Model representing Place object.
 */

public class MyPlace {

    enum Type {
        BAR, BISTRO, CAFE
    }

    private final String name;
    private final Type type;
    private final int rating;

    MyPlace(String name, Type type, int rating) {
        this.name = name;
        this.type = type;
        this.rating = rating;
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

    @Override
    public String toString() {
        return String.format("%s %s", type, name);
    }
}
