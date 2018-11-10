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

import com.codeveo.objcache.api.ObjCacheEntityMeta;
import com.codeveo.objcache.api.ObjCacheService;
import com.codeveo.objcache.api.SerializerType;
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

    private static final Field<String> COL_OBJECT_DATA = DSL.field(DSL.name(TABLE_NAME, "object_data"), String.class);

    private static final Field<String> COL_SERIALIZER_TYPE =
        DSL.field(DSL.name(TABLE_NAME, "serializer_type"), String.class);

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
     * @see com.codeveo.objcache.api.ObjCacheService#countByCollection(com.codeveo.objcache.api.String)
     */
    @Override
    public long countByCollection(final String aCollection) throws ObjCacheException {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            final String theQuery =
                DSL.selectCount().from(TABLE).where(COL_COLLECTION_ID.eq(aCollection)).getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            return jdbcTemplate.queryForObject(theQuery, long.class);
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0006,
                anException,
                "Error occured while running query for collection '" + aCollection + "'");
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#countByProperties(com.codeveo.objcache.api.String,
     *      java.util.Map)
     */
    @Override
    public long countByProperties(final String aCollection, final Map<String, Object> someProperties)
        throws ObjCacheException {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            final String theQuery =
                DSL
                    .selectCount()
                    .from(TABLE)
                    .where(COL_COLLECTION_ID.eq(aCollection))
                    .and(DSL.condition("{0} @> {1}", COL_OBJECT_PROPERTIES, MAPPER.writeValueAsString(someProperties)))
                    .getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            return jdbcTemplate.queryForObject(theQuery, long.class);
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0006,
                anException,
                "Error occured while running query for collection '" + aCollection + "'");
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#create(com.codeveo.objcache.api.String,
     *      java.lang.String, java.util.Map, java.io.Object)
     */
    @Override
    public ObjCacheEntityMeta create(
        final String aCollection,
        final String anObjectKey,
        SerializerType aSerializerType,
        final Map<String, Object> someProperties,
        final Object anObject)
        throws ObjCacheException {
        return txTemplate
            .execute(
                aStatus -> createCommon(aCollection, anObjectKey, aSerializerType, someProperties, anObject, null));
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#create(com.codeveo.objcache.api.String,
     *      java.lang.String, java.util.Map, java.io.Object, java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta create(
        final String aCollection,
        final String anObjectKey,
        SerializerType aSerializerType,
        final Map<String, Object> someProperties,
        final Object anObject,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        return txTemplate
            .execute(
                aStatus -> createCommon(
                    aCollection,
                    anObjectKey,
                    aSerializerType,
                    someProperties,
                    anObject,
                    anExpirationTime));
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#delete(com.codeveo.objcache.api.String,
     *      java.lang.String)
     */
    @Override
    public long delete(final String aCollection, final String anObjectKey) throws ObjCacheException {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            Validate.notBlank(anObjectKey, "Object key must be not blank");
            final String theQuery =
                DSL
                    .deleteFrom(TABLE)
                    .where(COL_COLLECTION_ID.eq(aCollection))
                    .and(COL_OBJECT_KEY.eq(anObjectKey))
                    .getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            return jdbcTemplate.update(theQuery);
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0006,
                anException,
                "Error occured while running delete query for collection '"
                    + aCollection
                    + "' and object key '"
                    + anObjectKey
                    + "'");
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#deleteByCollection(com.codeveo.objcache.api.String)
     */
    @Override
    public long deleteByCollection(final String aCollection) throws ObjCacheException {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            final String theQuery =
                DSL.deleteFrom(TABLE).where(COL_COLLECTION_ID.eq(aCollection)).getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            return jdbcTemplate.update(theQuery);
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0006,
                anException,
                "Error occured while running delete query objects for collection '" + aCollection + "'");
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#deleteByProperties(com.codeveo.objcache.api.String,
     *      java.lang.String)
     */
    @Override
    public long deleteByProperties(final String aCollection, Map<String, Object> someProperties)
        throws ObjCacheException {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            final String theQuery =
                DSL
                    .deleteFrom(TABLE)
                    .where(COL_COLLECTION_ID.eq(aCollection))
                    .and(DSL.condition("{0} @> {1}", COL_OBJECT_PROPERTIES, MAPPER.writeValueAsString(someProperties)))
                    .getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            return jdbcTemplate.update(theQuery);
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0006,
                anException,
                "Error occured while running delete query objects for collection '"
                    + aCollection
                    + "' and propeties '"
                    + someProperties
                    + "'");
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#expire(com.codeveo.objcache.api.String,
     *      java.lang.String, java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta expire(
        final String aCollection,
        final String anObjectKey,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.expire
        return null;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#find(com.codeveo.objcache.api.String,
     *      java.lang.String)
     */
    @Override
    public <T> Optional<T> find(final String aCollection, final String anObjectKey, Class<T> aClass)
        throws ObjCacheException {
        try {
            final String theQuery =
                DSL
                    .selectFrom(TABLE)
                    .where(COL_COLLECTION_ID.eq(aCollection))
                    .and(COL_OBJECT_KEY.eq(anObjectKey))
                    .getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            final T theObjectData = jdbcTemplate.queryForObject(theQuery, (aRowMapper, aRowNum) -> {
                final String theColObjectData = aRowMapper.getString(COL_OBJECT_DATA.getName());
                final SerializerType theSerializerType =
                    SerializerType.valueOf(aRowMapper.getString(COL_SERIALIZER_TYPE.getName()));
                return objSerDerFactory
                    .getSerializer(theSerializerType)
                    .deserialize(aCollection, anObjectKey, theColObjectData, aClass);
            });

            return Optional.ofNullable(theObjectData);
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0005, anException, anObjectKey, aCollection);
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#findAll(com.codeveo.objcache.api.String)
     */
    @Override
    public <T> List<T> findByCollection(final String aCollection, Class<T> aClass) throws ObjCacheException {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            final String theQuery =
                DSL.selectFrom(TABLE).where(COL_COLLECTION_ID.eq(aCollection)).getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            final List<T> theObjectsList = jdbcTemplate.query(theQuery, (aRowMapper, aRowNum) -> {
                final String theColObjectData = aRowMapper.getString(COL_OBJECT_DATA.getName());
                final String theObjectKey = aRowMapper.getString(COL_OBJECT_KEY.getName());
                final SerializerType theSerializerType =
                    SerializerType.valueOf(aRowMapper.getString(COL_SERIALIZER_TYPE.getName()));
                return objSerDerFactory
                    .getSerializer(theSerializerType)
                    .deserialize(aCollection, theObjectKey, theColObjectData, aClass);
            });

            return theObjectsList;
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0006,
                anException,
                "Error occured while running query objects for collection '" + aCollection + "'");
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#findByProperties(com.codeveo.objcache.api.String,
     *      java.util.Map)
     */
    @Override
    public <T> List<T> findByProperties(
        final String aCollection,
        final Map<String, Object> someProperties,
        Class<T> aClass)
        throws ObjCacheException {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            final String theQuery =
                DSL
                    .selectFrom(TABLE)
                    .where(COL_COLLECTION_ID.eq(aCollection))
                    .and(DSL.condition("{0} @> {1}", COL_OBJECT_PROPERTIES, MAPPER.writeValueAsString(someProperties)))
                    .getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            final List<T> theObjectsList = jdbcTemplate.query(theQuery, (aRowMapper, aRowNum) -> {
                final String theColObjectData = aRowMapper.getString(COL_OBJECT_DATA.getName());
                final String theObjectKey = aRowMapper.getString(COL_OBJECT_KEY.getName());
                final SerializerType theSerializerType =
                    SerializerType.valueOf(aRowMapper.getString(COL_SERIALIZER_TYPE.getName()));
                return objSerDerFactory
                    .getSerializer(theSerializerType)
                    .deserialize(aCollection, theObjectKey, theColObjectData, aClass);
            });

            return theObjectsList;
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(
                ObjCacheErrorCodeType.OBJCACHE_EC_0006,
                anException,
                "Error occured while running query objects for collection '" + aCollection + "'");
        }
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.String,
     *      java.lang.String, java.lang.Long, java.util.Map, java.io.Object,
     *      java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta update(
        final String aCollection,
        final String anObjectKey,
        final Long aVersion,
        final Map<String, Object> someProperties,
        final Object anObject,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0001, "update with specified version");
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.String,
     *      java.lang.String, java.util.Map, java.lang.Long, java.io.Object)
     */
    @Override
    public ObjCacheEntityMeta update(
        final String aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Long aVersion,
        final Object anObject)
        throws ObjCacheException {
        throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0001, "update with specified version");
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.String,
     *      java.lang.String, java.util.Map, java.io.Object)
     */
    @Override
    public ObjCacheEntityMeta update(
        final String aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Object anObject)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.update
        return null;
    }

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.api.ObjCacheService#update(com.codeveo.objcache.api.String,
     *      java.lang.String, java.util.Map, java.io.Object, java.time.ZonedDateTime)
     */
    @Override
    public ObjCacheEntityMeta update(
        final String aCollection,
        final String anObjectKey,
        final Map<String, Object> someProperties,
        final Object anObject,
        final ZonedDateTime anExpirationTime)
        throws ObjCacheException {
        // TODO Ladislav Klenovic, 19. 10. 2018: Implement method ObjCacheService.update
        return null;
    }

    private ObjCacheEntityMeta createCommon(
        final String aCollection,
        final String anObjectKey,
        SerializerType aSerializerType,
        final Map<String, Object> someProperties,
        final Object anObject,
        final ZonedDateTime anExpirationTime) {
        try {
            Validate.notBlank(aCollection, "Collection must be not blank");
            Validate.notBlank(anObjectKey, "Object key must be not blank");
            Validate.notNull(aSerializerType, "Serializer type must be not null");

            final String theProps = MAPPER.writeValueAsString(someProperties);
            final String theObjDataSerialized =
                serializeObjectData(aCollection, anObjectKey, aSerializerType, anObject);
            final String theQuery =
                DSL
                    .insertInto(TABLE)
                    .columns(
                        COL_COLLECTION_ID,
                        COL_OBJECT_KEY,
                        COL_SERIALIZER_TYPE,
                        COL_VERSION,
                        COL_OBJECT_DATA,
                        COL_OBJECT_PROPERTIES,
                        COL_EXPIRATION_TIME)
                    .values(
                        aCollection,
                        anObjectKey,
                        aSerializerType.name(),
                        1,
                        theObjDataSerialized,
                        theProps,
                        anExpirationTime != null
                            ? ObjCacheCommonUtils.formatDateTimeWithZoneSameInstant(anExpirationTime, ZoneId.of("UTC"))
                            : null)
                    .getSQL(ParamType.INLINED);

            LOGGER.debug("Running query '{}'", theQuery);

            final int theCount = jdbcTemplate.update(theQuery);

            if (theCount != 1) {
                throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0005, anObjectKey, aCollection);
            }

            return new ObjCacheEntityMeta(aCollection, anObjectKey, aSerializerType, 1, anExpirationTime);
        } catch (final ObjCacheException anException) {
            throw anException;
        } catch (final Exception anException) {
            throw new ObjCacheException(ObjCacheErrorCodeType.OBJCACHE_EC_0005, anException, anObjectKey, aCollection);
        }
    }

    private String serializeObjectData(
        final String aCollection,
        final String anObjectKey,
        SerializerType aSerializerType,
        final Object anObject) {
        final ObjCacheSerializerDeserializer theSerializer = objSerDerFactory.getSerializer(aSerializerType);

        return theSerializer.serialize(aCollection, anObjectKey, anObject);
    }
}
