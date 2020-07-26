INSERT INTO ACCOUNTS (FIRST_NAME, LAST_NAME, BALANCE)
VALUES ('Сергей', 'Иванов', 170000.34),
       ('Николай', 'Гумилёв', 20000),
       ('Дарт', 'Вейдер', 7000000.07),
       ('Сергей', 'Иванов', 4500000);

INSERT INTO TRANSFERS (SOURCE_ID, TARGET_ID, AMOUNT, TRANSFER_TIME)
VALUES (1, 3, 999, '2019-02-23'),
       (1, 4, 2000, '2020-07-04'),
       (1, 2, 760, '208-01-08'),
       (4, 3, 10000, '2020-07-25')