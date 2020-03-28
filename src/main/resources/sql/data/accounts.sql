create table accounts
(
    ID         LONG AUTO_INCREMENT Primary Key,
    FIRST_NAME varchar(255) NOT NULL,
    LAST_NAME  varchar(255) NOT NULL,
    BALANCE    DOUBLE NOT NULL
);