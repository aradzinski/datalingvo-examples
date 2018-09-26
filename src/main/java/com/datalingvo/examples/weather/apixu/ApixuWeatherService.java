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

import com.datalingvo.DLException;
import com.datalingvo.examples.weather.apixu.beans.CurrentResponse;
import com.datalingvo.examples.weather.apixu.beans.DayInfo;
import com.datalingvo.examples.weather.apixu.beans.DaysList;
import com.datalingvo.examples.weather.apixu.beans.RangeResponse;
import com.datalingvo.mdllib.DLRejection;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Weather data provider using https://www.apixu.com service.
 * <p>
 * It provides weather for two formats:
 * 1. Current weather. Maximum detailed weather info for current datetime.
 * 2. Weather data by days for periods (note: forecast and history data have days period restrictions.)
 * </p>
 * Note:
 * 1. `hours` data skipped to simplify this example.
 * 2. If provider access plan is Free or Silver (https://www.apixu.com/pricing.aspx)
 *    than history data requests returns only one (first) history day.
 *    Example: requests `give me weather from 2 days ago to today` returns history data for one day
 *    (the day before yesterday). To avoid this restriction please register and choose plan above
 *    `Gold` and use your own token.
 */
public class ApixuWeatherService {
    // Plan restrictions.
    // https://www.apixu.com/pricing.aspx
    private static final int SUPPORTED_DAYS_BACK = 7;
    private static final int SUPPORTED_DAYS_FORWARD = 7;

    private static final Gson gson = new Gson();
    private final String apiKey;

    /**
     * Creates service with given APIXU key.
     *
     * @param apiKey APIXU API key.
     */
    public ApixuWeatherService(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Gets whether data.
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

        // Ack.
        System.out.println("APIXU REST: " + url);

        try {
            URLConnection conn = new URL(url).openConnection();

            try (InputStream in = conn.getInputStream()) {
                String enc = conn.getContentEncoding();

                // Default 'unzipped'. APIXU changes format too often, can be changed again.
                InputStream stream = enc != null && enc.equals("gzip") ? new GZIPInputStream(in) : in;

                return gson.fromJson(new BufferedReader(new InputStreamReader(stream)), respClass);
            }
        }
        // IO, encoding errors.
        catch (Exception e) {
            e.printStackTrace(System.err);

            throw new DLRejection("Unable to answer due to weather data provider (APIXU) error.");
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
     * @throws ApixuPeriodException TODO:
     */
    public RangeResponse getWeather(String geo, Pair<LocalDate, LocalDate> range) throws ApixuPeriodException {
        LocalDate from = range.getLeft();
        LocalDate to = range.getRight();

        LocalDate now = LocalDate.now();

        if (from.isBefore(now.minusDays(SUPPORTED_DAYS_BACK)))
            throw new ApixuPeriodException("Date is out of supported range.<br>Maximum days back is " +
                SUPPORTED_DAYS_BACK + ".");

        if (to.isAfter(now.plusDays(SUPPORTED_DAYS_FORWARD)))
            throw new ApixuPeriodException("Date is out of supported range.<br>Maximum days forward is " +
                SUPPORTED_DAYS_FORWARD + ".");

        RangeResponse fullResp = new RangeResponse();

        if (from.isBefore(now)) {
            LocalDate end = to.isBefore(now) ? to : now;
    
            RangeResponse resp = get(
                RangeResponse.class,
                "history",
                geo,
                Pair.of("dt", from.format(DateTimeFormatter.ISO_DATE)),
                Pair.of("end_dt", end.format(DateTimeFormatter.ISO_DATE))
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
            
            int shift = (int)now.until(from, DAYS);
            
            if (shift > 0) {
                DaysList list = resp.getForecast();
    
                list.setForecastDay(Arrays.copyOfRange(list.getForecastDay(), shift, list.getForecastDay().length));
            }

            fullResp.setLocation(resp.getLocation());

            if (fullResp.getForecast() == null)
                fullResp.setForecast(resp.getForecast());
            else {
                DayInfo[] history = fullResp.getForecast().getForecastDay();
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
