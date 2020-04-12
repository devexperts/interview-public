CREATE TABLE Accounts (
    accountId IDENTITY PRIMARY KEY,
    firstName VARCHAR (255) NOT NULL,
    lastName VARCHAR (255) NOT NULL,
    balance DOUBLE NOT NULL
);