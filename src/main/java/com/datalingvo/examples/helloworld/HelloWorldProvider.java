/*
 * 2014-2015 Copyright (C) DataLingvo, Inc. All Rights Reserved.
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
 */
@DLActiveModelProvider
public class HelloWorldProvider extends DLSingleModelProviderAdapter {
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
            DLModelBuilder.newModel(MODEL_ID, "HelloWorld Example Model", "1.0")
                .setQueryFunction(ctx -> DLQueryResult.html(
                    "Hello World!<br/>" +
                    "See more <a target=_new href='https://youtu.be/zecueq-mo4M'>examples</a> of Hello World!"
                )
            )
            .build()
        );
    }
}
