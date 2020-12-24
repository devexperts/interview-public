create table transfers
(
	id bigint NOT NULL,
	source_id id not null references accounts,
	target_id id not null references accounts,
	amount DOUBLE PRESCISION not null,
	transfer_time TIMESTAMP not null
	CONSTRAINT transfers_pkey PRIMARY KEY (id)
);