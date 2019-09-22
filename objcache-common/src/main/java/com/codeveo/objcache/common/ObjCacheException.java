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

import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class ObjCacheException extends RuntimeException {

    private static final long serialVersionUID = 1885297950650395263L;

    private static ResourceBundle errorCatalog;

    static {
        errorCatalog = ResourceBundle.getBundle("objcache/ObjCacheErrorCatalog");
    }

    private final ObjCacheErrorCodeType errorCode;

    private final Object[] causeArgs;

    public ObjCacheException(final ObjCacheErrorCodeType anErrorCode, final Object... someCauseArgs) {
        this(anErrorCode, null, someCauseArgs);
    }

    public ObjCacheException(final Throwable aThrowable) {
        this(ObjCacheErrorCodeType.OBJCACHE_EC_0000, aThrowable, "-");
    }

    public ObjCacheException(
        final ObjCacheErrorCodeType anErrorCode,
        final Throwable aThrowable,
        final Object... someCauseArgs) {
        super(aThrowable);
        errorCode = anErrorCode;
        causeArgs = someCauseArgs;
    }

    public ObjCacheErrorCodeType getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        final String theErrorKey = errorCode.name();
        final boolean hasKey = errorCatalog.containsKey(theErrorKey);
        if (!hasKey) {
            return "Missing error message for key '"
                + theErrorKey
                + "' (cause args: "
                + Arrays.toString(causeArgs)
                + ")";
        }

        return errorCatalog.getString(errorCode.name());
    }

    public String getCauseString() {
        final String theCauseErrorKey = errorCode.name() + "_CAUSE";
        final boolean hasKey = errorCatalog.containsKey(theCauseErrorKey);
        if (!hasKey) {
            return "Missing error cause template for key '"
                + theCauseErrorKey
                + "' (cause args: "
                + Arrays.toString(causeArgs)
                + ")";
        }

        final String theCauseTmpl = errorCatalog.getString(theCauseErrorKey);
        final int theArgsCount = StringUtils.countMatches(theCauseTmpl, "%s");
        final Object[] theCauseArgs =
            theArgsCount > causeArgs.length ? Arrays.copyOf(causeArgs, theArgsCount) : causeArgs;

        return String.format(theCauseTmpl, theCauseArgs);
    }

    @Override
    public String toString() {
        return getClass().getName() + ':' + errorCode.name() + ':' + Arrays.toString(causeArgs);
    }
}
