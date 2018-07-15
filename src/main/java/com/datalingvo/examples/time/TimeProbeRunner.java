/*
 * 2014-2018 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.time;

import com.datalingvo.DLException;
import com.datalingvo.probe.dev.DLProbeConfig;
import com.datalingvo.probe.dev.DLProbeDevApp;

/**
 * In-process probe runner for this example.
 * <p>
 * Make sure to setup these system properties:
 * <ul>
 *     <li>
 *         <code>DATALINGVO_PROBE_ID</code>=<code>probe ID</code>
 *         (any user defined name).
 *     </li>
 *     <li>
 *         <code>DATALINGVO_PROBE_TOKEN</code>=<code>probe token</code>
 *         (see <a href="https://datalingvo.com/client/src/datalingvo.html#/account">Account</a> page).
 *     </li>
 * </ul>
 */
public class TimeProbeRunner {
    /**
     * In-process probe entry point.
     *
     * @param args Command like arguments (none are required).
     */
    public static void main(String[] args) throws DLException {
        // Create probe configuration with the provider instance.
        DLProbeConfig cfg = new DLProbeConfig(new TimeProvider());

        // Start probe and wait synchronously for its exit code.
        int exitCode = DLProbeDevApp.start(cfg);

        System.exit(exitCode);
    }
}
