SELECT source_id FROM transfers GROUP BY source_id HAVING SUM(amount)>=1000 WHERE transfer_time>'2019-01-01'::timestamp;