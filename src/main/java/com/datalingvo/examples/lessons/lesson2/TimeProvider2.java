/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.lessons.lesson2;

import com.datalingvo.*;
import com.datalingvo.examples.lessons.utils.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.intent.*;
import com.datalingvo.mdllib.intent.DLIntentSolver.*;
import com.datalingvo.mdllib.tools.builder.*;

/**
 * `Lesson 2` model provider.
 */
@DLActiveModelProvider
public class TimeProvider2 extends DLSingleModelProviderAdapter {
    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    TimeProvider2() throws DLException {
        DLElement dateTimeElem =
            DLElementBuilder.newElement("x:time").addSynonyms("time", "date").build();
    
        DLIntentSolver solver =
            new DLIntentSolver(
                "time-solver",
                () -> {
                    // Custom not-found function with tailored rejection message.
                    throw new DLRejection("I can't understand your question.");
                }
            );
        
        solver.addIntent(
            new INTENT(false, false, new DLIntentSolver.TERM("id == x:time", 1, 1)),
            ctx -> DLQueryResult.text(LessonsUtils.now())
        );
    
        // Initialize adapter.
        setup(
            DLModelBuilder.
                newModel("dl.time.ex", "Time Example Model", "1.0").
                addElement(dateTimeElem).
                setQueryFunction(solver::solve).
                build()
        );
    }
}
