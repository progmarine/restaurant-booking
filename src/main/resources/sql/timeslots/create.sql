CREATE TABLE IF NOT EXISTS timeslots (
  id SERIAL PRIMARY KEY,
  time TIMESTAMP,
  party_size INTEGER,
  restaurant_id VARCHAR,
  is_available BOOLEAN
);