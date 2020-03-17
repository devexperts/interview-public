create table ACCOUNTS_TABLE (
    account_id number not null,
    account_key number not null,
    first_name varchar2(20) not null,
    last_name varchar2(20) not null,
    balance number default 0 not null 
);

alter table ACCOUNTS_TABLE
	add constraint account_id_pk PRIMARY KEY (
		account_id
	)
/
	
CREATE SEQUENCE ACCOUNTS_TABLE_S
  MINVALUE 1
  MAXVALUE 9999999999999999
/
	
CREATE TRIGGER trigger_accounts_table_id before insert on ACCOUNTS_TABLE
for each row
begin
	select ACCOUNTS_TABLE_S.nextval into :new.account_id from dual;
end;
/