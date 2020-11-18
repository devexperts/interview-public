select a.id account, sum(t.amount) total
from accounts a,
transfers t
where t.source_id = a.id
and t.transfer_time > '2019-01-01'
group by a.id
having sum(t.amount) >= 1000;




