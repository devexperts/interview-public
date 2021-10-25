create table accounts (
    id int8 not null,
    first_name varchar(250) not null,
    last_name varchar(250),
    balance money,
    constraint accounts_pkey primary key (id)
);
