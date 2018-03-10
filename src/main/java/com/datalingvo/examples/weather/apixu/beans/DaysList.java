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

import com.google.gson.annotations.*;

/**
 * REST parsing bean.
 */
public class DaysList {
    @SerializedName("forecastday") private DayInfo[] forecastDay;

    public DayInfo[] getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(DayInfo[] forecastDay) {
        this.forecastDay = forecastDay;
    }
}
