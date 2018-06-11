/*
 * 2017 Copyright (C) DataLingvo, Inc. All Rights Reserved.
 *       ___      _          __ _
 *      /   \__ _| |_ __ _  / /(_)_ __   __ ___   _____
 *     / /\ / _` | __/ _` |/ / | | '_ \ / _` \ \ / / _ \
 *    / /_// (_| | || (_| / /__| | | | | (_| |\ V / (_) |
 *   /___,' \__,_|\__\__,_\____/_|_| |_|\__, | \_/ \___/
 *                                      |___/
 */

package com.datalingvo.examples.lessons.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lessons utility class.
 */
public class LessonsUtils {
    /**
     * Provides formatted current time.
     *
     * @return Formatted time.
     */
    public static String now() {
        return DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now(ZoneId.systemDefault()));
    }
    
    /**
     * Provides formatted current time for given zone ID.
     *
     * @param id Zone ID.
     * @return Formatted time.
     */
    public static String now(ZoneId id) {
        return DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now(id));
    }
}
