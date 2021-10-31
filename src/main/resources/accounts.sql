CREATE TABLE accounts
(
    id              BIGINT                      NOT NULL,
    first_name      VARCHAR(255)                NOT NULL,
    last_name       VARCHAR(255)                NOT NULL,
    balance         BIGINT                      NOT NULL,
    CONSTRAINT accounts_pkey2 PRIMARY KEY (id)
);