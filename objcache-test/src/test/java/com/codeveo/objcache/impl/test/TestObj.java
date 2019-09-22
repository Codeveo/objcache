/* Copyright (C) 2018 Codeveo Ltd. - All Rights Reserved
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 */
package com.codeveo.objcache.impl.test;

import java.util.Map;

public class TestObj {

    public static final String COLLECTION = "Test";

    private String propStr;

    private Integer propInt;

    private Map<String, Object> propMap;

    protected TestObj() {
    }

    public TestObj(String aPropStr, Integer aPropInt, Map<String, Object> aPropMap) {
        propStr = aPropStr;
        propInt = aPropInt;
        propMap = aPropMap;
    }

    public String getPropStr() {
        return propStr;
    }

    public Integer getPropInt() {
        return propInt;
    }

    public Map<String, Object> getPropMap() {
        return propMap;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((propInt == null) ? 0 : propInt.hashCode());
        result = prime * result + ((propMap == null) ? 0 : propMap.hashCode());
        result = prime * result + ((propStr == null) ? 0 : propStr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TestObj other = (TestObj) obj;
        if (propInt == null) {
            if (other.propInt != null)
                return false;
        } else if (!propInt.equals(other.propInt))
            return false;
        if (propMap == null) {
            if (other.propMap != null)
                return false;
        } else if (!propMap.equals(other.propMap))
            return false;
        if (propStr == null) {
            if (other.propStr != null)
                return false;
        } else if (!propStr.equals(other.propStr))
            return false;
        return true;
    }

}
