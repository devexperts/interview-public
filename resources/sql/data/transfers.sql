create table TRANSFERS_TABLE (
    transfer_id number not null,
    source_id number not null,
    target_id number not null,
    amount number not null,
    transfer_time DATE not null 
);

alter table TRANSFERS_TABLE
	add constraint transfer_id_pk PRIMARY KEY (
		transfer_id
	)
/
	
CREATE SEQUENCE TRANSFERS_TABLE_S
  MINVALUE 1
  MAXVALUE 9999999999999999
/
	
CREATE TRIGGER trigger_transfers_table_id before insert on TRANSFERS_TABLE
for each row
begin
	select TRANSFERS_TABLE_S.nextval into :new.transfer_id from dual;
end;
/