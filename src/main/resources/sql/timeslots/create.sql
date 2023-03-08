CREATE TABLE IF NOT EXISTS timeslots (
  id SERIAL PRIMARY KEY,
  time TIMESTAMP,
  restaurant_id VARCHAR,
  is_available BOOLEAN
);