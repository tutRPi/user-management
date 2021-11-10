CREATE USER admin;
CREATE DATABASE user_management;
GRANT ALL PRIVILEGES ON DATABASE user_management TO admin;
GRANT ALL PRIVILEGES ON DATABASE user_management TO postgres;

-- INSERT INTO public.tbl_roles (name, created_on, deleted_on) VALUES ('ROLE_ADMIN', NOW(), null);
-- INSERT INTO public.tbl_roles (name, created_on, deleted_on) VALUES ('ROLE_USER', NOW(), null);