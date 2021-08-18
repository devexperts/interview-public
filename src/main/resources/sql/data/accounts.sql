CREATE TABLE "accounts" (
    "id" INTEGER NOT NULL,
    "first_name" VARCHAR(50) NULL DEFAULT NULL,
    "last_name" VARCHAR(50) NULL DEFAULT NULL,
    "balance" INTEGER NULL DEFAULT NULL,
    PRIMARY KEY ("id")
);