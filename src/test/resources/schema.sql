CREATE SCHEMA if not exists aston;

CREATE TABLE if not exists aston.users (id BIGSERIAL PRIMARY KEY, name VARCHAR NOT NULL , email VARCHAR, age INT, created_at DATE);