/*
 * 2013-2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
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
import com.datalingvo.mdllib.tools.scala.DLScalaSupport._ /* IDEA wrongly marks it as unused. */

/**
  * Chat example model provider.
  */
@DLActiveModelProvider
class ChatProvider extends DLSingleModelProviderAdapter {
    private final val MODEL_ID = "dl.chat.ex"
    
    setup(
        MODEL_ID,
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
            .setQueryFunction((_: DLQueryContext) ⇒ { throw new DLCuration("Please curate!") }
        )
        .build()
    )
}