package com.codeveo.objcache.impl;

import com.codeveo.objcache.api.SerializerType;

public interface ObjCacheSerializerDeserializerFactory {

    ObjCacheSerializerDeserializer getSerializer(SerializerType aSerializerType);

}
