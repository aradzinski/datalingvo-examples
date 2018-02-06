/*
 * 2013-2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.whereami

import com.datalingvo.probe.dev.{DLProbeConfig, DLProbeDevApp}

/**
  * In-process probe runner for "Where Am I" model.
  * <p>
  * Make sure to setup these system properties:
  * <ul>
  *     <li><code>DATALINGVO_PROBE_ID</code> with probe ID (any user defined name).</li>
  *     <li><code>DATALINGVO_PROBE_TOKEN</code> with probe token (see admin page).</li>
  * </ul>
  */
object WhereAmIProbeRunner extends App {
    val exitCode = DLProbeDevApp.start(new DLProbeConfig(new WhereAmIProvider()))
    
    System.exit(exitCode)
}
