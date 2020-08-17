select source_id
from transfers
where transfer_date > '2019 01 01'
group by source_id
having sum(amount) > 1000;