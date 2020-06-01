create table transfers (
    id            bigint primary key,
    source_id     bigint    not null,
    target_id     bigint    not null,
    amount        numeric   not null,
    transfer_time timestamptz not null,
    constraint transfers_source_id_fk foreign key (SOURCE_ID) references accounts,
    constraint transfers_target_id_fk foreign key (TARGET_ID) references accounts
);

create index transfers_transfer_time_idx on transfers(transfer_time);