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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjCacheJsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createDefaultObjectMapper();

    /**
     * Create JSON for a given input. Default object mapper is used for marshalling.
     *
     * @param anObject input object
     * @return Created JSON
     */
    public String toJson(final Object anObject) {
        try {
            return OBJECT_MAPPER.writeValueAsString(anObject);
        } catch (final Exception theException) {
            throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0000, theException);
        }
    }

    public <T> T fromJson(final String aJson, final Class<T> aClass) {
        try {
            return OBJECT_MAPPER.readValue(aJson, aClass);
        } catch (final Exception theException) {
            throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0000, theException);
        }
    }

    public <T> T fromJson(final String aJson, final TypeReference<T> aTypeReference) {
        try {
            return OBJECT_MAPPER.readValue(aJson, aTypeReference);
        } catch (final Exception theException) {
            throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0000, theException);
        }
    }

    /**
     * Get default object mapper with the following features:
     * <ul>
     * <li>DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES = true</li>
     * <li>DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL = true</li>
     * <li>DeserializationFeature.READ_ENUMS_USING_TO_STRING = true</li>
     * <li>DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS = true</li>
     * <li>disabled SerializationFeature.WRITE_DATES_AS_TIMESTAMPS</li>
     * <li>registered module new JavaTimeModule()</li>
     * </ul>
     *
     * @return object mapper
     */
    public static ObjectMapper createDefaultObjectMapper() {
        return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());
    }
}
