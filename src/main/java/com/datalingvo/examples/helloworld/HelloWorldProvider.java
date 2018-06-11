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

import com.datalingvo.DLException;
import com.datalingvo.mdllib.DLActiveModelProvider;
import com.datalingvo.mdllib.DLQueryResult;
import com.datalingvo.mdllib.DLSingleModelProviderAdapter;
import com.datalingvo.mdllib.tools.builder.DLModelBuilder;

/**
 * Hello World example model provider.
 * <p>
 * This example simply responds with 'Hello World!' on any user input. This is the simplest
 * user model that can be defined.
 */
@DLActiveModelProvider
public class HelloWorldProvider extends DLSingleModelProviderAdapter {
    /**
     * Initializes provider.
     *
     * @throws DLException If any errors occur.
     */
    HelloWorldProvider() throws DLException {
        // Initialize adapter.
        setup(
            // Minimally defined model...
            DLModelBuilder.newModel("dl.helloworld.ex", "HelloWorld Example Model", "1.0")
                // Return HTML result.
                .setQueryFunction(ctx -> DLQueryResult.html(
                    "Hello World!<br/>" +
                    "See more <a target=_new href='https://youtu.be/zecueq-mo4M'>examples</a> of Hello World!"
                ))
                .build()
        );
    }
}
