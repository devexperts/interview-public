CREATE TABLE accounts
(
    id bigint NOT NULL,
    firstName character varying(255),
    lastName character varying(255),
	balance DOUBLE PRECISION,
    
    CONSTRAINT accounts_pkey PRIMARY KEY (id)
);