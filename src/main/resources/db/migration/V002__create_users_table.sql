CREATE SEQUENCE IF NOT EXISTS "restaurant-manager".user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS "restaurant-manager".users (
    id          BIGINT        NOT NULL DEFAULT nextval('"restaurant-manager".user_seq'),
    uuid        UUID          NOT NULL DEFAULT gen_random_uuid(),
    name        VARCHAR(100)  NOT NULL,
    email       VARCHAR(100)  NOT NULL,
    type_id        BIGINT   NOT NULL,
    login       VARCHAR(50)   NOT NULL,
    active      BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT users_pkey         PRIMARY KEY (id),
    CONSTRAINT fk_user_type FOREIGN KEY (type_id) REFERENCES "restaurant-manager".user_types(id),
    CONSTRAINT users_uuid_unique UNIQUE (uuid),
    CONSTRAINT users_email_unique UNIQUE (email),
    CONSTRAINT users_login_unique UNIQUE (login)
);


INSERT INTO "restaurant-manager".users (name, email, type_id, login, active)
VALUES
    ('John Doe', 'john.doe@example.com', 1, 'john.doe', TRUE),
    ('Jane Smith', 'jane.smith@example.com', 2, 'jane.smith', TRUE)
ON CONFLICT (email) DO NOTHING;

