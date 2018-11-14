# ObjCache #

Simple object store. Each object is stored under given collection and with a given key. Stored objects contain properties which can be used for object search as well.

### Prerequisites ###
* Postgresql database

### Setup ###

* Run SQL script *schema-postgresql.sql* located in project *objcache-config*. The script creates
  table with a name *t_objcache*, *sequence* and appropriate *indexes* (see script for details).
* Spring configuration sample:

````xml
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans" xmlns:jdbc="http://www.springframework.org/schema/jdbc"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-3.0.xsd">
    	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        	<property name="driverClassName" value="org.postgresql.Driver" />
        	<property name="url" value="jdbc:postgresql://localhost:5432/test_cdv_objcache" />
        	<property name="username" value="testobjcache" />
        	<property name="password" value="testobjcache" />
    	</bean>

    	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        	<property name="dataSource" ref="dataSource" />
    	</bean>

    	<bean id="txTemplate" class="org.springframework.transaction.support.TransactionTemplate">
	        <property name="transactionManager" ref="txManager" />
	    </bean>

    	<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        	<property name="dataSource" ref="dataSource" />
    	</bean>

    	<bean id="ObjCacheSerDerFactory" class="com.codeveo.objcache.impl.ObjCacheSerializerDeserializerFactoryImpl" />

    	<bean id="objCacheService" class="com.codeveo.objcache.impl.ObjCacheServiceImpl">
        	<constructor-arg ref="txTemplate" />
        	<constructor-arg ref="jdbcTemplate" />
        	<constructor-arg ref="ObjCacheSerDerFactory" />
    	</bean>
	</beans>
````
### Built With ###
[Maven](https://maven.apache.org/) - Dependency Management

### Authors ###
Ladislav Klenovic - [Codeveo](http://www.codeveo.com/)

### License ###
This project is licensed under the Apache License v2.0 - see the *LICENSE* file for details