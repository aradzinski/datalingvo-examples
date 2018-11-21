/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.weather.test;

import com.datalingvo.mdllib.tools.dev.*;
import org.junit.jupiter.api.*;

import static com.datalingvo.mdllib.tools.dev.DLTestClient.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Weather provider test.
 * Make sure to setup these system properties:
 * <ul>
 *     <li>
 *         <code>DATALINGVO_PROBE_EMAIL</code>=<code>probe user admin email</code>
 *     </li>
 *     <li>
 *         <code>DATALINGVO_PROBE_TOKEN</code>=<code>probe user admin email</code>
 *     </li>
 * </ul>
 *
 * Also check and set if necessary {@link WeatherProviderTest#DL_DS_NAME}
 */
public class WeatherProviderTest {
    /** Weather example datasource must be registered with this name. */
    private static final String DL_DS_NAME = "weather";
    
    /** Weather example intent ID, which called WeatherProvider#onCurrentMatch(DLIntentSolverContext)) */
    private static final String DL_INTENT_ID = "curr|date?|city?";
    
    private static final DLTestClientConfig cfg = new DLTestClientConfig();
    
    /**
     * Checks some sentences in default batch mode.
     *
     * @throws Exception If any error occur.
     */
    @Test
    public void test() throws Exception {
        DLTestClient asyncCli = DLTestClientBuilder.newBuilder().build(cfg);
    
        // Test sentences with expected results.
        assertTrue(
            asyncCli.test(
                new DLTestSentence(DL_DS_NAME, "Los Angeles weather now", DL_INTENT_ID, RESP_OK),
                new DLTestSentence(DL_DS_NAME, "Hi", RESP_TRIVIA), // No intent ID needed.
                new DLTestSentence(DL_DS_NAME, "Weather limit 5", DL_INTENT_ID, RESP_CURATION) // Forwarded to Curator from given intent.
                
            ).stream().noneMatch(DLTestResult::hasError)
        );
    }
    
    /**
     * Checks some sentences in non-conversation mode.
     *
     * @throws Exception If any error occur.
     */
    @Test
    public void testNoConversation() throws Exception {
        DLTestClient nonConvCli =
            DLTestClientBuilder.newBuilder().withAsyncMode(false).withClearConversation(true).build(cfg);
    
        // Test sentences with expected results.
        // Conversation is cleared after each sentence (note that async mode is turned off)
        // Second sentence should NOT be processed, because main element `weather` will be missing.
        assertTrue(
            nonConvCli.test(
                new DLTestSentence(DL_DS_NAME, "Los Angeles weather", DL_INTENT_ID, RESP_OK),
                new DLTestSentence(DL_DS_NAME, "San Francisco", RESP_REJECT)  // No intent ID needed, rejected when intent is not found.
            ).stream().noneMatch(DLTestResult::hasError)
        );
    }
    
    /**
     * Checks some sentences in conversation mode.
     *
     * @throws Exception If any error occur.
     */
    @Test
    public void testConversation() throws Exception {
        DLTestClient convCli =
            DLTestClientBuilder.newBuilder().withAsyncMode(false).withClearConversation(false).build(cfg);
    
        // Test sentences with expected results.
        // Conversation is NOT cleared after each sentence (note that async mode is turned off)
        // Second sentence should be processed OK, because element `weather` will be added from conversation context.
        assertTrue(
            convCli.test(
                new DLTestSentence(DL_DS_NAME, "Los Angeles weather", DL_INTENT_ID, RESP_OK),
                new DLTestSentence(DL_DS_NAME, "San Francisco", DL_INTENT_ID, RESP_OK)
            ).stream().noneMatch(DLTestResult::hasError)
        );
    }
}
