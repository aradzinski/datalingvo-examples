/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.lessons.lesson1;

import com.datalingvo.*;
import com.datalingvo.examples.lessons.utils.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.tools.builder.*;

/**
 * `Lesson 1` model provider.
 */
@DLActiveModelProvider
public class TimeProvider1 extends DLSingleModelProviderAdapter {
    private static final String MODEL_ID = "dl.time.ex";
    
    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    TimeProvider1() throws DLException {
        // Initialize adapter.
        setup(
            MODEL_ID,
            DLModelBuilder.newModel(MODEL_ID, "Time Example Model", "1.0").
                setQueryFunction(ctx -> DLQueryResult.text(LessonsUtils.now())).
                build()
        );
    }
}
