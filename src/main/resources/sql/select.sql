SELECT transfers.source_id FROM transfers
WHERE (transfers.transfer_time >= TIMESTAMP '2019-01-01')
GROUP BY transfers.source_id HAVING SUM(transfers.amount) > 1000;
