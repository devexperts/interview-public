CREATE TABLE IF NOT EXISTS devex.accounts (
   id NUMERIC(19) PRIMARY KEY,
   first_name VARCHAR(250) NOT NULL,
   last_name VARCHAR(250) NOT NULL,
   balance NUMERIC(9,2) NOT NULL
);