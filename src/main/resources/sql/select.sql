SELECT * FROM accounts 
WHERE id IN 
	(SELECT id FROM transfers 
	WHERE transfer_time >= '2019-01-01' 
	HAVING SUM(amount) >= 1000
	SORT BY transfer_time
	);