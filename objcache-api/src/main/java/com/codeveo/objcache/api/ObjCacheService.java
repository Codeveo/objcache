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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codeveo.objcache.common.ObjCacheException;

/**
 * Object cache service.
 *
 */
public interface ObjCacheService {

    /**
     * Count all documents under given collection
     *
     * @param aCollection collection
     * @return objects count
     * @throws ObjCacheException
     */
    long countByCollection(String aCollection) throws ObjCacheException;

    /**
     * Count all documents under given collection and containing given properties
     *
     * @param aCollection collection
     * @param someProperties properties
     * @return objects count
     * @throws ObjCacheException
     */
    long countByProperties(String aCollection, Map<String, Object> someProperties) throws ObjCacheException;

    /**
     * Create new object
     *
     * @param aCollection collection to create under
     * @param anObjectKey object key
     * @param someProperties object properties
     * @param anObject object data
     * @return object meta data
     * @throws ObjCacheException
     */
    ObjCacheEntityMeta create(
        String aCollection,
        String anObjectKey,
        SerializerType aSerializerType,
        Map<String, Object> someProperties,
        Object anObject)
        throws ObjCacheException;

    /**
     * Create new object
     *
     * @param aCollection collection to create under
     * @param anObjectKey object key
     * @param someProperties object properties
     * @param anObject object data
     * @param anExpirationTime expiration time
     * @return object meta data
     * @throws ObjCacheException
     */
    ObjCacheEntityMeta create(
        String aCollection,
        String anObjectKey,
        SerializerType aSerializerType,
        Map<String, Object> someProperties,
        Object anObject,
        ZonedDateTime anExpirationTime)
        throws ObjCacheException;

    /**
     * Delete object
     *
     * @param aCollection collection the object exists under
     * @param anObjectKey object key
     * @throws ObjCacheException
     */
    long delete(String aCollection, String anObjectKey) throws ObjCacheException;

    /**
     * Delete all object under given collection
     *
     * @param aCollection collection
     * @throws ObjCacheException
     */
    long deleteByCollection(String aCollection) throws ObjCacheException;

    /**
     * Delete object by given collection and containing given properties
     *
     * @param aCollection collection the object exists under
     * @param anObjectKey object key
     * @throws ObjCacheException
     */
    long deleteByProperties(String aCollection, Map<String, Object> someProperties) throws ObjCacheException;

    /**
     * Find object by collection and key
     *
     * @param aCollection collection
     * @param anObjectKey object key
     * @return found object or empty
     * @throws ObjCacheException
     */
    <T> Optional<T> find(String aCollection, String anObjectKey, Class<T> aClass) throws ObjCacheException;

    /**
     * Find all documents under given collection
     *
     * @param aCollection collection
     * @return all documents under given collection
     * @throws ObjCacheException
     */
    <T> List<T> findByCollection(String aCollection, Class<T> aClass) throws ObjCacheException;

    /**
     * Find all documents under given collection and containing given properties
     *
     * @param aCollection collection
     * @param someProperties object properties
     * @return found documents
     */
    <T> List<T> findByProperties(String aCollection, Map<String, Object> someProperties, Class<T> aClass)
        throws ObjCacheException;

    /**
     * Update existing object of given version
     *
     *
     * @param aCollection collection the object exists under
     * @param anObjectKey object key
     * @param aVersion object version to update
     * @param someProperties object properties
     * @param anObject object data
     * @param anExpirationTime expiration time
     * @return object meta data
     * @throws ObjCacheException
     */
    ObjCacheEntityMeta update(
        String aCollection,
        String anObjectKey,
        Integer aVersion,
        Map<String, Object> someProperties,
        Object anObject,
        ZonedDateTime anExpirationTime)
        throws ObjCacheException;

    /**
     * Update existing object with version
     *
     *
     * @param aCollection collection the object exists under
     * @param anObjectKey object key
     * @param someProperties object properties
     * @param aVersion object version
     * @param anObject object data
     * @return object meta data
     * @throws ObjCacheException
     */
    ObjCacheEntityMeta update(
        String aCollection,
        String anObjectKey,
        Map<String, Object> someProperties,
        Integer aVersion,
        Object anObject)
        throws ObjCacheException;

    /**
     * Update existing object latest version
     *
     *
     * @param aCollection collection the object exists under
     * @param anObjectKey object key
     * @param someProperties object properties
     * @param anObject object data
     * @return object meta data
     * @throws ObjCacheException
     */
    ObjCacheEntityMeta update(
        String aCollection,
        String anObjectKey,
        Map<String, Object> someProperties,
        Object anObject)
        throws ObjCacheException;

    /**
     * Update existing object latest version
     *
     *
     * @param aCollection collection the object exists under
     * @param anObjectKey object key
     * @param someProperties object properties
     * @param anObject object data
     * @param anExpirationTime expiration time
     * @return object meta data
     * @throws ObjCacheException
     */
    ObjCacheEntityMeta update(
        String aCollection,
        String anObjectKey,
        Map<String, Object> someProperties,
        Object anObject,
        ZonedDateTime anExpirationTime)
        throws ObjCacheException;

}
