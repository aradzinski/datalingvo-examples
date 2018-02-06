/*
 * 2013-2015 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.time;

import com.datalingvo.DLException;
import com.datalingvo.probe.dev.*;

/**
 * In-process probe runner for time model.
 * <p>
 * Make sure to setup these system properties:
 * <ul>
 *     <li>{@code DATALINGVO_PROBE_ID} with probe ID (any user defined name).</li>
 *     <li>{@code DATALINGVO_PROBE_TOKEN} with probe token (see admin page).</li>
 * </ul>
 */
public class TimeProbeRunner {
    /**
     *
     * @param args Command like arguments (none are required).
     */
    public static void main(String[] args) throws DLException {
        int exitCode = DLProbeDevApp.start(new DLProbeConfig(new TimeProvider()));

        System.exit(exitCode);
    }
}
