CREATE SEQUENCE seq_transfers;

CREATE TABLE transfers
(
   id bigint NOT NULL DEFAULT nextval('seq_transfers'),
   source_id bigint NOT NULL,
   target_id bigint NOT NULL,
   amount double precision NOT NULL,
   transfer_time timestamp without time zone NOT NULL DEFAULT now(),
   CONSTRAINT pk_transfer_id PRIMARY KEY (id),
   CONSTRAINT fk_source_id FOREIGN KEY (source_id) REFERENCES accounts (id),
   CONSTRAINT fk_target_id FOREIGN KEY (target_id) REFERENCES accounts (id)
)
WITH (
  OIDS = FALSE
);