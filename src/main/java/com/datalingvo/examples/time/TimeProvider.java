/*
 * 2013-2015 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.time;

import com.datalingvo.*;
import com.datalingvo.examples.time.cities.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.*;
import com.datalingvo.mdllib.tools.builder.*;
import org.apache.commons.lang3.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import static java.time.format.FormatStyle.*;

/**
 * Time example model provider.
 */
@DLActiveModelProvider
public class TimeProvider extends DLSingleModelProviderAdapter {
    static private final DateTimeFormatter FMT = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);

    static private Map<City, CityData> citiesData = CitiesDataProvider.get();

    /**
     * Gets query result.
     *
     * @param city Detected city.
     * @param cntry Detected country.
     * @param tmz Timezone ID.
     * @param lat City latitude.
     * @param lon City longitude.
     */
    private static DLQueryResult remoteResult(String city, String cntry, String tmz, double lat, double lon) {
        String cityFmt = WordUtils.capitalize(city);
        String cntrFmt = WordUtils.capitalize(cntry);

        String css1 = "style='display: inline-block; min-width: 100px'";
        String css2 = "style='font-weight: 200'";

        return DLQueryResult.jsonMultipart(
            DLQueryResult.html(
                String.format(
                    "<b %s>Time:</b> <span style='color: #F1C40F'>%s</span><br/>" +
                    "<b %s>City:</b> <span %s>%s</span><br/>" +
                    "<b %s>Country:</b> <span %s>%s</span><br/>" +
                    "<b %s>Timezone:</b> <span %s>%s</span><br/>" +
                    "<b %s>Local Time:</b> <span %s>%s</span>",
                    css1, ZonedDateTime.now(ZoneId.of(tmz)).format(FMT),
                    css1, css2, cityFmt,
                    css1, css2, cntrFmt,
                    css1, css2, tmz,
                    css1, css2, ZonedDateTime.now().format(FMT)
                )
            ),
            DLQueryResult.jsonGmap(
                String.format(
                    "{" +
                        "\"cssStyle\": {" +
                            "\"width\": \"600px\", " +
                            "\"height\": \"300px\"" +
                        "}," +
                        "\"gmap\": {" +
                            "\"center\": \"%f,%f\"," +
                            "\"zoom\": 4," +
                            "\"scale\": 2," +
                            "\"size\": \"600x300\", " +
                            "\"maptype\": \"terrain\", " +
                            "\"markers\": \"color:red|%f,%f\"" +
                        "}" +
                    "}",
                    lat,
                    lon,
                    lat,
                    lon
                )
            )
        );
    }

    /**
     * Return local time result.
     *
     * @param sen Sentence.
     * @return Query result.
     */
    private DLQueryResult localResult(DLSentence sen) {
        DLMetadata md = sen.getMetadata();

        return remoteResult(
            md.getStringOrElse("CITY", ""),
            md.getStringOrElse("COUNTRY_CODE", "US"),
            md.getStringOrElse("TMZ_NAME", "America/Los_Angeles"),
            md.getDoubleOrElse("LATITUDE", 37.7749),
            md.getDoubleOrElse("LONGITUDE", 122.4194)
        );
    }

    /**
     *
     * @param ctx
     * @return
     */
    private DLQueryResult onLocalMatch(DLTokenSolverContext ctx) {
        return localResult(ctx.getSentence());
    }

    /**
     *
     * @param ctx
     * @return
     */
    private DLQueryResult onRemoteMatch(DLTokenSolverContext ctx) {
        if (ctx.getTokens().get(1).isEmpty()) // GEO is optional.
            // Use user location by default - just return the local or PST time.
            return localResult(ctx.getSentence());
        else {
            // Only one GEO token is allowed per model metadata.
            DLToken geoTok = ctx.getTokens().get(1).get(0);

            DLMetadata meta = geoTok.getMetadata();

            // 'GEO_COUNTRY' and 'GEO_CITY' is mandatory metadata of 'dl:geo' token.
            String city = meta.getString("GEO_CITY");
            String cntry = meta.getString("GEO_COUNTRY");

            CityData data = citiesData.get(new City(city, cntry));

            if (data != null)
                return remoteResult(city, cntry, data.getTimezone(), data.getLatitude(), data.getLongitude());
            else
                // We don't have timezone mapping for parsed GEO location.
                throw new DLRejection(String.format("No timezone mapping for %s, %s.", city, cntry));
        }
    }

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    public TimeProvider() throws DLException {
        String path = DLModelBuilder.classPathFile("time_model.json");

        DLTokenSolver solver = new DLTokenSolver(
            "time-solver",
            false,
            __ -> { throw new DLRejection("Seems unrelated (check city name)."); }
        );

        // Check for exactly one 'x:time' token and zero 'dl:geo' without looking into conversation.
        // That's indication of asking for local time.
        solver.addIntent(
            new INTENT(false, 3,
                new TERM(new RULE("id", "==", "x:time"), 1, 1), // Index 0.
                new TERM(new RULE("id", "==", "dl:geo"), 0, 0)  // Index 1.
            ),
            this::onLocalMatch
        );

        // Check for exactly one 'x:time' and one optional 'dl:geo' CITY token including conversation
        // context. That can be either local or remote time.
        solver.addIntent(
            new INTENT(true, 3,
                new TERM(new RULE("id", "==", "x:time"), 1, 1), // Index 0.
                new TERM(new AND(                                // Index 1.
                    new RULE("id", "==", "dl:geo"),
                    new RULE("~GEO_KIND", "==", "CITY")
                ), 0, 1)
            ),
            this::onRemoteMatch
        );

        DLModel model =
            DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build();

        // Initialize adapter.
        setup("dl.time.ex", model);
    }
}
