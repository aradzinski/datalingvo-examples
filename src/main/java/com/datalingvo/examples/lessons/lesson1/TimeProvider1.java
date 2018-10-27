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

import com.datalingvo.DLException;
import com.datalingvo.examples.lessons.utils.LessonsUtils;
import com.datalingvo.mdllib.DLActiveModelProvider;
import com.datalingvo.mdllib.DLQueryResult;
import com.datalingvo.mdllib.DLModelProviderAdapter;
import com.datalingvo.mdllib.tools.builder.DLModelBuilder;

/**
 * `Lesson 1` model provider.
 */
@DLActiveModelProvider
public class TimeProvider1 extends DLModelProviderAdapter {
    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    TimeProvider1() throws DLException {
        // Initialize adapter.
        setup(
            DLModelBuilder.newModel("dl.time.ex", "Time Example Model", "1.0").
                setQueryFunction(ctx -> DLQueryResult.text(LessonsUtils.now())).
                build()
        );
    }
}
