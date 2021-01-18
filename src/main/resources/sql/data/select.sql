SELECT
    source_id
FROM
    transfers
WHERE
    transfer_time > '2019-01-01'::timestamp
GROUP BY
    source_id
HAVING
    sum(amount) > 1000;