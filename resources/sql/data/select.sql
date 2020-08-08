select *from ACCOUNTS where ID in 
	(select SOURCE_ID from 
		(select SOURCE_ID, sum (AMOUNT) as SUMM from 
			(select *from TRANSFERS where TRANSFER_TIME > '2019-01-01') 
		group by SOURCE_ID) 
	where SUMM >= 1000);