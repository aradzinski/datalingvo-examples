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

import com.datalingvo.DLException;
import com.datalingvo.examples.misc.geo.cities.CitiesDataProvider;
import com.datalingvo.examples.misc.geo.cities.City;
import com.datalingvo.examples.misc.geo.cities.CityData;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.AND;
import com.datalingvo.mdllib.DLTokenSolver.CONV_INTENT;
import com.datalingvo.mdllib.DLTokenSolver.NON_CONV_INTENT;
import com.datalingvo.mdllib.DLTokenSolver.TERM;
import com.datalingvo.mdllib.tools.builder.DLModelBuilder;
import org.apache.commons.lang3.text.WordUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.time.format.FormatStyle.MEDIUM;

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
    private static DLQueryResult formatResult(String city, String cntry, String tmz, double lat, double lon) {
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
     * Callback on local time intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onLocalMatch(DLTokenSolverContext ctx) {
        DLMetadata md = ctx.getSentence().getMetadata();

        // Get local geo data from sentence metadata defaulting to
        // Silicon Valley location in case we are missing that info.
        return formatResult(
            md.getStringOrElse("CITY", ""),
            md.getStringOrElse("COUNTRY_NAME", "United States"),
            md.getStringOrElse("TMZ_NAME", "America/Los_Angeles"),
            md.getDoubleOrElse("LATITUDE", 37.7749),
            md.getDoubleOrElse("LONGITUDE", 122.4194)
        );
    }

    /**
     * Callback on remote time intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onRemoteMatch(DLTokenSolverContext ctx) {
        // 'dl:geo' is mandatory.
        // Only one 'dl:geo' token is allowed, so we don't have to check for it.
        DLToken geoTok = ctx.getTokens().get(1).get(0);

        // GEO token metadata.
        DLMetadata meta = geoTok.getMetadata();

        // 'GEO_COUNTRY' and 'GEO_CITY' is mandatory metadata of 'dl:geo' token.
        String city = meta.getString("GEO_CITY");
        String cntry = meta.getString("GEO_COUNTRY");

        CityData data = citiesData.get(new City(city, cntry));

        if (data != null)
            return formatResult(city, cntry, data.getTimezone(), data.getLatitude(), data.getLongitude());
        else
            // We don't have timezone mapping for parsed GEO location.
            // Instead of defaulting to a local time - we reject with a specific error message for cleaner UX.
            throw new DLRejection(String.format("No timezone mapping for %s, %s.", city, cntry));
    }

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    TimeProvider() throws DLException {
        String path = DLModelBuilder.classPathFile("time_model.json");

        DLTokenSolver solver = new DLTokenSolver(
            "time-solver",
            // Allow for multi matches. If two intents match - pick any random one...
            true,
            // Custom not-found function with tailored rejection message.
            () -> { throw new DLRejection("Are you asking about time?<br>Check spelling and city name too."); }
        );

        // NOTE:
        // We need to have two intents vs. one intent with an optional GEO. The reason is that
        // first intent isn't using the conversation context to make sure we can always ask
        // for local time **no matter** what was asked before... Note that non-conversational
        // intent always "wins" over the conversational one given otherwise equal weight because
        // non-conversational intent is more specific (i.e. using only the most current user input).

        // Check for exactly one 'x:time' token **without** looking into conversation context.
        // That's an indication of asking for local time only.
        solver.addIntent(
            new NON_CONV_INTENT("id == x:time", 1, 1), // Term idx=0.
            this::onLocalMatch
        );

        // Check for exactly one 'x:time' and one 'dl:geo' CITY token **including** conversation
        // context. This is always remote time.
        solver.addIntent(
            new CONV_INTENT(
                new TERM("id == x:time", 1, 1), // Term idx=0.
                new TERM(
                    new AND(                    // Term idx=1.
                        "id == dl:geo",
                        // GEO locations can only be city (we can't get time for country or region or continent).
                        "~GEO_KIND == CITY"
                    ), 1, 1
                )
            ),
            this::onRemoteMatch
        );

        // Initialize adapter.
        setup(DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build());
    }
}
