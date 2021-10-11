select source_id
from transfers
where transfer_time > '2019-01-01' :: timestamp
group by source_id
having sum(ammount) > 1000;