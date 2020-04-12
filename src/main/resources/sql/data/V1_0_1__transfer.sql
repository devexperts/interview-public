CREATE TABLE Transfers (
    id IDENTITY PRIMARY KEY,
    sourceId long NOT NULL,
    targetId long NOT NULL,
    amount DOUBLE NOT NULL,
    transferTime TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (sourceId) REFERENCES Accounts(accountId),
    FOREIGN KEY (targetId) REFERENCES Accounts(accountId)
);