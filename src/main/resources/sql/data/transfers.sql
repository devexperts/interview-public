CREATE TABLE Transfers (
    ID bigint NOT NULL,
    SOURCE_ID bigint,
    TARGET_ID bigint,
    AMOUNT double,
    TRANSFER_TIME date,
    PRIMARY KEY (ID),
    CONSTRAINT FK_SourceAccountTransfers FOREIGN KEY (SOURCE_ID)
    REFERENCES Accounts(ID),
    CONSTRAINT FK_TargetAccountTransfers FOREIGN KEY (TARGET_ID)
    REFERENCES Accounts(ID)
);