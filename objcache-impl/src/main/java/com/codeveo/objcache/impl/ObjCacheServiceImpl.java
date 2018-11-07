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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.codeveo.objcache.api.ObjCacheCollection;
import com.codeveo.objcache.api.ObjCacheEntityMeta;
import com.codeveo.objcache.api.ObjCacheService;
import com.codeveo.objcache.common.ObjCacheCommonUtils;
import com.codeveo.objcache.common.ObjCacheErrorCodeType;
import com.codeveo.objcache.common.ObjCacheException;
import com.codeveo.objcache.common.ObjCacheJsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Object cache service implementation.
 *
 */
public class ObjCacheServiceImpl implements ObjCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjCacheServiceImpl.class);

    private static final ObjectMapper MAPPER = ObjCacheJsonUtils.createDefaultObjectMapper();

    private static final String TABLE_NAME = "t_objcache";

    private static final Table<Record> TABLE = DSL.table(DSL.name(TABLE_NAME));

    private static final Field<Long> COL_ID = DSL.field(DSL.name(TABLE_NAME, "id"), Long.class);

    private static final Field<String> COL_COLLECTION_ID =
        DSL.field(DSL.name(TABLE_NAME, "collection_id"), String.class);

    private static final Field<String> COL_OBJECT_KEY = DSL.field(DSL.name(TABLE_NAME, "object_key"), String.class);

    private static final Field<Integer> COL_VERSION = DSL.field(DSL.name(TABLE_NAME, "version"), Integer.class);

    private static final Field<String> COL_SERIALIZER_TYPE =
        DSL.field(DSL.name(TABLE_NAME, "serializer_type"), String.class);

    private static final Field<String> COL_OBJECT_DATA = DSL.field(DSL.name(TABLE_NAME, "object_data"), String.class);

    private static final Field<String> COL_OBJECT_PROPERTIES =
        DSL.field(DSL.name(TABLE_NAME, "properties"), String.class);

    private static final Field<String> COL_EXPIRATION_TIME =
        DSL.field(DSL.name(TABLE_NAME, "expiration_time"), String.class);

    private final TransactionTemplate txTemplate;

    private final JdbcTemplate jdbcTemplate;

    private final ObjCacheSerializerDeserializerFactory objSerDerFactory;

    public ObjCacheServiceImpl(
        TransactionTemplate aTxTemplate,
        JdbcTemplate aJdbcTemplate,
        ObjCacheSerializerDeserializerFactory anObjSerDerFactory) {
        txTemplate = Validate.notNull(aTxTemplate, "Transaction template is required");
        jdbcTemplate = Validate.notNull(aJdbcTemplate, "JDBC template is required");
        objSerDerFactory = Validate.notNull(anObjSerDerFactory, "ObjCache serializer factory is required");
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#countAll(com.codeveo.objcache.api.ObjCacheCollection)
     */
    @Override
    public long countAll(final ObjCacheCollection aCollection) throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.countAll
        return 0;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#countByProperties(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.util.Map)
     */
    @Override
    public long countByProperties(final ObjCacheCollection aCollection, final Map<String, Object> someProperties)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method
        // ObjCacheService.countByProperties
        return 0;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#create(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String, java.util.Map, java.io.Serializable)
     */
    @Override
    public ObjCacheEntityMeta create(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Serializable anObject)
        throws ObjCacheException {
        return txTemplate.execute(aStatus -> createCommon(aCollection, anObjectKey, someProperties, anObject, null));
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#create(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String, java.util.Map, java.io.Serializable, java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta create(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Serializable anObject,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        return txTemplate
            .execute(status -> createCommon(aCollection, anObjectKey, someProperties, anObject, anExpirationTime));
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#delete(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String)
     */
    @Override
    public void delete(final ObjCacheCollection aCollection, final String anObjectKey) throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.delete

    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#deleteByCollection(com.codeveo.objcache.api.ObjCacheCollection)
     */
    @Override
    public void deleteByCollection(final ObjCacheCollection aCollection) throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method
        // ObjCacheService.deleteByCollectionId

    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#deleteByProperties(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String)
     */
    @Override
    public void deleteByProperties(final ObjCacheCollection aCollection, final String anObjectKey)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method
        // ObjCacheService.deleteByProperties

    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#expire(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String, java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta expire(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.expire
        return null;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#find(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String)
     */
    @Override
    public <T> Optional<T> find(final ObjCacheCollection aCollection, final String anObjectKey)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.find
        return null;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#findAll(com.codeveo.objcache.api.ObjCacheCollection)
     */
    @Override
    public <T> List<T> findAll(final ObjCacheCollection aCollection) throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.findAll
        return null;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#findByProperties(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.util.Map)
     */
    @Override
    public <T> List<T> findByProperties(final ObjCacheCollection aCollection, final Map<String, Object> someProperties)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.findByProperties
        return null;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String, java.lang.Long, java.util.Map, java.io.Serializable,
     *      java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta update(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Long aVersion,
        final Map<String, Object> someProperties,
        final Serializable anObject,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0001, "update with specified version");
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String, java.util.Map, java.lang.Long, java.io.Serializable)
     */
    @Override
    public ObjCacheEntityMeta update(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Long aVersion,
        final Serializable anObject)
        throws ObjCacheException {
        throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0001, "update with specified version");
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String, java.util.Map, java.io.Serializable)
     */
    @Override
    public ObjCacheEntityMeta update(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Serializable anObject)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.update
        return null;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.ObjCacheCollection,
     *      java.lang.String, java.util.Map, java.io.Serializable, java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta update(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Serializable anObject,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.update
        return null;
    }

    private String serializeObjectData(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Serializable anObject) {
        final ObjCacheSerializerDeserializer theSerializer =
            objSerDerFactory.getSerializer(aCollection.getSerializerType());

        return theSerializer.serialize(aCollection.getCollectionId(), anObjectKey, anObject);
    }

    private void validateArgs(ObjCacheCollection aCollection, String anObjectKey) {
        Validate.notNull(aCollection, "Collection is must be not null");
        Validate.notBlank(aCollection.getCollectionId(), "Collection ID must be not blank");
        Validate.notNull(aCollection.getSerializerType(), "Serializer type must be not null");
    }

    private void validateArgs(ObjCacheCollection aCollection, String anObjectKey, Integer aVersion) {
        validateArgs(aCollection, anObjectKey);
        Validate.isTrue(aVersion != null && aVersion > 0, "Version must be positive number");
    }

    private ObjCacheEntityMeta createCommon(
        final ObjCacheCollection aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Serializable anObject,
        final ZonedDateTime anExpirationTime) {
        try {
            validateArgs(aCollection, anObjectKey);
            final String theProps = MAPPER.writeValueAsString(someProperties);
            final String theObjDataSerialized = serializeObjectData(aCollection, anObjectKey, anObject);
            final String theQuery =
                DSL
                    .insertInto(TABLE)
                    .columns(
                        COL_COLLECTION_ID,
                        COL_OBJECT_KEY,
                        COL_VERSION,
                        COL_SERIALIZER_TYPE,
                        COL_OBJECT_DATA,
                        COL_OBJECT_PROPERTIES,
                        COL_EXPIRATION_TIME)
                    .values(
                        aCollection.getCollectionId(),
                        anObjectKey,
                        1,
                        aCollection.getSerializerType().name(),
                        theObjDataSerialized,
                        theProps,
                        anExpirationTime != null
                            ? ObjCacheCommonUtils.formatDateTimeWithZoneSameInstant(anExpirationTime, ZoneId.of("UTC"))
                            : null)
                    .getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            final int theCount = jdbcTemplate.update(theQuery);

            if (theCount != 1) {
                throw new ObjCacheException(
                    ObjCacheErrorCodeType.OBJCACHE_EC_0005,
                    anObjectKey,
                    aCollection.getCollectionId());
            }

            return new ObjCacheEntityMeta(
                anObjectKey,
                aCollection.getCollectionId(),
                1,
                anExpirationTime,
                aCollection.getSerializerType());
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0005,
                anException,
                anObjectKey,
                aCollection.getCollectionId());
        }
    }
}
