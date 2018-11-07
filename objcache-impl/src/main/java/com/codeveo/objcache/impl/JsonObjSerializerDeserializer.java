/*******************************************************************************
 * Copyright 2018 Codeveo Ltd.
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.codeveo.objcache.impl;

import java.io.Serializable;

import com.codeveo.objcache.api.SerializerType;
import com.codeveo.objcache.common.ObjCacheErrorCodeType;
import com.codeveo.objcache.common.ObjCacheException;
import com.codeveo.objcache.common.ObjCacheJsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjSerializerDeserializer implements ObjCacheSerializerDeserializer {

    private static final ObjectMapper MAPPER = ObjCacheJsonUtils.createDefaultObjectMapper();

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.impl.ObjCacheSerializerDeserializer#serialize(java.lang.String,
     *      java.lang.String, java.io.Serializable)
     */
    @Override
    public String serialize(final String aCollectionId, final String anObjectKey, final Serializable anObject) {
        try {
            return anObject != null ? MAPPER.writeValueAsString(anObject) : null;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0003,
                anException,
                aCollectionId,
                anObjectKey);
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.impl.ObjCacheSerializerDeserializer#deserialize(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public <T extends Serializable> T deserialize(
        final String aCollectionId,
        final String anObjectKey,
        final String aSerializedObject) {
        try {
            return MAPPER.readValue(aSerializedObject, new TypeReference<T>() {
            });
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0004,
                anException,
                aCollectionId,
                anObjectKey);
        }
    }

    @Override
    public SerializerType getSerializerType() {
        return SerializerType.JSON;
    }
}
