/* Copyright (C) 2018 Codeveo Ltd. - All Rights Reserved
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Ladislav Klenovic <lklenovic@codeveo.com>
 */
package com.codeveo.objcache.impl.test;

import java.io.Serializable;

public class TestObj implements Serializable {

    private static final long serialVersionUID = -6316008715744705836L;

    private final String prop1 = "aaa";

    private final String prop2 = "bbb";

    private final String prop3 = "ccc";

    public TestObj() {
    }

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public String getProp3() {
        return prop3;
    }
}
