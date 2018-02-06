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
public class Day {
    @SerializedName("maxtemp_c") private Double maxTempC;
    @SerializedName("maxtemp_f") private Double maxTempF;
    @SerializedName("mintemp_c") private Double minTempC;
    @SerializedName("mintemp_f") private Double minTempF;
    @SerializedName("avgtemp_c") private Double avgTempC;
    @SerializedName("avgtemp_f") private Double avgTempF;
    @SerializedName("maxwind_mph") private Double maxWindMph;
    @SerializedName("maxwind_kph") private Double maxWindKph;
    @SerializedName("totalprecip_mm") private Double totalPrecipMm;
    @SerializedName("totalprecip_in") private Double totalPrecipIn;
    @SerializedName("avgvis_km") private Double avgVisKm;
    @SerializedName("avgvis_miles") private Double avgVisMiles;
    @SerializedName("avghumidity") private int avgHumidity;
    private Condition condition;
    private Double uv;

    /**
     *
     * @return
     */
    public Double getMaxTempC() {
        return maxTempC;
    }

    /**
     *
     * @param maxTempC
     */
    public void setMaxTempC(Double maxTempC) {
        this.maxTempC = maxTempC;
    }

    /**
     *
     * @return
     */
    public Double getMaxTempF() {
        return maxTempF;
    }

    /**
     *
     * @param maxTempF
     */
    public void setMaxTempF(Double maxTempF) {
        this.maxTempF = maxTempF;
    }

    /**
     *
     * @return
     */
    public Double getMinTempC() {
        return minTempC;
    }

    /**
     *
     * @param minTempC
     */
    public void setMinTempC(Double minTempC) {
        this.minTempC = minTempC;
    }

    /**
     *
     * @return
     */
    public Double getMinTempF() {
        return minTempF;
    }

    /**
     *
     * @param minTempF
     */
    public void setMinTempF(Double minTempF) {
        this.minTempF = minTempF;
    }

    /**
     * 
     * @return
     */
    public Double getAvgTempC() {
        return avgTempC;
    }

    /**
     *
     * @param avgTempC
     */
    public void setAvgTempC(Double avgTempC) {
        this.avgTempC = avgTempC;
    }

    /**
     *
     * @return
     */
    public Double getAvgTempF() {
        return avgTempF;
    }

    /**
     * 
     * @param avgTempF
     */
    public void setAvgTempF(Double avgTempF) {
        this.avgTempF = avgTempF;
    }

    /**
     *
     * @return
     */
    public Double getMaxWindMph() {
        return maxWindMph;
    }

    /**
     *
     * @param maxWindMph
     */
    public void setMaxWindMph(Double maxWindMph) {
        this.maxWindMph = maxWindMph;
    }

    /**
     *
     * @return
     */
    public Double getMaxWindKph() {
        return maxWindKph;
    }

    /**
     *
     * @param maxWindKph
     */
    public void setMaxWindKph(Double maxWindKph) {
        this.maxWindKph = maxWindKph;
    }

    /**
     *
     * @return
     */
    public Double getTotalPrecipMm() {
        return totalPrecipMm;
    }

    /**
     *
     * @param totalPrecipMm
     */
    public void setTotalPrecipMm(Double totalPrecipMm) {
        this.totalPrecipMm = totalPrecipMm;
    }

    /**
     *
     * @return
     */
    public Double getTotalPrecipIn() {
        return totalPrecipIn;
    }

    /**
     *
     * @param totalPrecipIn
     */
    public void setTotalPrecipIn(Double totalPrecipIn) {
        this.totalPrecipIn = totalPrecipIn;
    }

    /**
     *
     * @return
     */
    public Double getAvgVisKm() {
        return avgVisKm;
    }

    /**
     *
     * @param avgVisKm
     */
    public void setAvgVisKm(Double avgVisKm) {
        this.avgVisKm = avgVisKm;
    }

    /**
     *
     * @return
     */
    public Double getAvgVisMiles() {
        return avgVisMiles;
    }

    /**
     * 
     * @param avgVisMiles
     */
    public void setAvgVisMiles(Double avgVisMiles) {
        this.avgVisMiles = avgVisMiles;
    }

    /**
     *
     * @return
     */
    public int getAvgHumidity() {
        return avgHumidity;
    }

    /**
     *
     * @param avgHumidity
     */
    public void setAvgHumidity(int avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    /**
     *
     * @return
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     *
     * @param condition
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    /**
     *
     * @return
     */
    public Double getUv() {
        return uv;
    }

    /**
     *
     * @param uv
     */
    public void setUv(Double uv) {
        this.uv = uv;
    }
}
