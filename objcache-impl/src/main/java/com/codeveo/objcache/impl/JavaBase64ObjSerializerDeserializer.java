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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import com.codeveo.objcache.api.SerializerType;
import com.codeveo.objcache.common.ObjCacheErrorCodeType;
import com.codeveo.objcache.common.ObjCacheException;

public class JavaBase64ObjSerializerDeserializer implements ObjCacheSerializerDeserializer {

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.impl.ObjCacheSerializerDeserializer#serialize(java.io.Serializable)
     */
    @Override
    public String serialize(final String aCollectionId, final String anObjectKey, final Object anObject) {
        try (
            final ByteArrayOutputStream theBaos = new ByteArrayOutputStream();
            final ObjectOutputStream theOos = new ObjectOutputStream(theBaos)) {
            theOos.writeObject(anObject);

            return Base64.getEncoder().encodeToString(theBaos.toByteArray());
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
    public <T> T deserialize(
        final String aCollectionId,
        final String anObjectKey,
        final String aSerializedObject,
        Class<T> aClass) {
        try {
            final byte[] theData = Base64.getDecoder().decode(aSerializedObject);

            try (final ObjectInputStream theOis = new ObjectInputStream(new ByteArrayInputStream(theData))) {
                return aClass.cast(theOis.readObject());
            }
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
        return SerializerType.JAVA_BASE64;
    }
}
