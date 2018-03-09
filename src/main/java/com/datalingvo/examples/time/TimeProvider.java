/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.time;

import com.datalingvo.*;
import com.datalingvo.examples.misc.geo.cities.*;
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
 * <p>
 * This example answers the questions about current time, either local or at some city.
 * It provides HTML response with time and timezone information as well as Google map
 * of the location (default or provided by the user).
 */
@DLActiveModelProvider
public class TimeProvider extends DLSingleModelProviderAdapter {
    // Medium data formatter.
    static private final DateTimeFormatter FMT = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);

    // Map of cities and their geo and timezone information.
    static private Map<City, CityData> citiesData = CitiesDataProvider.get();

    /**
     * Gets multipart query result.
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

        // Multipart result consists of HTML fragment and Google static map fragment.
        return DLQueryResult.jsonMultipart(
            // HTML block with CSS formatting.
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
            // Google static map fragment.
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

        // Get local geo data from sentence metadata defaulting to
        // Silicon Valley location in case we are missing that info in the
        // sentence metadata.
        return remoteResult(
            md.getStringOrElse("CITY", ""),
            md.getStringOrElse("COUNTRY_CODE", "US"),
            md.getStringOrElse("TMZ_NAME", "America/Los_Angeles"),
            md.getDoubleOrElse("LATITUDE", 37.7749),
            md.getDoubleOrElse("LONGITUDE", 122.4194)
        );
    }

    /**
     * Callback on local time intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onLocalMatch(DLTokenSolverContext ctx) {
        return localResult(ctx.getSentence());
    }

    /**
     * Callback on remote time intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onRemoteMatch(DLTokenSolverContext ctx) {
        // GEO is optional.
        // If we don't have geo data - simply assume local time request.
        if (ctx.getTokens().get(1).isEmpty())
            // Use user location by default for cleaner UX - just return the local or PST time.
            return localResult(ctx.getSentence());
        else {
            // Only one GEO token is allowed per model metadata, so we don't have to check for it.
            DLToken geoTok = ctx.getTokens().get(1).get(0);

            // GEO token metadata.
            DLMetadata meta = geoTok.getMetadata();

            // 'GEO_COUNTRY' and 'GEO_CITY' is mandatory metadata of 'dl:geo' token.
            String city = meta.getString("GEO_CITY");
            String cntry = meta.getString("GEO_COUNTRY");

            CityData data = citiesData.get(new City(city, cntry));

            if (data != null)
                return remoteResult(city, cntry, data.getTimezone(), data.getLatitude(), data.getLongitude());
            else
                // We don't have timezone mapping for parsed GEO location.
                // Instead of defaulting to a local time - we reject with a specific error message for cleaner UX.
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
            // Allow for multi matches. If two intents match - pick any random one...
            true,
            // Custom not-found function with tailored rejection message.
            () -> { throw new DLRejection("Seems confusing - are you asking about time?</br>Check spelling and city name too."); }
        );

        // Check for exactly one 'x:time' token **without** looking into conversation context.
        // That's an indication of asking for local time.
        solver.addIntent(
            new INTENT(
                false, // Don't include conversation context when matching.
                true, // Only an exact match.
                3, // Maximum 3 free words.
                new TERM(new RULE("id", "==", "x:time"), 1, 1) // Term idx=0.
            ),
            this::onLocalMatch
        );

        // Check for exactly one 'x:time' and one optional 'dl:geo' CITY token **including** conversation
        // context. That can be either local or remote time.
        solver.addIntent(
            new INTENT(
                /* Default is to include conversation context. */
                /* Default is to do an exact match. */
                3, // Maximum 3 free words.
                new TERM(new RULE("id", "==", "x:time"), 1, 1), // Term idx=0.
                new TERM(new AND(                               // Term idx=1.
                    new RULE("id", "==", "dl:geo"),
                    // GEO locations can only be city (we can't get time for country or region or continent).
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
