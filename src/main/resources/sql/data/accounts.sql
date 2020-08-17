create table if not exists accounts
(
	id uuid not null
		constraint accounts_pk
			primary key,
	first_name text not null,
	last_name text not null,
	balance numeric not null
);

comment on table accounts is 'Table for storing information about bank accounts';

