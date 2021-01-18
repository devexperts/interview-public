INSERT INTO accounts(first_name, last_name, amount) VALUES ('Ivan', 'Petrov', 1000.0);
INSERT INTO accounts(first_name, last_name, amount) VALUES ('Sergey', 'Lunin', 500.0);
INSERT INTO accounts(first_name, last_name, amount) VALUES ('Dmitriy', 'Sidorov', 100.0);
INSERT INTO accounts(first_name, last_name, amount) VALUES ('Anton', 'Rubin', 700.0);
INSERT INTO accounts(first_name, last_name, amount) VALUES ('Denis', 'Denisov', 300.0);

/* - */
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (5, 4, 500, to_timestamp('12-07-2018 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (5, 2, 700, to_timestamp('12-08-2018 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));

/* + */
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (1, 3, 600, to_timestamp('12-08-2019 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (1, 4, 600, to_timestamp('12-03-2019 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));

/* - */
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (2, 3, 300, to_timestamp('12-01-2019 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (2, 1, 300, to_timestamp('12-04-2019 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (2, 5, 300, to_timestamp('12-06-2019 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));

/* + */
INSERT INTO transfers(source_id, target_id, amount, transfer_time) VALUES (3, 5, 1100, to_timestamp('12-10-2019 15:15:15', 'dd-mm-yyyy hh24:mi:ss'));