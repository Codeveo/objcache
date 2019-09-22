/*******************************************************************************
 * Copyright 2018 Codeveo Ltd.
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.codeveo.objcache.common;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Common utils which could not be moved to any specific category.
 *
 */
public class ObjCacheCommonUtils {

    private static final ObjectMapper OBJECT_MAPPER = ObjCacheJsonUtils.createDefaultObjectMapper();

    public static String jsonOrDefaultToString(final Object anObject) {
        if (anObject == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(anObject);
        } catch (final JsonProcessingException theException) {
            return anObject.toString();
        }
    }

    public static String formatDateTimeWithZoneSameInstant(final ZonedDateTime aDateTime, final ZoneId aZoneId) {
        return aDateTime
            .withZoneSameInstant(aZoneId)
            .withFixedOffsetZone()
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
