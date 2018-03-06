/*
 * 2014-2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.chat

import com.datalingvo.mdllib._
import com.datalingvo.mdllib.tools.builder.DLModelBuilder
import com.datalingvo.mdllib.tools.scala.DLScalaSupport._ /* NOTE: IDEA wrongly marks it as unused. */

/**
  * Chat example model provider.
  * <p>
  * This model sends any user requests to curation allowing human Linguist to "chat"
  * with user via talkback, curation or rejection functionality. This is primarily for
  * easy demonstration of Linguist operations.
  */
@DLActiveModelProvider
class ChatProvider extends DLSingleModelProviderAdapter {
    // Any immutable user defined ID.
    private final val MODEL_ID = "dl.chat.ex"
    
    // Setting up provider adapter.
    setup(
        MODEL_ID,
        // Using inline JSON model.
        DLModelBuilder.newJsonStringModel(
            s"""
               | {
               |    "id": "$MODEL_ID",
               |    "name": "Chat Example Model",
               |    "version": "1.0",
               |    "metadata": {
               |        "DESCRIPTION": "Chat example model.",
               |        "VENDOR_NAME": "DataLingvo, Inc",
               |        "VENDOR_URL": "https://www.datalingvo.com",
               |        "VENDOR_CONTACT": "Support",
               |        "VENDOR_EMAIL": "info@datalingvo.com",
               |        "DOCS_URL": "https://www.datalingvo.com",
               |        "ALLOW_NO_USER_TOKENS": true
               |    },
               |    "examples": [
               |        "Hey, any plans this evening?",
               |        "Wanna see Warriors tonight?"
               |    ],
               |    "defaultTrivia": "true"
               | }
            """.stripMargin)
            // Query function sends any user input to curation.
            .setQueryFunction((_: DLQueryContext) ⇒ { throw new DLCuration("Please curate!") }
        )
        .build()
    )
}