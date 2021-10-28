select a.id, a.first_name, sum(t.amount) total
from accounts a join transfers t on a.id = t.source_id
where t.transfer_time >= '2019-01-01'
group by a.id
having sum(t.amount) > 1000