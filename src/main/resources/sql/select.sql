select *
from Accounts a
where a.ID in (
	select t.SOURCE_ID from Transfers t
	where t.TRANSFER_TIME > '2019-01-10'
	group by t.SOURCE_ID
	having sum(t.AMOUNT) > 1000
)