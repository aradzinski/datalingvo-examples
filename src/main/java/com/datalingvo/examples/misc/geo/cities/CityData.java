/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.misc.geo.cities;

/**
 * City data holder.
 */
public class CityData {
    private String timezone;
    private double latitude;
    private double longitude;

    /**
     * Creates new city data holder.
     *
     * @param timezone City timezone
     * @param latitude City latitude.
     * @param longitude City longitude.
     */
    public CityData(String timezone, double latitude, double longitude) {
        this.timezone = timezone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets timezone.
     *
     * @return City timezone.
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Gets latitude.
     *
     * @return City latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets longitude.
     *
     * @return City longitude.
     */
    public double getLongitude() {
        return longitude;
    }
}
