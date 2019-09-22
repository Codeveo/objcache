CREATE DATABASE test_cdv_objcache
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.utf8'
       LC_CTYPE = 'en_US.utf8'
       CONNECTION LIMIT = -1;
       
GRANT CONNECT, TEMPORARY ON DATABASE test_cdv_objcache TO public;
