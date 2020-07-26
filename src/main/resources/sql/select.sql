SELECT SOURCE_ID
FROM TRANSFERS t
         JOIN ACCOUNTS a_source
              ON t.SOURCE_ID = a_source.ID
         JOIN ACCOUNTS a_target
              ON t.TARGET_ID = a_target.ID
WHERE (a_source.FIRST_NAME <> a_target.FIRST_NAME OR a_source.LAST_NAME <> a_target.LAST_NAME)
  AND TRANSFER_TIME >= '2019-01-01'
GROUP BY SOURCE_ID
HAVING SUM(AMOUNT) > 1000