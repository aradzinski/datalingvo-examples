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
public class Astro {
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;

    /**
     *
     * @return
     */
    public String getSunrise() {
        return sunrise;
    }

    /**
     *
     * @param sunrise
     */
    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    /**
     *
     * @return
     */
    public String getSunset() {
        return sunset;
    }

    /**
     *
     * @param sunset
     */
    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    /**
     *
     * @return
     */
    public String getMoonrise() {
        return moonrise;
    }

    /**
     *
     * @param moonrise
     */
    public void setMoonrise(String moonrise) {
        this.moonrise = moonrise;
    }

    /**
     *
     * @return
     */
    public String getMoonset() {
        return moonset;
    }

    /**
     * 
     * @param moonset
     */
    public void setMoonset(String moonset) {
        this.moonset = moonset;
    }
}
