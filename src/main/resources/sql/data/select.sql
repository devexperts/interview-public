SELECT accounts.ID, accounts.FIRST_NAME, accounts.LAST_NAME
FROM accounts, transfers
WHERE transfers.SOURCE_ID = accounts.ID
AND transfers.TRANSFER_TIME >= 2019-01-01
GROUP BY accounts.ID
HAVING SUM(transfers.AMOUNT) > 1000.00
