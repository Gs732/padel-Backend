CREATE ROLE padel_owner    LOGIN PASSWORD 'owner_pwd';
CREATE ROLE padel_app      LOGIN PASSWORD 'app_pwd';
CREATE ROLE padel_readonly LOGIN PASSWORD 'readonly_pwd';

ALTER DATABASE padel OWNER TO padel_owner;
ALTER SCHEMA public OWNER TO padel_owner;

GRANT CONNECT ON DATABASE padel TO padel_app, padel_readonly;
GRANT USAGE ON SCHEMA public    TO padel_app, padel_readonly;

ALTER DEFAULT PRIVILEGES FOR ROLE padel_owner IN SCHEMA public
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO padel_app;
ALTER DEFAULT PRIVILEGES FOR ROLE padel_owner IN SCHEMA public
  GRANT USAGE, SELECT ON SEQUENCES TO padel_app;
ALTER DEFAULT PRIVILEGES FOR ROLE padel_owner IN SCHEMA public
  GRANT SELECT ON TABLES TO padel_readonly;