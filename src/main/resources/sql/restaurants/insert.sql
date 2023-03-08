INSERT
INTO restaurants (name, address, phone, available_time_slots)
VALUES ('Mono A', '123 Main St.', '555-1234', ARRAY['2023-03-04 13:10:20.281964', '2023-03-04 14:10:20.281964', '2023-03-04 15:10:20.281964']::timestamp[])
ON CONFLICT DO NOTHING;
