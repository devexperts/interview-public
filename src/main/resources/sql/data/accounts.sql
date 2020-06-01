create table accounts (
    id         bigint primary key,
    first_name text    not null,
    last_name  text    not null,
    balance    numeric not null
);