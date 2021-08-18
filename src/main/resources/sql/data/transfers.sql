CREATE TABLE "transfers" (
    "id" INTEGER NOT NULL,
    "source_id" INTEGER,
    "target_id" INTEGER,
    "amount" INTEGER,
    "transfer_time" TIMESTAMP,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("source_id")
        REFERENCES "accounts" ("id"),
    FOREIGN KEY ("target_id")
        REFERENCES "accounts" ("id")
)
;