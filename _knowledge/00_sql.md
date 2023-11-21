### 1. Inner join vs outer join: what's the difference?

An inner join returns only the rows from both tables that satisfy the specified join condition (can be joined by indicated field).
Rows that do not have matching values in the joined columns are excluded from the result set.

An outer join returns all the rows from one table and the matching rows from the other table, being connected by indicated field.
But if there is no match, the result will contain `NULL` values for columns from the table that does not have a matching row.

### 2. How SQL `GROUP BY` command works?

`GROUP BY` clause is used to group rows that have the same values in specified columns into summary rows, 
often for the purpose of applying aggregate functions to each group:

```sql
SELECT security_branch, COUNT(user_id) as user_count, MAX(last_login_datetime) as latest_login
FROM cybersecurity_users
GROUP BY security_branch;
```
with result:

```shell
+-------------------+------------+------------------------+
| security_branch   | user_count | latest_login  |
+-------------------+------------+------------------------+
| Threat Analysis   | 25         | 2023-11-15T08:30:00Z   |
| Incident Response | 18         | 2023-09-28T15:45:00Z   |
| Penetration Testing | 12       | 2023-07-05T12:10:00Z   |
| Security Operations | 30      | 2023-08-10T18:22:30Z   |
| Compliance        | 15         | 2023-09-02T09:55:45Z   |
+-------------------+------------+------------------------+
```

### 3. What is ORM?

It is a programming paradigm that allows you to interact with a relational database using an object-oriented programming.
Key features:
- [x] Mapping: ORM systems map database tables to classes, with each row in a table corresponds to an instance of a class, and each column corresponds to an attribute or property of that class.
- [x] Data abstraction: ORM abstracts away the details of database interactions, you deal with the objects / classes, not with the SQL queries.
- [x] CRUD: ORM systems provide methods and APIs for performing CRUD (Create, Read, Update, Delete) operations on database entities.
- [x] Relationships: ORM systems handle relationships between entities, such as one-to-one, one-to-many, and many-to-many relationships.
- [x] Portability: ORM systems often provide a level of database portability, allowing developers to switch between different database management systems (e.g., MySQL, PostgreSQL, Oracle) with minimal code changes. The ORM system abstracts the differences in SQL syntax and handles them internally.
- [x] Performance optimization: ORM systems may include features for optimizing database access, such as lazy loading (loading data on demand), caching, and query optimization.

### 4. What is stored procedure? What to use it for? 

Stored procedures are precompiled database scripts (group of statements) that can be executed from a database client, such as a Java application, using JDBC. 
Stored procedures offer better performance, reduced network traffic, and improved security. 
They can encapsulate complex SQL logic and business rules. 
JDBC provides the CallableStatement interface for executing stored procedures.
    
### 5. What is database view?

SQL view can be considered as a virtual table that consolidates data from one or more tables. 
Contrary to physical tables, view doesn’t store data itself and exists only logically in the database, where it is saved.
Unlike procedures, view doesn't have logic (it is only for presentation). No params, no storage and it's read-only.

### 6. What is coursor?

It allows you to retrieve and manipulate rows from a result set one at a time. Mainly used for iteration. Rows under cursor can be transformed (e.g. updated, deleted).
Other purposes: pagination, data validation.

### 7. What is the difference between function and stored procedure in SQL?

Function: 
- [x] database object, a set of SQL statements or expressions wrapped into a reusable and named single unit
- [x] returns value(s) (single value - scalar function, result set - table-valued functions).
- [x] they can be used as expressions in queries, such as SELECT, WHERE...
- [x] scalar functions can be used to modify data, but they are typically designed for computations and transformations
- [x] designed to be used as read-only, they cannot contain control statements like COMMIT or ROLLBACK

Stored procedures:
- [x] may or may not return values - it is optional
- [x] called explicitly using the EXECUTE or EXEC statement, cannot be used directly in a SELECT statement or a WHERE clause
- [x] can include data modification statements (INSERT, UPDATE, DELETE) and transaction control statements (COMMIT, ROLLBACK)
- [x] suitable for operations that involve both reading and writing data
- [x] can contain transaction control statements, allowing for explicit control over transactions

### 8. What are triggers in SQL database?

A trigger is a set of instructions or a program that is automatically executed ("triggered") in response to specific events on a particular table or view. 
These events can include data manipulation language (DML) events like INSERT, UPDATE, DELETE, or data definition language (DDL) events like CREATE, ALTER, or DROP. 
Triggers are often used to enforce business rules, maintain referential integrity, and automate certain tasks.

### 8a. What are DML Triggers?

These triggers respond to data manipulation language (DML) events, such as INSERT, UPDATE, and DELETE operations on a table.
Common use cases include enforcing data integrity rules, auditing changes, and automating specific actions based on data modifications.
Example of an AFTER INSERT trigger:

```sql
CREATE TRIGGER AfterInsertTrigger
AFTER INSERT
ON Employees
FOR EACH ROW
BEGIN
    -- Trigger logic, e.g., update a related table, log the change, etc.
END;
```
#### 8b. What are DDL Triggers:

These triggers respond to data definition language (DDL) events, such as CREATE, ALTER, and DROP operations on a database or table.
Common use cases include restricting certain schema modifications, logging schema changes, or implementing specific actions when database objects are altered.
Example of a BEFORE CREATE trigger:

```sql
CREATE TRIGGER BeforeCreateTrigger
BEFORE CREATE
ON DATABASE
BEGIN
    -- Trigger logic, e.g., check if the user has permission to create a table
END;
```

### 8c. What does the trigger consist of? 

Each trigger consist of three elements:

- [x] trigger event (when it should happen?) - specifies the event that causes the trigger to be executed (e.g., AFTER INSERT, BEFORE UPDATE)
- [x] trigger condition (why it should happen?) - **optionally** specifies a condition that must be true for the trigger to execute
- [x] trigger action (what should happen?) - contains SQL statements or procedures that are executed when the trigger is fired

### 8d. How triggers are used?

- [x] to enforce business rules, 
- [x] to enforce / maintain referential integrity rules
- [x] auditing & logging schema changes
- [x] automating data modifications
- [x] restricting certain schema modifications, logging schema changes
- [x] to database objects

### 8e. What is the risk of using triggers?

They introduce additional complexity and can impact performance!
Overuse of triggers can make database behavior less transparent and harder to manage. 
Therefore, triggers are often employed for tasks that are best handled within the database layer (meta-level, database management), such as enforcing integrity constraints or automating certain actions, rather than for general application logic.

### 9. What is SQL index?

A SQL index consists of a data structure that stores a sorted or hashed subset of the columns from a database table, 
along with pointers to the corresponding rows, to facilitate efficient and quick data retrieval operations.

### 9a. Advantages of using indexes

- [x] avoiding a full table scan (row by row), using trees and hashing in searching
- [x] index is a given field + pointer to the record, so it is fewer data than original record
- [x] faster with SELECT, WHERE, JOIN, and ORDER BY

### 9b. Drawbacks of using indexes

- [x] it takes additional storage space (it needs additional data structure that stores a sorted or hashed subset of the columns)
- [x] it can slightly slow down write operations (INSERT, UPDATE, DELETE) because the index must be updated when the data changes

### 9c. When to use indexes:
- [x] high-cardinality columns (uniqness of data in particular column)
- [x] frequent searches
- [x] large tables
- [x] JOIN, GROUP BY, ORDER
- [x] unique constraints (PRIMARY_KEY, UNIQUE)

### 9d. When not to use indexes:
- [x] small tables
- [x] sequential data, increasing or decreasing, like timestamps: the benefits of indexing might be limited, as new values are continuously added at one end of the index
- [x] frequent write operations
- [x] low-cardinality columns
- [x] temporary tables

### 10. Cardinality

Uniqueness of data values contained in a particular column. High-cardinality refers to columns with values that are very uncommon or unique - 
a good use case to apply indexes. E.g. user_id (which is unique), Normal-cardinality: address, name etc. Low-cardinality: booleans, flags, Y/N etc. -
do not use indexes such columns.

### 11. Transactions

In SQL, a transaction is a sequence of one or more SQL statements that are executed as a single, indivisible unit of work.
The properties of a transaction are often described by the ACID properties, which stand for Atomicity, Consistency, Isolation, and Durability.

Transaction phases are: acquiring lock, read, update, validation, commit, rollback.

When to use transactions?

- [x] to perform multiple database operations as a cohesive unit, such as updating multiple tables, inserting records, or deleting data
- [x] enforce data integrity and consistency
- [x] financial, banking, e-commerce
- [x] concurrency
- [x] where a large number of records need to be updated or processed
- [x] offline / online synchronization
- [x] when rollback is needed as an option
- [x] for isolation
- [x] complex operations (multiple steps)
- [x] ensuring durability

When not to use transactions?

- [x] simple read-only operations
- [x] where high concurrency is a top priority and conflicts are unlikely
- [x] individual, independent operations
- [x] performance-critical scenarios
- [x] where data is cached or denormalized
- [x] bulk data loading or large-scale batch processing
- [x] non-critical data, short-lived operations
- [x] logging & auditing

### 12. Deadlock

A deadlock in SQL occurs when two or more transactions are blocked, 
each waiting for the other to release a lock on a resource, resulting in a circular waiting condition.

### 13. Optimistic lock

Way of preventing lost update. Optimistic lock checks whether a value to be updated has not been changed since last read.
The optimistic locking approach allows multiple transactions to proceed with their operations without acquiring locks on the data. 
Instead, it relies on a mechanism to detect conflicts and resolve them at the time of committing the changes.

During read phase, the transaction records some form of a version identifier associated with the data (e.g., a timestamp, a version number, a hash value).

**It does not acquire any lock**. 

First, it reads data (1) and records some form of a version identifier associated with the data (e.g., a timestamp, a version number, a hash value).

Then starts the second phase: update (2).

During validation phase (3), it checks for any modifiactions done by another transaction in the meantime. 
This is typically done by comparing the recorded version identifier with the current version of the data.

Commit / rollback phase (4): if no changes -> commit. If changes -> rollback or conflict resolution.

### 13a. When optimistic locking is a good strategy?

**With high concurrency requirements**: in scenarios where high levels of concurrent access to the data are crucial, optimistic locking can be more suitable. It allows multiple transactions to read and modify data concurrently, reducing contention and increasing overall system performance.

**With low risk of conflicts:** when the likelihood of conflicts between transactions is low, optimistic locking is an efficient choice. If the data is not frequently updated by multiple transactions simultaneously, the overhead of acquiring and releasing locks may be unnecessary.

**For short transactions:** optimistic locking is well-suited for short-duration transactions where the time between reading and updating the data is minimal. Short transactions reduce the window during which conflicts might occur, making it less likely for two transactions to modify the same data concurrently.

**When optimizing read-heavy workloads:** in situations where the workload is predominantly read-heavy, and write operations are infrequent, optimistic locking can be effective. Readers are not impeded by locks, and conflicts during write operations are addressed when they occur.

**To reducing lock contention:** optimistic locking helps in reducing lock contention (**competition for acquiring locks**). By allowing multiple transactions to read data simultaneously and **only checking for conflicts at the time of update**, contention is minimized.

Optimistic locking is often **more compatible with distributed systems**. In scenarios where data is distributed across multiple nodes or databases, acquiring locks might be challenging or impractical. Optimistic locking allows for a more decentralized approach.

Optimistic locking is commonly used in scenarios where data may be edited **offline**, and changes need to be merged with the central database. Each offline editor can make changes independently, and conflicts are resolved when attempting to merge the changes.
It seems to be the way the deck synchronization in Anki works.

It is also more scalable solution for systems with a large number of transactions and a desire to reduce the load on the database caused by acquiring and releasing locks.

### 14. Pessimistic lock

Way of preventing lost update. Pessimistic lock explicitly forces other threads to wait until an update is done.

Lock acquisition (untill commit / rollback) - in many cases, it involves exclusive locks. Another type is shared lock, which might be escalated to exclusive lock.

Pessimistic locking may lead to deadlocks. Pessimistic locking is often associated with higher isolation levels, with more consistency and less concurrency.

### 14a. When pessimistic locking is a good strategy?

Where certain sections of code or database operations are **critical** and must be executed without interference from other transactions, pessimistic locking can be beneficial. This ensures that only one transaction at a time can access or modify the protected resource.

When maintaining **data integrity is a top priority**, pessimistic locking can be appropriate. For example, if an application enforces business rules that require consistency in data relationships, acquiring locks during transactions helps prevent concurrent modifications that could violate those rules.

In situations where transactions involve **resource-intensive operations or complex calculations**, pessimistic locking can be used to avoid conflicts and ensure that a transaction completes without interference from other transactions.
Also, when transactions involve **multiple steps or span different parts of the application, pessimistic locking can be used to ensure that the entire transaction is executed atomically without interference from other transactions.

Pessimistic locking is effective in **preventing race conditions**, where multiple transactions compete to read or modify the same data simultaneously. By acquiring locks, the system can control access and avoid conflicts.

In **batch processing scenarios where large volumes of data are processed**, pessimistic locking can help maintain order and prevent concurrent transactions from affecting each other. This is especially important when the order of processing is crucial.
Maintaining Consistency in Distributed Systems:

In **distributed systems with shared resources**, pessimistic locking can be used to ensure that only one node at a time makes modifications to a shared resource.

### 15. What is ACID?

Atomicity (A) ensures that a transaction is treated as a single, indivisible unit of work. Either all the changes made within the transaction are committed to the database, or none of them are. 
If any part of the transaction fails, the entire transaction is rolled back to its previous state.

Consistency (C) ensures that a transaction brings the database from one valid state to another. 
The database must satisfy certain integrity constraints before and after the transaction. 
If a transaction violates any integrity constraints, the database is left unchanged.

Isolation (I) ensures that the execution of one transaction is isolated from the execution of other transactions,  even if they are executed concurrently.

Durability (D) guarantees that once a transaction is committed, its effects are permanent and survive subsequent system failures. 
The changes made by the transaction are stored in non-volatile storage (such as disk) and can be recovered even if the system crashes or restarts.
