/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.timer;

import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.*;
import com.datalingvo.mdllib.tools.builder.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Timer example model provider.
 * <p>
 * This example provides a simple "alarm clock" interface where you can ask to set the timer
 * for a specific duration from now expressed in hours, minutes and/or seconds. You can say "ping me in 3 minutes",
 * "buzz me in an hour and 15 minutes", or "set my alarm for 30 secs". When the timers is up it will
 * simply print out "BEEP BEEP BEEP" in the probe console.
 * <p>
 * As an additional exercise you can quickly add support for settings the alarm to a specific
 * time (and not only for a duration) and can play with the way the system reacts when the timer is up.
 */
@DLActiveModelProvider
public class TimerProvider extends DLSingleModelProviderAdapter {
    private static final DateTimeFormatter FMT = DateTimeFormatter.
        ofPattern("'<code><b>'HH'</b>h' '<b>'mm'</b>m' '<b>'ss'</b>s</code>'").
        withZone(ZoneId.of("UTC"));
    
    private final Timer timer = new Timer();
    
    /**
     * Initializes model provider.
     */
    TimerProvider() {
        String modelPath = DLModelBuilder.classPathFile("timer_model.json");
        
        DLTokenSolver solver = new DLTokenSolver();

        // Add a wide-catch intent. Note that terms in the intent will be matched
        // in any order and this intent can match some unusual grammar input
        // like "2 secs and 3 mins set the timer". For the sake of simplicity
        // we allow such idiosyncratic input.
        solver.addIntent(
            new NON_CONV_INTENT(
                new TERM("id == x:timer", 1, 1),
                new TERM("id == x:hour", 0, 1),
                new TERM("id == x:minute", 0, 1),
                new TERM("id == x:second", 0, 1),
                new TERM(
                    new AND(
                        new RULE("id == dl:num"),
                        // We are looking for a simple numeric value.
                        new RULE("~NUM_TYPE == value"),
                        // This will ensure that every number matched will have associated
                        // token, i.e. no dangling numbers are allowed for a match. All dangling
                        // numbers will not be matched and will lead to rejection.
                        new RULE("~NUM_INDEXES != null")
                    ),
                    0,
                    3
                )
            ),
            this::onMatch
        );
        
        setup(DLModelBuilder.newJsonModel(modelPath).setQueryFunction(solver::solve).build());
    }
    
    /**
     * Finds numeric value.
     *
     * @param ctx Context.
     * @param fieldNumIdx Numeric field index.
     * @return Numeric value.
     * @param nums Numeric values collection.
     */
    private static int getValue(DLTokenSolverContext ctx, int fieldNumIdx, List<DLToken> nums) {
        List<DLToken> numFields = ctx.getIntentTokens().get(fieldNumIdx);
        
        // Either zero or one.
        assert numFields.size() <= 1;
        
        // If field not found its value is zero.
        if (numFields.isEmpty())
            return 0;
        else {
            DLToken numField = numFields.get(0);
            int numFieldIdx = numField.getTokenIndex();

            // Tries to find reference to given field.
            for (DLToken num : nums) {
                @SuppressWarnings("unchecked")
                List<Integer> idxs = (List<Integer>) num.getMetadata().get("NUM_INDEXES");

                if (idxs.contains(numFieldIdx))
                    return num.getMetadata().getInteger("NUM_VALUE");
            }

            // Gets default value (like 'an hour', 'a minute' or 'a second').
            return 1;
        }
    }
    
    /**
     * Callback on intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onMatch(DLTokenSolverContext ctx) {
        List<DLToken> nums = ctx.getIntentTokens().get(4);
    
        int ms =
            getValue(ctx, 1, nums) * 60 * 60 * 1000 + // Hours (index 1).
            getValue(ctx, 2, nums) * 60 * 1000 +      // Minutes (index 2).
            getValue(ctx, 3, nums) * 1000;            // Seconds (index 3).
        
        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    // Timer is up...
                    System.out.println("BEEP BEEP BEEP for: " + ctx.getSentence().getMetadata().getString("NORMTEXT"));
                }
            },
            ms
        );
        
        return DLQueryResult.html("Timer set for: " + FMT.format(Instant.ofEpochMilli(ms)));
    }
}
