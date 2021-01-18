CREATE SEQUENCE seq_accounts;

CREATE TABLE accounts
(
    id bigint NOT NULL DEFAULT nextval('seq_accounts'),
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    amount double precision NOT NULL DEFAULT 0,
    CONSTRAINT pk_account_id PRIMARY KEY (id)
)
WITH (
  OIDS = FALSE
);