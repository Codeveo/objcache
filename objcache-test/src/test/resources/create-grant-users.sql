DROP USER IF EXISTS testobjcache;
CREATE USER testobjcache WITH PASSWORD 'testobjcache';
GRANT ALL ON DATABASE test_cdv_objcache TO postgres;
GRANT ALL ON DATABASE test_cdv_objcache TO testobjcache;
ALTER DATABASE test_cdv_objcache OWNER TO testobjcache;