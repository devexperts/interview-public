create table transfers(ID int, SOURCE_ID int, TARGET_ID int, AMOUNT int, TRANSFER_TIME DATE);

insert into transfers values(1, 1, 2, 500, '2019-01-01');
insert into transfers values(2, 1, 3, 2, '2019-01-02');
insert into transfers values(3, 1, 2, 500, '2019-01-03');