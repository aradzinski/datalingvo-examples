/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.weather.apixu;

import com.datalingvo.*;
import com.datalingvo.examples.weather.apixu.beans.*;
import com.datalingvo.mdllib.*;
import com.google.gson.*;
import org.apache.commons.lang3.tuple.*;
import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

import static java.time.temporal.ChronoUnit.*;

/**
 * Weather data provider using https://www.apixu.com service.
 *
 * It provides weather for two formats:
 * 1. Current weather. Maximum detailed weather info for current datetime.
 * 2. Weather data by days for periods (note: forecast and history data have days period restrictions.)
 *
 * Note:
 * 1. `hours` data skipped to simplify this example.
 * 2. If provider access plan is Free or Silver (https://www.apixu.com/pricing.aspx)
 *    than history data requests returns only one (first) history day.
 *    Example: requests `give me weather from 2 days ago to today` returns history data for one day
 *    (the day before yesterday). To avoid this restriction please register and choose plan above
 *    `Gold` and use your own token.
 */
public class ApixuWeatherService {
    /** */
    private static final int SUPPORTED_DAYS_BACK = 30;

    /** */
    private static final int SUPPORTED_DAYS_FORWARD = 10;

    /** */
    private static final Gson gson = new Gson();

    /** */
    private final String apiKey;

    /**
     *
     * @param apiKey APIXU API key.
     */
    public ApixuWeatherService(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     *
     * @param respClass Response type.
     * @param geo Geo location.
     * @param params Parameters.
     * @throws DLException Thrown in case of any errors.
     */
    @SafeVarargs
    private final <T> T get(
        Class<T> respClass,
        String method,
        String geo,
        Pair<String, Object>... params
    ) throws DLRejection {
        String pLine =
            Stream.concat(
                Stream.of(Pair.of("key", apiKey), Pair.of("q", geo), Pair.of("lang", "en")),
                Arrays.stream(params)
            ).map(p -> {
                try {
                    return p.getKey() + '=' + URLEncoder.encode(p.getValue().toString(), "UTF8");
                }
                catch (UnsupportedEncodingException e) {
                    // Shouldn't be here.
                    throw new DLException("Invalid encoding.", e);
                }
            }).collect(Collectors.joining("&"));

        String url = "http://api.apixu.com/v1/" + method + ".json?" + pLine;

        try (InputStream in = new URL(url).openConnection().getInputStream()) {
            return gson.fromJson(new BufferedReader(new InputStreamReader(in)), respClass);
        }
        catch (IOException e) {
            e.printStackTrace(System.err);

            throw new DLRejection("Unable to answer due to weather data provider (APIXU) failure.");
        }
    }

    /**
     * Gets current weather conditions.
     *
     * @param geo Geo location.
     */
    public CurrentResponse getCurrentWeather(String geo) {
        return get(CurrentResponse.class, "current", geo);
    }

    /**
     * Gets weather conditions for the date range (past or future).
     *
     * @param geo Geo location.
     * @param range Date range.
     */
    public RangeResponse getWeather(String geo, Pair<LocalDate, LocalDate> range) {
        LocalDate from = range.getLeft();
        LocalDate to = range.getRight();

        LocalDate now = LocalDate.now();

        if (from.isBefore(now.minusDays(SUPPORTED_DAYS_BACK)))
            from = now.minusDays(SUPPORTED_DAYS_BACK);

        if (to.isAfter(now.plusDays(SUPPORTED_DAYS_FORWARD)))
            to = now.plusDays(SUPPORTED_DAYS_FORWARD);

        RangeResponse fullResp = new RangeResponse();

        if (from.isBefore(now)) {
            RangeResponse resp = get(
                RangeResponse.class,
                "history",
                geo,
                Pair.of("dt", from.format(DateTimeFormatter.ISO_DATE)),
                Pair.of("end_dt", to.format(DateTimeFormatter.ISO_DATE))
            );

            fullResp.setLocation(resp.getLocation());
            fullResp.setForecast(resp.getForecast());
        }

        if (to.isAfter(now)) {
            RangeResponse resp = get(
                RangeResponse.class,
                "forecast",
                geo,
                Pair.of("days", DAYS.between(now, to))
            );

            fullResp.setLocation(resp.getLocation());

            if (fullResp.getForecast() == null)
                fullResp.setForecast(resp.getForecast());
            else {
                DayInfo[] history = resp.getForecast().getForecastDay();
                DayInfo[] future = resp.getForecast().getForecastDay();

                DayInfo[] fullData = new DayInfo[history.length + future.length];

                System.arraycopy(history, 0, fullData, 0, history.length);
                System.arraycopy(future, 0, fullData, history.length, future.length);

                DaysList days = new DaysList();

                days.setForecastDay(fullData);

                fullResp.setForecast(days);
            }
        }

        return fullResp;
    }
}
