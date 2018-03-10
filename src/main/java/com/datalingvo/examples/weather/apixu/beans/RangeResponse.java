/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.weather.apixu.beans;

/**
 * REST parsing bean.
 */
public class RangeResponse {
    private Location location;
    private DaysList forecast;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DaysList getForecast() {
        return forecast;
    }
    
    public void setForecast(DaysList forecast) {
        this.forecast = forecast;
    }
}
