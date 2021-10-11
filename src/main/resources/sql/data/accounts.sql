create table accounts
(
    id         int8         not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    balance    int8         not null,
    primary key (id)
);