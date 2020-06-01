select t.source_id from transfers t
where t.transfer_time >= timestamp '2019-01-01' and source_id != target_id and amount > 0
group by source_id
having sum(amount) > 1000;