/*
 * 2014-2015 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.alexa;

import com.datalingvo.DLException;
import com.datalingvo.examples.misc.geo.cities.CitiesDataProvider;
import com.datalingvo.examples.misc.geo.cities.City;
import com.datalingvo.examples.misc.geo.cities.CityData;
import com.datalingvo.mdllib.DLActiveModelProvider;
import com.datalingvo.mdllib.DLMetadata;
import com.datalingvo.mdllib.DLModel;
import com.datalingvo.mdllib.DLQueryResult;
import com.datalingvo.mdllib.DLRejection;
import com.datalingvo.mdllib.DLSingleModelProviderAdapter;
import com.datalingvo.mdllib.DLToken;
import com.datalingvo.mdllib.DLTokenSolver;
import com.datalingvo.mdllib.DLTokenSolver.AND;
import com.datalingvo.mdllib.DLTokenSolver.INTENT;
import com.datalingvo.mdllib.DLTokenSolver.RULE;
import com.datalingvo.mdllib.DLTokenSolver.TERM;
import com.datalingvo.mdllib.DLTokenSolverContext;
import com.datalingvo.mdllib.tools.builder.DLModelBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Map;

/**
 * Alexa time example model provider.
 */
@DLActiveModelProvider
public class AlexaTimeProvider extends DLSingleModelProviderAdapter {
    // It formats time to string like `11 hours 28 minutes 27 February`
    static private final DateTimeFormatter FMT =
        new DateTimeFormatterBuilder().
        appendValue(ChronoField.HOUR_OF_DAY).
        appendLiteral(" hours ").
        appendText(ChronoField.MINUTE_OF_HOUR).
        appendLiteral(" minutes").
        appendLiteral(' ').
        appendValue(ChronoField.DAY_OF_MONTH).
        appendLiteral(' ').
        appendPattern("MMMM").
        toFormatter();
    
    static private Map<City, CityData> citiesData = CitiesDataProvider.get();

    /**
     *
     * @param ctx
     * @return
     */
    private DLQueryResult onMatch(DLTokenSolverContext ctx) {
        // Only one GEO token is allowed per model metadata.
        DLToken geoTok = ctx.getTokens().get(1).get(0);

        DLMetadata meta = geoTok.getMetadata();

        // 'GEO_COUNTRY' and 'GEO_CITY' is mandatory metadata of 'dl:geo' token.
        String city = meta.getString("GEO_CITY");
        String cntry = meta.getString("GEO_COUNTRY");

        CityData data = citiesData.get(new City(city, cntry));

        if (data != null)
            return DLQueryResult.text(
                String.format("%s time is %s", city, ZonedDateTime.now(ZoneId.of(data.getTimezone())).format(FMT))
            );
        
        // We don't have timezone mapping for parsed GEO location.
        throw new DLRejection(String.format("No timezone mapping for %s, %s.", city, cntry));
    }

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    public AlexaTimeProvider() throws DLException {
        String path = DLModelBuilder.classPathFile("alexa_time_model.json");

        DLTokenSolver solver = new DLTokenSolver(
            "alexa-time-solver",
            true, // Allow for multi match. If two intents match - pick any random one...
            () -> { throw new DLRejection("Seems confusing. I can answer about time in some city."); }
        );

        // Check for exactly one 'x:time' and one optional 'dl:geo' CITY token including conversation
        // context.
        solver.addIntent(
            new INTENT(3,
                new TERM(new RULE("id", "==", "x:time"), 1, 1), // Index 0.
                new TERM(new AND(                               // Index 1.
                    new RULE("id", "==", "dl:geo"),
                    new RULE("~GEO_KIND", "==", "CITY")
                ), 0, 1)
            ),
            this::onMatch
        );

        DLModel model =
            DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build();

        // Initialize adapter.
        setup("dl.alexa.time.ex", model);
    }
}
