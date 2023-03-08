CREATE TABLE IF NOT EXISTS reviews (
  id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  restaurant_id VARCHAR NOT NULL,
  rating INTEGER NOT NULL,
  comment TEXT
);