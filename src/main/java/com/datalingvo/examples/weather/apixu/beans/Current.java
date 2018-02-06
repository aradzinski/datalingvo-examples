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
public class Current {
    @SerializedName("last_updated") private String lastUpdated;
    @SerializedName("last_updated_epoch") private int lastUpdatedEpoch;
    @SerializedName("temp_c") private Double tempC;
    @SerializedName("temp_f") private Double tempF;
    @SerializedName("feelslike_c") private Double feelsLikeC;
    @SerializedName("feelslike_f") private Double feelsLikeF;
    private Condition condition;
    @SerializedName("wind_mph") private Double windMph;
    @SerializedName("wind_kph") private Double windKph;
    @SerializedName("wind_dir") private String windDir;
    @SerializedName("wind_degree") private int windDegree;
    @SerializedName("pressure_mb") private Double pressureMb;
    @SerializedName("pressure_in") private Double pressureIn;
    @SerializedName("precip_mm") private Double precipMm;
    @SerializedName("precip_in") private Double precipIn;
    private int humidity;
    private int cloud;
    @SerializedName("is_day") private int isDay;

    /**
     *
     * @return
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     *
     * @param lastUpdated
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     *
     * @return
     */
    public int getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }

    /**
     *
     * @param lastUpdatedEpoch
     */
    public void setLastUpdatedEpoch(int lastUpdatedEpoch) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
    }

    /**
     *
     * @return
     */
    public Double getTempC() {
        return tempC;
    }

    /**
     *
     * @param tempC
     */
    public void setTempC(Double tempC) {
        this.tempC = tempC;
    }

    /**
     *
     * @return
     */
    public Double getTempF() {
        return tempF;
    }

    /**
     *
     * @param tempF
     */
    public void setTempF(Double tempF) {
        this.tempF = tempF;
    }

    /**
     *
     * @return
     */
    public Double getFeelsLikeC() {
        return feelsLikeC;
    }

    /**
     *
     * @param feelsLikeC
     */
    public void setFeelsLikeC(Double feelsLikeC) {
        this.feelsLikeC = feelsLikeC;
    }

    /**
     *
     * @return
     */
    public Double getFeelsLikeF() {
        return feelsLikeF;
    }

    /**
     *
     * @param feelsLikeF
     */
    public void setFeelsLikeF(Double feelsLikeF) {
        this.feelsLikeF = feelsLikeF;
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
    public Double getWindMph() {
        return windMph;
    }

    /**
     *
     * @param windMph
     */
    public void setWindMph(Double windMph) {
        this.windMph = windMph;
    }

    /**
     *
     * @return
     */
    public Double getWindKph() {
        return windKph;
    }

    /**
     *
     * @param windKph
     */
    public void setWindKph(Double windKph) {
        this.windKph = windKph;
    }

    /**
     *
     * @return
     */
    public String getWindDir() {
        return windDir;
    }

    /**
     *
     * @param windDir
     */
    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    /**
     *
     * @return
     */
    public int getWindDegree() {
        return windDegree;
    }

    /**
     *
     * @param windDegree
     */
    public void setWindDegree(int windDegree) {
        this.windDegree = windDegree;
    }

    /**
     *
     * @return
     */
    public Double getPressureMb() {
        return pressureMb;
    }

    /**
     *
     * @param pressureMb
     */
    public void setPressureMb(Double pressureMb) {
        this.pressureMb = pressureMb;
    }

    /**
     *
     * @return
     */
    public Double getPressureIn() {
        return pressureIn;
    }

    /**
     *
     * @param pressureIn
     */
    public void setPressureIn(Double pressureIn) {
        this.pressureIn = pressureIn;
    }

    /**
     *
     * @return
     */
    public Double getPrecipMm() {
        return precipMm;
    }

    /**
     *
     * @param precipMm
     */
    public void setPrecipMm(Double precipMm) {
        this.precipMm = precipMm;
    }

    /**
     *
     * @return
     */
    public Double getPrecipIn() {
        return precipIn;
    }

    /**
     *
     * @param precipIn
     */
    public void setPrecipIn(Double precipIn) {
        this.precipIn = precipIn;
    }

    /**
     *
     * @return
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    /**
     *
     * @return
     */
    public int getCloud() {
        return cloud;
    }

    /**
     *
     * @param cloud
     */
    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    /**
     *
     * @return
     */
    public int getIsDay() {
        return isDay;
    }

    /**
     *
     * @param isDay
     */
    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }
}
