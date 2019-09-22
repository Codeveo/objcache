/*******************************************************************************
 * Copyright 2018 Codeveo Ltd.
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.codeveo.objcache.common;

public enum ObjCacheErrorCodeType {
    /**
     * Internal error
     */
    OBJCACHE_EC_0000,

    /**
     * Unsupported operation or service
     */
    OBJCACHE_EC_0001,

    /**
     * OLC exception
     */
    OBJCACHE_EC_0002,

    /**
     * Object serialisation error
     */
    OBJCACHE_EC_0003,

    /**
     * Object deserialisation error
     */
    OBJCACHE_EC_0004,

    /**
     * Object persisting error
     */
    OBJCACHE_EC_0005,

    /**
     * Query error
     */
    OBJCACHE_EC_0006,

    /**
     * Multiple object update
     */
    OBJCACHE_EC_0007
}
