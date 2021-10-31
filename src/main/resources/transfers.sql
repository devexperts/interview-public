CREATE TABLE transfers
(
    id                  BIGINT                              NOT NULL,
    source_id           BIGINT                              NOT NULL,
    target_id           BIGINT                              NOT NULL,
    amount              BIGINT                              NOT NULL,
    transfer_time       TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    CONSTRAINT transfers_pkey2 PRIMARY KEY (id)
);

ALTER TABLE transfers ADD CONSTRAINT transfers_source_id FOREIGN KEY (source_id) REFERENCES accounts (id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE transfers ADD CONSTRAINT transfers_target_id FOREIGN KEY (target_id) REFERENCES accounts (id) ON UPDATE CASCADE ON DELETE RESTRICT;