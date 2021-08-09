SELECT t.source_id FROM Transfers AS t
WHERE t.transfer_time >= TIMESTAMP '2019-01-01' AND source_id != target_id AND amount > 0
GROUP BY source_id
HAVING SUM(amount) > 1000;
