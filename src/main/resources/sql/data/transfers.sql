CREATE TABLE transfers (
    id BIGINT PRIMARY KEY,
    source_id BIGINT NOT NULL REFERENCES accounts(id),
    target_id BIGINT NOT NULL REFERENCES accounts(id),
    amount NUMERIC NOT NULL,
    transfer_time TIMESTAMP NOT NULL
);