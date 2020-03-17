SELECT a.account_id from ACCOUNTS_TABLE a, TRANSFERS_TABLE b
where a.account_id = b.source_id
and b.transfer_time > to_date('20190101','YYYYMMDD')
GROUP BY a.account_id
HAVING SUM(amount)>1000;