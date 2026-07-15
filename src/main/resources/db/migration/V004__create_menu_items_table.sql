CREATE SEQUENCE IF NOT EXISTS "restaurant-manager".menu_items_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS "restaurant-manager".menu_items (
    id BIGINT NOT NULL DEFAULT nextval('"restaurant-manager".menu_items_seq'),
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    price NUMERIC(14,2) NOT NULL,
    local_only BOOLEAN NOT NULL,
    photo_path VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT menu_items_pkey         PRIMARY KEY (id),
    CONSTRAINT menu_items_uuid_unique UNIQUE (uuid),
    CONSTRAINT fk_menuitem_restaurant FOREIGN KEY (restaurant_id) REFERENCES "restaurant-manager".restaurants(id) ON DELETE CASCADE
);

INSERT INTO "restaurant-manager".menu_items (restaurant_id, name, description, price, local_only, photo_path)
VALUES
    (1, 'Margherita Pizza', 'Tomato sauce, mozzarella and basil', 42.90, FALSE, '/images/margherita-pizza.jpg'),
    (2, 'Grilled Salmon', 'Grilled salmon with herbs and lemon', 58.50, FALSE, '/images/grilled-salmon.jpg')
ON CONFLICT (uuid) DO NOTHING;

