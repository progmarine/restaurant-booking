CREATE TABLE IF NOT EXISTS booking (
  id SERIAL PRIMARY KEY,
  user_id BIGINT,
  restaurant_id VARCHAR,
  time_slot_id BIGINT,
  party_size INTEGER,
  special_request TEXT,
  status VARCHAR
);