/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.lessons.lesson5;

import com.datalingvo.*;
import com.datalingvo.examples.lessons.utils.*;
import com.datalingvo.examples.misc.geo.cities.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.*;
import com.datalingvo.mdllib.tools.builder.*;
import java.time.*;
import java.util.*;

/**
 * `Lesson 5` model provider.
 */
@DLActiveModelProvider
public class TimeProvider5 extends DLSingleModelProviderAdapter {
    static private Map<City, CityData> citiesData = CitiesDataProvider.get();

    /**
     * Gets formatted query result.
     *
     * @param tmz Timezone ID.
     */
    private static DLQueryResult formatResult(String tmz) {
        return DLQueryResult.text(LessonsUtils.now(ZoneId.of(tmz)));
    }
    
    /**
     * Callback on intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onMatch(DLTokenSolverContext ctx) {
        // 'dl:geo' is optional here.
        if (ctx.getTokens().get(1).isEmpty())
            // Get user's timezone from sentence metadata.
            return formatResult(ctx.getSentence().getMetadata().getStringOrElse("TMZ_NAME", "America/Los_Angeles"));

        // Note that only one 'dl:geo' token is allowed per model metadata.
        DLToken geoTok = ctx.getTokens().get(1).get(0);

        DLMetadata geoMeta = geoTok.getMetadata();

        String city = geoMeta.getString("GEO_CITY");
        String cntry = geoMeta.getString("GEO_COUNTRY");

        CityData data = citiesData.get(new City(city, cntry));

        // We don't have timezone mapping for parsed GEO location.
        if (data == null)
            throw new DLRejection(String.format("No timezone mapping for %s, %s.", city, cntry));

        return formatResult(data.getTimezone());
    }

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    TimeProvider5() throws DLException {
        String path = DLModelBuilder.classPathFile("lessons/time_model5.json");

        DLTokenSolver solver =
            new DLTokenSolver(
                "time-solver",
                true,
                () -> {
                    // Custom not-found function with tailored rejection message.
                    throw new DLRejection("I can't understand your question.");
                }
            );

        // Check for exactly one 'x:time' token. 'dl:geo' is optional.
        solver.addIntent(
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
