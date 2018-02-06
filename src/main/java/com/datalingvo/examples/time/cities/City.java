/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.time.cities;

/**
 * City data object.
 */
public class City {
    private String name;
    private String country;

    /**
     * Creates new city object.
     *
     * @param name City name.
     * @param country City country.
     */
    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    /**
     * Gets city name.
     *
     * @return City name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets city country.
     *
     * @return City country.
     */
    public String getCountry() {
        return country;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (name != null ? !name.equals(city.name) : city.name != null) return false;

        return country != null ? country.equals(city.country) : city.country == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;

        result = 31 * result + (country != null ? country.hashCode() : 0);

        return result;
    }
}
