create table TRANSFERS 
(
ID int primary key, 
SOURCE_ID int references ACCOUNTS(ID), 
TARGET_ID int references ACCOUNTS(ID), 
AMOUNT double, 
TRANSFER_TIME datetime
);