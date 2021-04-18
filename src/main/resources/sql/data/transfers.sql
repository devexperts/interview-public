CREATE TABLE IF NOT EXISTS Transfers  (
  ID SERIAL PRIMARY KEY NOT NULL,
  SOURCE_ID INTEGER references Accounts(ID),
  TARGET_ID INTEGER references Accounts(ID),
  AMOUNT double precision,
  TRANSFER_TIME timestamp NOT NULL
);