# Welcome to the Devexperts Interview

## How this programming assignment is organized
We'll go through several small tasks that implement logic of the same business domain,
however they are loosely coupled, which means being unable to complete one task
doesn't necessarily mean that further tasks can't be completed without it.

**NOTE: In order to finish the assignment you need to submit a pull request with as many tasks completed as you can.**

## How project is organized
You can find here both maven and gradle project files, so feel free to import
the one you're more comfortable with. Both of them contain minimal required dependencies:
- Java 8
- SpringBoot 5
- JUnit 4/5

## What we're going to build
We're trying to build a Proof of Concept type of a banking system.
For starters we'll have just a naive approximation of a real `Account` entity in our system
and a service level interface `com.devexperts.service.AccountService.getAccount`.
You can consider it to be an in-memory account cache that can also perform some operations.

Note that in the scope of this assignment we don't care about any persistence layer,
where and how these accounts are actually stored and how performed operations on these accounts
are going to be reflected there. For the sake of simplicity let's assume it's a given.

So what we want to achieve is to store the accounts in some data structure in the memory of our application.
And then build some business logic operations on top of that.

**Note:**
Please, avoid moving and renaming `Account`, `AccountKey` and `AccountService` classes as well as
changing signature of any methods of these classes.
However, you're free to introduce new modules, packages, classes, methods and/or override existing ones.

# Task 1
Let's assume one of your colleagues was given a task to provide implementation for
`AccountService.clear`, `AccountService.createAccount` and `AccountService.getAccount` methods.
In particular, it was required to do so in the most efficient way possible.

**Goal:** Please, perform code review and change implementation accordingly.

**Hint:** Consider performance of `AccountService.getAccount` method.


# Task 2
We want to start expanding our business logic, and for now most important feature is to allow money transfers
between accounts. 

**Goal:** Implement `AccountService.transfer`

**Hint:** Assume we're in a single-threaded environment

# Task 3
Assume that transfers now can go from different client applications and threads.
Please, rework your implementation of single-threaded `AccountService.transfer` to work in the multi-threaded environment 

**Goal:** Have money transfer between accounts working in concurrent environment

**Note:** You can safely assume that `AccountService` is the only place where accounts are accessed

# Task 4
Create a REST service for `transfer` method. It should still operate with accounts in-memory

**Goal:** Have money transfer between accounts accessible to the outside world

**Details:**
The operation should be available at `POST localhost:8080/api/operations/transfer` with required query parameters:
*source_id*, *target_id*, *amount*.
Response codes are the following:
- 200 (OK) - successful transfer
- 400 (Bad Request) -  one of the parameters in not present or amount is invalid
- 404 (Not Found) - account is not found
- 500 (Internal Server Error) - insufficient account balance

# Task 5
Create 2 tables for accounts and corresponding transfers. Accounts column set should correspond to java code:
- ID
- FIRST_NAME
- LAST_NAME
- BALANCE

Same goes for transfers, however one additional column ('TRANSFER_TIME') representing datetime of transfer
should be added:
- ID
- SOURCE_ID
- TARGET_ID
- AMOUNT
- TRANSFER_TIME

**Goal:** Provide SQL to create both tables (`resources/sql/data/accounts.sql`) and (`resources/sql/data/transfers.sql`)
and sql query (`resources/sql/select.sql`) that finds all accounts
 that in total transferred more than 1000$ to other people starting from 2019-01-01 