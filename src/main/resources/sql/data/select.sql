SELECT ac.accountId FROM Accounts AS ac
    INNER JOIN Transfers AS tr WHERE ac.accountId = tr.sourceId
    AND transfers.transferTime >= 2019-01-01
    GROUP BY tr.sourceId
    HAVING SUM(tr.amount) > 1000.00