create table transfers (
    id int8 not null,
    source_id int8 not null,
    target_id int8 not null,
    amount money not null,
    transfer_time timestamp not null,
    constraint transfers_pkey primary key (id)
);
