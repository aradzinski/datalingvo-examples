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

import com.datalingvo.DLException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * City-timezone map provider.
 */
public class CitiesDataProvider {
    private static final String DATA_FILE = "cities_timezones.txt";

    /**
     * Creates and returns cities timezone map for all cities with a population > 15000 or capitals.
     *
     * @return Cities timezone map.
     */
    public static Map<City, CityData> get() throws DLException {
        URL url = CitiesDataProvider.class.getClassLoader().getResource(DATA_FILE);

        if (url == null)
            throw new IllegalArgumentException("File not found: " + DATA_FILE);

        System.out.println();

        try {
            return Files.
                lines(Paths.get(url.getFile())).
                filter(p -> !p.startsWith("#")).
                map(String::trim).
                filter(p -> !p.isEmpty()).
                map(p -> p.split("\t")).
                map(p -> Arrays.stream(p).map(String::trim).toArray(String[]::new)).
                map(arr ->
                    Pair.of(
                        new City(arr[0], arr[1]),
                        new CityData(arr[2], Double.parseDouble(arr[3]), Double.parseDouble(arr[4])))
                ).
                collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        }
        catch (IOException e) {
            throw new DLException("Failed to read data file: " + DATA_FILE, e);
        }
    }
}
