create table if not exists transfers
(
	id uuid not null
		constraint transfers_pk
			primary key,
	source_id uuid not null
		constraint transfers_accounts_id_fk
			references accounts,
	target_id uuid not null
		constraint transfers_accounts_id_fk_2
			references accounts,
	amount numeric not null,
	transfer_date timestamp with time zone not null
);

comment on table transfers is 'Table for transfers records';

create index if not exists transfers_source_id_index
	on transfers (source_id);