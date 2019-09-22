package com.codeveo.objcache.impl;

import com.codeveo.objcache.api.SerializerType;
import com.codeveo.objcache.common.ObjCacheErrorCodeType;
import com.codeveo.objcache.common.ObjCacheException;

public class ObjCacheSerializerDeserializerFactoryImpl implements ObjCacheSerializerDeserializerFactory {

    private static final JsonObjSerializerDeserializer JSON_SERDER = new JsonObjSerializerDeserializer();

    private static final JavaBase64ObjSerializerDeserializer JAVABASE64_SERDER =
        new JavaBase64ObjSerializerDeserializer();

    /**
     * Overrides an inherit method or implements an abstract method.
     *
     * @see com.codeveo.objcache.impl.ObjCacheSerializerDeserializerFactory#getSerializer(com.codeveo.objcache.api.SerializerType)
     */
    @Override
    public ObjCacheSerializerDeserializer getSerializer(SerializerType aSerializerType) {
        switch (aSerializerType) {
            case JAVA_BASE64:
                return JAVABASE64_SERDER;
            case JSON:
                return JSON_SERDER;
        }

        throw new ObjCacheException(
            ObjCacheErrorCodeType.OBJCACHE_EC_0000,
            "Unknonw serializer type " + aSerializerType);
    }
}
