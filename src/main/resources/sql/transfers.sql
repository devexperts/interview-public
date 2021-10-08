create table if not exists transfers
(
    id            int8 not null,
    source_id     int8 not null,
    target_id     int8 not null,
    amount        int8 not null,
    transfer_time timestamp not null,
    primary key (id)
);

alter table if exists transfers add constraint transfers_source_id_fk foreign key (source_id) references accounts(id);

alter table if exists transfers add constraint transfers_target_id_fk foreign key (target_id) references accounts(id);