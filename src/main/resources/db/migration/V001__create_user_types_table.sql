CREATE SCHEMA IF NOT EXISTS "restaurant-manager";
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SEQUENCE IF NOT EXISTS "restaurant-manager".user_types_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS "restaurant-manager".user_types (
    id BIGINT NOT NULL DEFAULT nextval('"restaurant-manager".user_types_seq'),
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT user_types_pkey         PRIMARY KEY (id),
    CONSTRAINT user_types_uuid_unique UNIQUE (uuid)
);

INSERT INTO "restaurant-manager".user_types (name)
VALUES
    ('OWNER'),
    ('CUSTOMER')
ON CONFLICT (name) DO NOTHING;

