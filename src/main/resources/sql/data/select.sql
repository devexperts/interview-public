-- Find all accounts that in total transferred more than 1000$ to other people starting from 2019-01-10

SELECT *
FROM Accounts a
WHERE a.ID IN (
	SELECT t.SOURCE_ID FROM Transfers t
	WHERE t.TRANSFER_TIME > '2019-01-10'
	GROUP BY t.SOURCE_ID
	HAVING SUM(t.AMOUNT) > 1000
)