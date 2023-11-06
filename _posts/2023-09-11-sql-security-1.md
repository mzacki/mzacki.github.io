---
layout: single
title: "SQL security 1"
date:   2023-09-11 20:23
description: "Advanced SQL for Java developers: intro to security."

categories:
- SQL, database, data, security

tags:
- SQL, database, data, persistency, security

---

Previously on SQL: [Advanced SQL for Java developers: coursor, function, index.](/sql-cheatsheet-7)

### Normalized database vs denormalized database

Normalized database is optimized for minimizing redundancy, not for lowest possible read time.
Such database contains many tables, uses joins and rather complex queries etc.

In a normalized database, data are organized into multiple related tables. 
Each table is designed to store a specific type of data, and relationships between tables are established through foreign keys.

Storage efficiency is better as data are stored in most space-efficient manner.
Read efficiency is harder to achieve, when it retrieves data from multiple related tables. 
While this can be computationally expensive, it allows for flexibility in querying the data.

With proper normalization, data consistency is usually easier to maintain, as changes to data only need to be made in one place (the corresponding table).
Normalized databases are typically favored for systems where data integrity and consistency are critical, such as financial and transactional systems.

Denormalized databases is optimized for read time, not for minimizing redundancy.
Such database contains as many columns in one table as possible. Here, there is no need to create more tables, but smaller ones.
It does not look like a clear solution, but it is faster. Data are stored in fewer tables, 
and there may be duplication of data across tables. This is done to reduce the need for JOIN operations.
However, denormalization can be a valid design choice if it serves specific performance needs.
Denormalization can be more storage-intensive because it may involve redundancy.

### Security in normalized and denormalized database

Normalization aims to minimize data redundancy and ensure data integrity.
By breaking data into smaller, related tables, it reduces the risk of data anomalies, such as insertion, update, and deletion anomalies.
Normalized databases are typically favored for systems where data integrity and consistency are critical, such as financial and transactional systems.

In normalized databases, the primary concern is managing data relationships, and security measures should focus on access controls, 
auditing, and preventing unauthorized changes to the database schema, as the structure is more complex.

Denormalization can lead to some loss of data integrity, as redundancy increases the risk of anomalies. 
Here, data duplication can be a problem.

In denormalized databases, security measures need to consider data duplication, as the same data might exist in multiple places. 
Special attention must be paid to keeping all copies of the data secure and ensuring consistency in access controls.

### Integrity

In SQL databases, data integrity refers to the accuracy and consistency of data stored in the database. 
There are several types of data integrity in SQL databases, each serving a specific purpose. 
These integrity constraints help ensure that data remains reliable and valid.

- [x] Entity integrity: No duplicate rows exist in a table. 

Entity integrity ensures that each row (record) in a table is uniquely identifiable, typically through a primary key. 
This means that each row must have a unique value in its primary key column, preventing duplicate records in the table.

- [x] Domain integrity: Restricting the type of values that one can insert in order to enforce correct values (in Java, for example, using enums may be helpful).

Domain integrity enforces that data values in a column meet specific criteria, such as data type, format, and allowable values. 
Common examples include ensuring that a date column contains valid dates or that an integer column contains only whole numbers.

- [x] Referential integrity: Records that are used by other records cannot be deleted (using constraints).

Referential integrity establishes and enforces relationships between tables through foreign keys. 
It ensures that data in a foreign key column in one table corresponds to data in the primary key column of another table. 
This constraint prevents orphaned records and maintains the consistency of relationships.
Here, for example, you won't be able to delete a record from one table which is related to another table via constraint.
Either you delete both of them, or none.

Cascading actions, such as CASCADE DELETE and CASCADE UPDATE, are often associated with referential integrity. 
They define what should happen to related records when a referenced record is deleted or updated. Cascading actions can help maintain data consistency.

- [x] Custom integrity / custom constraints

User-defined integrity allows users to define custom constraints or business rules to maintain data integrity. 
This can include rules specific to a particular application or domain, ensuring that data adheres to business logic:
validation rules, data calculations, and workflow-related checks.

Triggers are event-driven actions that can be executed automatically in response to changes in the database. 
Triggers can enforce custom data integrity rules and actions.

Combination of integrity requirements may happen, 
\like domain key integrity that combines elements of both domain and entity integrity by ensuring that the primary key values in a table are unique and also meet domain constraints, 
such as data type and format requirements.

### Integrity and security

Database integrity, in the context of security, refers to the fundamental aspect of ensuring the accuracy, consistency, and reliability of the data stored in a database as a means to enhance data security. 

Ensuring data integrity means that data stored in the database is accurate and free from errors. Accuracy is crucial for making informed decisions and avoiding security incidents that could arise from erroneous data.

Consistency prevents data anomalies that might be exploited for security breaches.

Part of data integrity involves validating and sanitizing data input to the database. 
This practice minimizes the risk of SQL injection and other security vulnerabilities that could compromise the integrity and security of the database.

Data integrity helps prevent unauthorized changes or tampering with the data. This safeguards against both accidental and malicious alterations to the data.

Data corruption can lead to security risks, as corrupted data might have unpredictable consequences on the application.
Data integrity measures help minimize the risk of data corruption by ensuring that data is stored consistently and accurately.

Even within an organization, there is a risk of insider threats. Data integrity measures, such as access controls and audit trails, 
help detect and prevent unauthorized access, alterations, or exfiltration of data by employees or insiders.

Last but not least, maintaining data quality, ensuring data recovery and continuity are also important feature of integrity that serves the purpose of security.
n the event of a security incident or data breach, maintaining data integrity ensures that backup and recovery processes can restore a reliable and consistent database state. 
Data integrity is essential for business continuity and disaster recovery planning.

In summary, database integrity plays a crucial role in data security by ensuring the accuracy, consistency, and reliability of the data stored in the database. When data is trustworthy, it reduces the likelihood of security incidents, minimizes vulnerabilities, and supports the overall security of the database and the applications that rely on it. 
Data integrity and security measures work hand in hand to protect sensitive information and maintain the integrity of the database.

### Idempotent vs deterministic function

Although sometimes both terms are mistaken or not clear enough, these are two different concepts.

An idempotent function is a fuction that has the same effect when applied multiple times. 
No matter if executed once or more, the result is the same. It is used to ensure that a specific operation is performed only once, even if it is requested multiple times.
An example of an idempotent function is SQL `DELETE`. Deleting a resource one time or multiple times has the same result: the resource has been deleted.

In mathematics: `f(f(x)) = f(x)`

On the other hand, a deterministic function is a function in which the output is completely determined by the input. 
In other words, given the same input, a deterministic function will always produce the same output, making it predictable and consistent.

For each input x, there is only one corresponding output y, so it's simply `f(x)`.

Deterministic functions are commonly used in various fields, including computer science, cryptography, and databases. 
They are valuable for ensuring data consistency and predictability, as they guarantee that the same input will always result in the same output.

A common use case of deterministic functions is **unit testing**: for the same input data, their result of unit tests is always the same. There should not be any other factors impacting the result.
If such test starts to fail, it means the code has been broken.

In SQL, examples of deterministic functions are mathematical and date and time functions: `SELECT ABS(-9)`, `SELECT 2 - 2`, `SELECT DATEADD(day, 5, '2023-01-01')`.

Non-deterministic SQL functions are getting random number, generating UUID, selecting current user etc.

### Idempotent and deterministic SQL functions - implications for security

Idempotent functions can help prevent data corruption and security incidents. 
For example, idempotent SQL DELETE or UPDATE operations ensure that critical data is not accidentally or maliciously deleted or modified multiple times.
Idempotent functions are often used within transactions to ensure that a sequence of operations can be safely retried without introducing data inconsistencies
(atomic operations).

One of their features is preventing unwanted side effects. Idempotent functions follows **Security Through Predictable Behavior** principle. 
The predictability of idempotent functions can help prevent unwanted side effects or actions that could lead to security incidents. 
When a function's behavior is consistent, it is easier to anticipate and control its impact on the database.
In disaster recovery and backup scenarios, idempotent operations can be valuable for restoring the database to a known state without introducing additional inconsistencies.

Deterministic functions are critical for data integrity and help in auditing and compliance efforts. Deterministic functions are commonly used in cryptographic operations.
They ensure consistent encryption and decryption, which is essential for data security.