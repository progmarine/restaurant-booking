CREATE TABLE IF NOT EXISTS restaurants (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR NOT NULL,
     reviews JSONB[],
     address VARCHAR NOT NULL,
     phone VARCHAR,
     available_time_slots TIMESTAMP[]
);