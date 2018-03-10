/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.helloworld;

import com.datalingvo.*;
import com.datalingvo.mdllib.*;
import com.datalingvo.mdllib.tools.builder.*;

/**
 * Hello World example model provider.
 * <p>
 * This example simply responds with 'Hello World!' on any user input. This is the simplest
 * user model that can be defined.
 */
@DLActiveModelProvider
public class HelloWorldProvider extends DLSingleModelProviderAdapter {
    // Any immutable user defined ID.
    private static final String MODEL_ID = "dl.helloworld.ex";

    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    public HelloWorldProvider() throws DLException {
        // Initialize adapter.
        setup(
            MODEL_ID,
            // Minimally defined model...
            DLModelBuilder.newModel(MODEL_ID, "HelloWorld Example Model", "1.0")
                // Return HTML result.
                .setQueryFunction(ctx -> DLQueryResult.html(
                    "Hello World!<br/>" +
                    "See more <a target=_new href='https://youtu.be/zecueq-mo4M'>examples</a> of Hello World!"
                )
            )
            .build()
        );
    }
}
