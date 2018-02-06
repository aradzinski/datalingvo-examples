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

import com.google.gson.annotations.SerializedName;

/**
 * REST parsing bean.
 */
public class Location {
    private String name;
    private String region;
    @SerializedName("lat") private String latitude;
    @SerializedName("lon") private String longitude;
    @SerializedName("tz_id") private String tzId;
    @SerializedName("localtime_epoch") private String localtimeEpoch;
    private String localtime;

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getRegion() {
        return region;
    }

    /**
     *
     * @param region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     *
     * @return
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     */
    public String getTzId() {
        return tzId;
    }

    /**
     *
     * @param tzId
     */
    public void setTzId(String tzId) {
        this.tzId = tzId;
    }

    /**
     *
     * @return
     */
    public String getLocaltimeEpoch() {
        return localtimeEpoch;
    }

    /**
     *
     * @param localtimeEpoch
     */
    public void setLocaltimeEpoch(String localtimeEpoch) {
        this.localtimeEpoch = localtimeEpoch;
    }

    /**
     *
     * @return
     */
    public String getLocaltime() {
        return localtime;
    }

    /**
     * 
     * @param localtime
     */
    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }
}

