/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.lessons.lesson3;

import com.datalingvo.DLException;
import com.datalingvo.examples.lessons.utils.LessonsUtils;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.DLTokenSolver.NON_CONV_INTENT;
import com.datalingvo.mdllib.tools.builder.DLModelBuilder;

/**
 * `Lesson 3` model provider.
 */
@DLActiveModelProvider
public class TimeProvider3 extends DLSingleModelProviderAdapter {
    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    TimeProvider3() throws DLException {
        DLTokenSolver solver =
            new DLTokenSolver(
                "time-solver",
                true,
                () -> {
                    // Custom not-found function with tailored rejection message.
                    throw new DLRejection("I can't understand your question.");
                }
        );
        
        solver.addIntent(
            new NON_CONV_INTENT("id == x:time", 1, 1),
            ctx -> DLQueryResult.text(LessonsUtils.now())
        );

        String path = DLModelBuilder.classPathFile("lessons/time_model3.json");
        DLModel model = DLModelBuilder.newJsonModel(path).setQueryFunction(solver::solve).build();
    
        // Initialize adapter.
        setup(model);
    }
}
