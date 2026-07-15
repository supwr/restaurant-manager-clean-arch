CREATE SEQUENCE IF NOT EXISTS "restaurant-manager".restaurants_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS "restaurant-manager".restaurants (
    id BIGINT NOT NULL DEFAULT nextval('"restaurant-manager".restaurants_seq'),
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    address VARCHAR(500) NOT NULL,
    cuisine_type VARCHAR(100) NOT NULL,
    opening_hours VARCHAR(200) NOT NULL,
    owner_user_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT restaurants_pkey         PRIMARY KEY (id),
    CONSTRAINT restaurants_uuid_unique UNIQUE (uuid)
);

INSERT INTO "restaurant-manager".restaurants (name, address, cuisine_type, opening_hours, owner_user_id)
VALUES
    ('The Green Table', '123 Main Street', 'Italian', 'Mon-Sun 11:00-22:00', 1),
    ('Ocean Breeze', '456 Harbor Avenue', 'Seafood', 'Tue-Sun 12:00-23:00', 2)
ON CONFLICT (uuid) DO NOTHING;

