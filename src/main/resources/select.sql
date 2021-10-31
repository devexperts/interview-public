select source.first_name, concat(coalesce(sum(transfer.amount), 0), ' $') as total_sent
from accounts source inner join transfers transfer on source.id = transfer.source_id
where transfer.transfer_time > '2011-01-01'
group by source.id
having coalesce(sum(transfer.amount), 0) > 1000;