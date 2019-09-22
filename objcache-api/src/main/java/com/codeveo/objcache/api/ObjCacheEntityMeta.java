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
package com.codeveo.objcache.api;

import java.time.ZonedDateTime;

/**
 * Object cache meta document definition.
 *
 */
public class ObjCacheEntityMeta {

    private final String objectKey;

    private final String collection;

    private final SerializerType serializerType;

    private final Integer version;

    private final ZonedDateTime expirationTime;

    /**
     * Constructs a new instance of class ObjCacheMeta.
     *
     * @param aObjectKey object key
     * @param aCollectionId collection ID
     * @param aVersion object version
     * @param anExpirationTime expiration time
     * @param aSerializerType used serializer
     */
    public ObjCacheEntityMeta(
        final String aCollection,
        final String aObjectKey,
        SerializerType aSerializerType,
        final Integer aVersion,
        final ZonedDateTime anExpirationTime) {
        super();
        collection = aCollection;
        objectKey = aObjectKey;
        serializerType = aSerializerType;
        version = aVersion;
        expirationTime = anExpirationTime;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getCollection() {
        return collection;
    }

    public Integer getVersion() {
        return version;
    }

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }

    public SerializerType getSerializerType() {
        return serializerType;
    }
}
