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
package com.codeveo.objcache.jpa.impl;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ ObjCacheStarterPropertiesConfig.class })
public class ObjCachePostgresqlConfigComponent {

    @Bean
    public DataSource dataSourcePostgresql(final DataSourceProperties aDataSourceProperties) {
        final String theUrl = aDataSourceProperties.getUrl();
        return DataSourceBuilder
            .create()
            .username(aDataSourceProperties.getUsername())
            .password(aDataSourceProperties.getPassword())
            .driverClassName(aDataSourceProperties.getDriverClassName())
            .url(theUrl)
            .build();
    }

}
