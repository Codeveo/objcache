/* Copyright (C) 2018 Codeveo Ltd. - All Rights Reserved
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 */
package com.codeveo.objcache.impl.test;

import com.codeveo.objcache.api.AbstractObjCacheCollection;
import com.codeveo.objcache.api.SerializerType;

public class TestObjCacheCollectionJson extends AbstractObjCacheCollection {

    public static final TestObjCacheCollectionJson INSTANCE = new TestObjCacheCollectionJson();

    public TestObjCacheCollectionJson() {
        super("test", SerializerType.JSON);
    }
}
