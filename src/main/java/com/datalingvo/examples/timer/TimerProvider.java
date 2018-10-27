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
import com.datalingvo.mdllib.intent.*;
import com.datalingvo.mdllib.intent.DLIntentSolver.*;
import com.datalingvo.mdllib.tools.builder.*;
import com.datalingvo.mdllib.utils.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import static com.datalingvo.mdllib.utils.DLTokenUtils.*;
import static java.time.temporal.ChronoUnit.MILLIS;

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
public class TimerProvider extends DLModelProviderAdapter {
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("'<b>'HH'</b>h' '<b>'mm'</b>m' '<b>'ss'</b>s'").withZone(ZoneId.systemDefault());
    
    private Timer timer = new Timer();
    
    TimerProvider() {
        // Add a wide-catch intent. Note that terms in the intent will be matched
        // in any order and this intent can match some unusual grammar input
        // like "2 secs and 3mins set the timer". For the sake of simplicity
        // we allow such idiosyncratic input.
        DLIntentSolver solver = new DLIntentSolver(null, () -> { throw new DLCuration(); });
        
        solver.addIntent(
            "timer|num{1+}",
            new NON_CONV_INTENT(
                new TERM("id == x:timer", 1, 1),
                new TERM(
                    new AND("id == dl:num", "~NUM_UNITTYPE == datetime", "~NUM_ISEQUALCONDITION == true"),
                    0,
                    7 // Supported numeric `datetime` unit types.
                )
            ),
            this::onMatch
        );
    
        setup(DLModelBuilder.newJsonModel(DLModelBuilder.classPathFile("timer_model.json")).
            setQueryFunction(solver::solve).build());
    }
    
    /**
     * Callback on intent match.
     *
     * @param ctx Token solver context.
     * @return Query result.
     */
    private DLQueryResult onMatch(DLIntentSolverContext ctx) {
        if (!ctx.isExactMatch())
            throw new DLRejection("Not exact match.");
        
        List<DLToken> nums = ctx.getIntentTokens().get(1);
    
        long unitsCnt = nums.stream().map(DLTokenUtils::getNumUnit).distinct().count();
        
        if (unitsCnt != nums.size())
            throw new DLRejection("Ambiguous units.");
    
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dt = now;
    
        for (DLToken num : nums) {
            String unit = getNumUnit(num);
    
            // Skips possible fractional to simplify.
            long v = (long)getNumFrom(num);
            
            if (v <= 0)
                throw new DLRejection("Value must be positive: " + unit);
    
            switch (unit) {
                case "second": { dt = dt.plusSeconds(v); break; }
                case "minute": { dt = dt.plusMinutes(v); break; }
                case "hour": { dt = dt.plusHours(v); break; }
                case "day": { dt = dt.plusDays(v); break; }
                case "week": { dt = dt.plusWeeks(v); break; }
                case "month": { dt = dt.plusMonths(v); break; }
                case "year": { dt = dt.plusYears(v); break; }
        
                default:
                    // It shouldn't be assert, because 'datetime' unit can be extended.
                    throw new DLRejection("Unsupported time unit: " + unit);
            }
        }
    
        long ms = now.until(dt, MILLIS);
        
        assert ms >= 0;
    
        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    System.out.println(
                        "BEEP BEEP BEEP for: " + ctx.getQueryContext().getSentence().getNormalizedText() + ""
                    );
                }
            },
            ms
        );
    
        return DLQueryResult.html("Timer set for: " + FMT.format(dt));
    }
}
