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
package com.codeveo.objcache.impl.test;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.codeveo.objcache.api.ObjCacheEntityMeta;
import com.codeveo.objcache.api.ObjCacheService;
import com.google.common.collect.ImmutableMap;

@ContextConfiguration("classpath:test-context.xml")
public class ObjCacheServiceImplTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjCacheServiceImplTest.class);

    @Autowired
    private ObjCacheService objCacheService;

    @Test
    @DirtiesContext
    public void testCreate() {
        final ObjCacheEntityMeta theMeta =
            objCacheService
                .create(
                    TestObjCacheCollectionJson.INSTANCE,
                    "test1",
                    ImmutableMap.of("a", 1, "b", "text"),
                    new TestObj());
        Assert.assertNotNull(theMeta.getCollection());
        Assert.assertNotNull(theMeta.getCollection().getCollectionId());
        Assert.assertNotNull(theMeta.getCollection().getSerializerType());
        Assert.assertNotNull(theMeta.getObjectKey());
        Assert.assertNotNull(theMeta.getVersion());
        Assert.assertNull(theMeta.getExpirationTime());

        final Optional<TestObj> theTestObj =
            objCacheService.find(TestObjCacheCollectionJson.INSTANCE, "test1", TestObj.class);
        Assert.assertTrue(theTestObj.isPresent());
    }
}
