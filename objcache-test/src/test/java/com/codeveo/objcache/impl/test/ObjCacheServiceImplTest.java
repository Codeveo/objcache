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

import java.util.List;
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
import com.codeveo.objcache.api.SerializerType;
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
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text"),
                    new TestObj());
        Assert.assertNotNull(theMeta.getCollection());
        Assert.assertNotNull(theMeta.getSerializerType());
        Assert.assertNotNull(theMeta.getObjectKey());
        Assert.assertNotNull(theMeta.getVersion());
        Assert.assertNull(theMeta.getExpirationTime());

        final Optional<TestObj> theTestObj = objCacheService.find(TestObj.COLLECTION, "test1", TestObj.class);
        Assert.assertTrue(theTestObj.isPresent());
    }

    @Test
    @DirtiesContext
    public void testFindByCollection() {
        final TestObj theTestObj1 = new TestObj("a", 1, ImmutableMap.of("k1", 1, "k2", "v2"));
        final TestObj theTestObj2 = new TestObj("b", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final ObjCacheEntityMeta theMeta1 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text"),
                    theTestObj1);
        Assert.assertNotNull(theMeta1.getCollection());
        Assert.assertNotNull(theMeta1.getSerializerType());
        Assert.assertNotNull(theMeta1.getObjectKey());
        Assert.assertNotNull(theMeta1.getVersion());
        Assert.assertNull(theMeta1.getExpirationTime());

        final ObjCacheEntityMeta theMeta2 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test2",
                    SerializerType.JSON,
                    ImmutableMap.of("c", 1, "d", "text"),
                    theTestObj2);
        Assert.assertNotNull(theMeta2.getCollection());
        Assert.assertNotNull(theMeta2.getSerializerType());
        Assert.assertNotNull(theMeta2.getObjectKey());
        Assert.assertNotNull(theMeta2.getVersion());
        Assert.assertNull(theMeta2.getExpirationTime());

        final List<TestObj> theTestObjs = objCacheService.findByCollection(TestObj.COLLECTION, TestObj.class);
        Assert.assertEquals(theTestObjs.size(), 2);
        Assert.assertEqualsNoOrder(theTestObjs.toArray(), new Object[] { theTestObj1, theTestObj2 });
    }

    @Test
    @DirtiesContext
    public void testFindByProperties() {
        final TestObj theTestObj1 = new TestObj("a", 1, ImmutableMap.of("k1", 1, "k2", "v2"));
        final TestObj theTestObj2 = new TestObj("b", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final ObjCacheEntityMeta theMeta1 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text"),
                    theTestObj1);
        Assert.assertNotNull(theMeta1.getCollection());
        Assert.assertNotNull(theMeta1.getSerializerType());
        Assert.assertNotNull(theMeta1.getObjectKey());
        Assert.assertNotNull(theMeta1.getVersion());
        Assert.assertNull(theMeta1.getExpirationTime());

        final ObjCacheEntityMeta theMeta2 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test2",
                    SerializerType.JSON,
                    ImmutableMap.of("c", 1, "d", "text"),
                    theTestObj2);
        Assert.assertNotNull(theMeta2.getCollection());
        Assert.assertNotNull(theMeta2.getSerializerType());
        Assert.assertNotNull(theMeta2.getObjectKey());
        Assert.assertNotNull(theMeta2.getVersion());
        Assert.assertNull(theMeta2.getExpirationTime());

        final List<TestObj> theTestObjs =
            objCacheService.findByProperties(TestObj.COLLECTION, ImmutableMap.of("d", "text"), TestObj.class);
        Assert.assertEquals(theTestObjs.size(), 1);
        Assert.assertEqualsNoOrder(theTestObjs.toArray(), new Object[] { theTestObj2 });
    }

    @Test
    @DirtiesContext
    public void testCountByCollection() {
        final TestObj theTestObj1 = new TestObj("a", 1, ImmutableMap.of("k1", 1, "k2", "v2"));
        final TestObj theTestObj2 = new TestObj("b", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final TestObj theTestObj3 = new TestObj("c", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final ObjCacheEntityMeta theMeta1 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text"),
                    theTestObj1);
        Assert.assertNotNull(theMeta1.getCollection());
        Assert.assertNotNull(theMeta1.getSerializerType());
        Assert.assertNotNull(theMeta1.getObjectKey());
        Assert.assertNotNull(theMeta1.getVersion());
        Assert.assertNull(theMeta1.getExpirationTime());

        final ObjCacheEntityMeta theMeta2 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test2",
                    SerializerType.JSON,
                    ImmutableMap.of("c", 1, "d", "text"),
                    theTestObj2);
        Assert.assertNotNull(theMeta2.getCollection());
        Assert.assertNotNull(theMeta2.getSerializerType());
        Assert.assertNotNull(theMeta2.getObjectKey());
        Assert.assertNotNull(theMeta2.getVersion());
        Assert.assertNull(theMeta2.getExpirationTime());

        final ObjCacheEntityMeta theMeta3 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test3",
                    SerializerType.JSON,
                    ImmutableMap.of("e", 3, "f", "text3"),
                    theTestObj3);
        Assert.assertNotNull(theMeta3.getCollection());
        Assert.assertNotNull(theMeta3.getSerializerType());
        Assert.assertNotNull(theMeta3.getObjectKey());
        Assert.assertNotNull(theMeta3.getVersion());
        Assert.assertNull(theMeta3.getExpirationTime());

        final long theTestObjsCount = objCacheService.countByCollection(TestObj.COLLECTION);
        Assert.assertEquals(theTestObjsCount, 3);
    }

    @Test
    @DirtiesContext
    public void testCountByProperties() {
        final TestObj theTestObj1 = new TestObj("a", 1, ImmutableMap.of("k1", 1, "k2", "v2"));
        final TestObj theTestObj2 = new TestObj("b", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final TestObj theTestObj3 = new TestObj("c", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final ObjCacheEntityMeta theMeta1 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text"),
                    theTestObj1);
        Assert.assertNotNull(theMeta1.getCollection());
        Assert.assertNotNull(theMeta1.getSerializerType());
        Assert.assertNotNull(theMeta1.getObjectKey());
        Assert.assertNotNull(theMeta1.getVersion());
        Assert.assertNull(theMeta1.getExpirationTime());

        final ObjCacheEntityMeta theMeta2 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test2",
                    SerializerType.JSON,
                    ImmutableMap.of("c", 1, "d", "text"),
                    theTestObj2);
        Assert.assertNotNull(theMeta2.getCollection());
        Assert.assertNotNull(theMeta2.getSerializerType());
        Assert.assertNotNull(theMeta2.getObjectKey());
        Assert.assertNotNull(theMeta2.getVersion());
        Assert.assertNull(theMeta2.getExpirationTime());

        final ObjCacheEntityMeta theMeta3 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test3",
                    SerializerType.JSON,
                    ImmutableMap.of("e", 3, "f", "text3"),
                    theTestObj3);
        Assert.assertNotNull(theMeta3.getCollection());
        Assert.assertNotNull(theMeta3.getSerializerType());
        Assert.assertNotNull(theMeta3.getObjectKey());
        Assert.assertNotNull(theMeta3.getVersion());
        Assert.assertNull(theMeta3.getExpirationTime());

        final long theTestObjsCount =
            objCacheService.countByProperties(TestObj.COLLECTION, ImmutableMap.of("a", 1, "b", "text"));
        Assert.assertEquals(theTestObjsCount, 1);
    }

    @Test
    @DirtiesContext
    public void testDelete() {
        final TestObj theTestObj1 = new TestObj("a", 1, ImmutableMap.of("k1", 1, "k2", "v2"));
        final TestObj theTestObj2 = new TestObj("b", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final TestObj theTestObj3 = new TestObj("c", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final ObjCacheEntityMeta theMeta1 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text"),
                    theTestObj1);
        Assert.assertNotNull(theMeta1.getCollection());
        Assert.assertNotNull(theMeta1.getSerializerType());
        Assert.assertNotNull(theMeta1.getObjectKey());
        Assert.assertNotNull(theMeta1.getVersion());
        Assert.assertNull(theMeta1.getExpirationTime());

        final ObjCacheEntityMeta theMeta2 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test2",
                    SerializerType.JSON,
                    ImmutableMap.of("c", 1, "d", "text"),
                    theTestObj2);
        Assert.assertNotNull(theMeta2.getCollection());
        Assert.assertNotNull(theMeta2.getSerializerType());
        Assert.assertNotNull(theMeta2.getObjectKey());
        Assert.assertNotNull(theMeta2.getVersion());
        Assert.assertNull(theMeta2.getExpirationTime());

        final ObjCacheEntityMeta theMeta3 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test3",
                    SerializerType.JSON,
                    ImmutableMap.of("e", 3, "f", "text3"),
                    theTestObj3);
        Assert.assertNotNull(theMeta3.getCollection());
        Assert.assertNotNull(theMeta3.getSerializerType());
        Assert.assertNotNull(theMeta3.getObjectKey());
        Assert.assertNotNull(theMeta3.getVersion());
        Assert.assertNull(theMeta3.getExpirationTime());

        final long theDeletedCount = objCacheService.delete(TestObj.COLLECTION, "test1");
        Assert.assertEquals(theDeletedCount, 1);
    }

    @Test
    @DirtiesContext
    public void testDeleteByCollection() {
        final TestObj theTestObj1 = new TestObj("a", 1, ImmutableMap.of("k1", 1, "k2", "v2"));
        final TestObj theTestObj2 = new TestObj("b", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final TestObj theTestObj3 = new TestObj("c", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final TestObj theTestObj4 = new TestObj("d", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final ObjCacheEntityMeta theMeta1 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text"),
                    theTestObj1);
        Assert.assertNotNull(theMeta1.getCollection());
        Assert.assertNotNull(theMeta1.getSerializerType());
        Assert.assertNotNull(theMeta1.getObjectKey());
        Assert.assertNotNull(theMeta1.getVersion());
        Assert.assertNull(theMeta1.getExpirationTime());

        final ObjCacheEntityMeta theMeta2 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test2",
                    SerializerType.JSON,
                    ImmutableMap.of("c", 1, "d", "text"),
                    theTestObj2);
        Assert.assertNotNull(theMeta2.getCollection());
        Assert.assertNotNull(theMeta2.getSerializerType());
        Assert.assertNotNull(theMeta2.getObjectKey());
        Assert.assertNotNull(theMeta2.getVersion());
        Assert.assertNull(theMeta2.getExpirationTime());

        final ObjCacheEntityMeta theMeta3 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test3",
                    SerializerType.JSON,
                    ImmutableMap.of("e", 3, "f", "text3"),
                    theTestObj3);
        Assert.assertNotNull(theMeta3.getCollection());
        Assert.assertNotNull(theMeta3.getSerializerType());
        Assert.assertNotNull(theMeta3.getObjectKey());
        Assert.assertNotNull(theMeta3.getVersion());
        Assert.assertNull(theMeta3.getExpirationTime());

        final ObjCacheEntityMeta theMeta4 =
            objCacheService
                .create("TestCol2", "test4", SerializerType.JSON, ImmutableMap.of("e", 3, "f", "text3"), theTestObj4);
        Assert.assertNotNull(theMeta4.getCollection());
        Assert.assertNotNull(theMeta4.getSerializerType());
        Assert.assertNotNull(theMeta4.getObjectKey());
        Assert.assertNotNull(theMeta4.getVersion());
        Assert.assertNull(theMeta4.getExpirationTime());

        final long theDeletedCount = objCacheService.deleteByCollection(TestObj.COLLECTION);
        Assert.assertEquals(theDeletedCount, 3);

        final long theDeletedCount2 = objCacheService.deleteByCollection("TestCol2");
        Assert.assertEquals(theDeletedCount2, 1);
    }

    @Test
    @DirtiesContext
    public void testDeleteByProperties() {
        final TestObj theTestObj1 = new TestObj("a", 1, ImmutableMap.of("k1", 1, "k2", "v2"));
        final TestObj theTestObj2 = new TestObj("b", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final TestObj theTestObj3 = new TestObj("c", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final TestObj theTestObj4 = new TestObj("d", 2, ImmutableMap.of("k1", 1, "k2", "v2", "k3", "v3"));
        final ObjCacheEntityMeta theMeta1 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test1",
                    SerializerType.JSON,
                    ImmutableMap.of("a", 1, "b", "text", "delete", true),
                    theTestObj1);
        Assert.assertNotNull(theMeta1.getCollection());
        Assert.assertNotNull(theMeta1.getSerializerType());
        Assert.assertNotNull(theMeta1.getObjectKey());
        Assert.assertNotNull(theMeta1.getVersion());
        Assert.assertNull(theMeta1.getExpirationTime());

        final ObjCacheEntityMeta theMeta2 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test2",
                    SerializerType.JSON,
                    ImmutableMap.of("c", 1, "d", "text", "delete", true),
                    theTestObj2);
        Assert.assertNotNull(theMeta2.getCollection());
        Assert.assertNotNull(theMeta2.getSerializerType());
        Assert.assertNotNull(theMeta2.getObjectKey());
        Assert.assertNotNull(theMeta2.getVersion());
        Assert.assertNull(theMeta2.getExpirationTime());

        final ObjCacheEntityMeta theMeta3 =
            objCacheService
                .create(
                    TestObj.COLLECTION,
                    "test3",
                    SerializerType.JSON,
                    ImmutableMap.of("e", 3, "f", "text3", "delete", false),
                    theTestObj3);
        Assert.assertNotNull(theMeta3.getCollection());
        Assert.assertNotNull(theMeta3.getSerializerType());
        Assert.assertNotNull(theMeta3.getObjectKey());
        Assert.assertNotNull(theMeta3.getVersion());
        Assert.assertNull(theMeta3.getExpirationTime());

        final ObjCacheEntityMeta theMeta4 =
            objCacheService
                .create("TestCol2", "test4", SerializerType.JSON, ImmutableMap.of("e", 3, "f", "text3"), theTestObj4);
        Assert.assertNotNull(theMeta4.getCollection());
        Assert.assertNotNull(theMeta4.getSerializerType());
        Assert.assertNotNull(theMeta4.getObjectKey());
        Assert.assertNotNull(theMeta4.getVersion());
        Assert.assertNull(theMeta4.getExpirationTime());

        final long theDeletedCount =
            objCacheService.deleteByProperties(TestObj.COLLECTION, ImmutableMap.of("delete", true));
        Assert.assertEquals(theDeletedCount, 2);
    }
}
