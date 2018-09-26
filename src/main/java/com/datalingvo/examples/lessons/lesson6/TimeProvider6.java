/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.lessons.lesson6;

import com.datalingvo.*;
import com.datalingvo.examples.misc.geo.cities.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.intent.*;
import com.datalingvo.mdllib.intent.DLIntentSolver.*;
import com.datalingvo.mdllib.tools.builder.*;
import org.apache.commons.lang3.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import static com.datalingvo.mdllib.utils.DLTokenUtils.*;
import static java.time.format.FormatStyle.*;

/**
 * `Lesson 6` model provider.
 */
@DLActiveModelProvider
public class TimeProvider6 extends DLSingleModelProviderAdapter {
    static private Map<City, CityData> citiesData = CitiesDataProvider.get();

    // Medium data formatter.
    static private final DateTimeFormatter FMT = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);

    // CSS formatting styles.
    static private final String CSS1= "style='display: inline-block; min-width: 100px'";
    static private final String CSS2 = "style='font-weight: 200'";

    /**
     * Gets formatted query result.
     *
     * @param city Detected city.
     * @param cntry Detected country.
     * @param tmz Timezone ID.
     * @param lat City latitude.
     * @param lon City longitude.
     */
    private static DLQueryResult formatResult(
        String city,
        String cntry,
        String tmz,
        double lat,
        double lon
    ) {
        String cityFmt = WordUtils.capitalize(city);
        String cntrFmt = WordUtils.capitalize(cntry);

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
                    CSS1, ZonedDateTime.now(ZoneId.of(tmz)).format(FMT),
                    CSS1, CSS2, cityFmt,
                    CSS1, CSS2, cntrFmt,
                    CSS1, CSS2, tmz,
                    CSS1, CSS2, ZonedDateTime.now().format(FMT)
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
     * Callback on intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onMatch(DLIntentSolverContext ctx) {
        // 'dl:geo' is optional here.
        if (ctx.getIntentTokens().get(1).isEmpty()) {
            DLSentence sen = ctx.getQueryContext().getSentence();

            // Get local geo data from sentence metadata defaulting to
            // Silicon Valley location in case we are missing that info.
            return formatResult(
                sen.getCityName().orElse(""),
                sen.getCountryName().orElse("United States"),
                sen.getTimezoneName().orElse("America/Los_Angeles"),
                sen.getLatitude().orElse(37.7749),
                sen.getLongitude().orElse(122.4194)
            );
        }

        // Note that only one 'dl:geo' token is allowed per model metadata.
        DLToken geoTok = ctx.getIntentTokens().get(1).get(0);

        // Country and city are is mandatory metadata of 'dl:geo' token.
        String city = getGeoCity(geoTok);
        String cntry = getGeoCountry(geoTok);

        CityData data = citiesData.get(new City(city, cntry));

        // We don't have timezone mapping for parsed GEO location.
        if (data == null)
            throw new DLRejection(String.format("No timezone mapping for %s, %s.", city, cntry));

        return formatResult(
            city,
            cntry,
            data.getTimezone(),
            data.getLatitude(),
            data.getLongitude()
        );
    }

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    TimeProvider6() throws DLException {
        String path = DLModelBuilder.classPathFile("lessons/time_model6.json");

        DLIntentSolver solver =
            new DLIntentSolver(
                "time-solver",
                () -> {
                    // Custom not-found function with tailored rejection message.
                    throw new DLRejection("I can't understand your question.");
                }
            );

        // Check for exactly one 'x:time' token. 'dl:geo' is optional.
        solver.addIntent(
            "time|city?",
            new NON_CONV_INTENT(
                new TERM("id == x:time", 1, 1),
                new TERM(
                    new AND(
                        "id == dl:geo",
                        "~GEO_KIND == CITY"
                    ),
                    0,
                    1
                )
            ),
            this::onMatch
        );

        DLModel model = DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build();

        // Initialize adapter.
        setup(model);
    }
}
