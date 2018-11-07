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

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"com.codeveo.objcache"},
    includeFilters = {
        @Filter(type = FilterType.REGEX, pattern = "com.codeveo.objcache.*.impl.entity.*Entity"),
        @Filter(type = FilterType.REGEX, pattern = "com.codeveo.objcache.*.impl.repository.*Repository") })
@EnableAutoConfiguration(
    exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class })
@Import(ObjCacheStarterPropertiesConfig.class)
public class ObjCacheTxConfigComponent {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        final JpaProperties aJpaProperties,
        final DataSource aDataSource) {
        final LocalContainerEntityManagerFactoryBean theEntityManagerFactoryBean =
            new LocalContainerEntityManagerFactoryBean();
        theEntityManagerFactoryBean.setDataSource(aDataSource);
        theEntityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        theEntityManagerFactoryBean.setPackagesToScan("com.codeveo.objcache");

        final Properties theProperties = new Properties();
        theProperties.put("hibernate.dialect", aJpaProperties.getProperties().get("hibernate.dialect"));
        theProperties.put("hibernate.show_sql", aJpaProperties.isShowSql());
        theProperties.put("hibernate.hbm2ddl.auto", aJpaProperties.getHibernate().getDdlAuto());

        theEntityManagerFactoryBean.setJpaProperties(theProperties);

        return theEntityManagerFactoryBean;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public JpaTransactionManager transactionManager(
        final LocalContainerEntityManagerFactoryBean anEntityManagerFactory) {
        final JpaTransactionManager theTransactionManager = new JpaTransactionManager();
        theTransactionManager.setEntityManagerFactory(anEntityManagerFactory.getObject());

        return theTransactionManager;
    }
}
