---
layout: single
title: "SQL cheatsheet: part 6"
date:   2023-07-20 04:23
description: "Advanced SQL for Java developers: procedure, view."

categories:
- SQL, database, data

tags:
- SQL, database, data, persistency

---

Previously on SQL: [Medium SQL for Java developers: recapitulation](/sql-cheatsheet-5)

### Procedure

SQL procedure is a kind of SQL query embedded in a SQL script. In other words, it is SQL function that executes pre-programmed query.
A procedure can accept arguments. Here is an example of a stored procedure that selects all columns from the table ``company`` according
to the country code argument passed to this procedure.

```sql
-- STORED PROCEDURE - SQL query saved directly in db as a function
-- such function accepts argument being used to execute query within stored procedure
-- procedure can be then called multiple times with different args

-- HOW TO CREATE
DELIMITER
$$
CREATE PROCEDURE get_company_by_country_code(
    country_code VARCHAR (4)
)
BEGIN
    IF
country_code IS NULL
THEN
SELECT 'Function argument is null';
    ELSE
SELECT *
FROM company
WHERE company.hq_country = country_code;
END IF;
END $$

-- HOW TO CALL
CALL get_company_by_country_code('JPN') -- returns result
$$
CALL get_company_by_country_code(null) -- returns hard-coded answer
$$
CALL get_company_by_country_code() -- returns error as arg is expected
$$
```

I asked ChatGPT to explain the procedure. Here is detailed explanation:

> The code sets the delimiter to $$. This is used to specify the end of the stored procedure definition since it contains semicolons (;) within its body.

> The CREATE PROCEDURE statement is used to define the stored procedure get_company_by_country_code with the country_code parameter.

> The BEGIN keyword indicates the start of the stored procedure's body.

> The code checks if the country_code parameter is NULL using the IF statement.

> If the country_code is NULL, the code executes the SELECT 'Function argument is null'; statement. This statement will return a single row with the string value 'Function argument is null'.

> If the country_code is not NULL, the code executes the SELECT * FROM company WHERE company.hq_country = country_code; statement. This statement selects all columns (*) from the company table where the hq_country column matches the provided country_code parameter.

> The END IF; statement denotes the end of the IF block.

> The END $$ statement denotes the end of the stored procedure definition, using the previously set delimiter.

> Overall, this stored procedure retrieves company data based on the provided country_code. If the country_code parameter is NULL, it returns the string 'Function argument is null'. Otherwise, it selects all columns from the company table where the hq_country column matches the provided country_code.


The explanation created by ChatGPT contains rather obvious, self-explanatory statements, but it can be helpful, nevertheless.

### Why we should use stored procedures?

[The Oracle JDBC tutorial explains this issue.](https://docs.oracle.com/javase/tutorial/jdbc/basics/storedprocedures.html)

> JDBC means Java Database Connectivity.
> 
> It's Java API (a.k.a. "abstraction layer") designed to standarize and simplify the process of connecting Java software to relational database management systems (RDBMS)

In short:

Stored procedures are precompiled database scripts (group of statements) that can be executed from a database client, such as a Java application, using JDBC.
Stored procedures offer better performance, reduced network traffic, and improved security. They can encapsulate complex SQL logic and business rules.
JDBC provides the **CallableStatement** interface for executing stored procedures.

> The CallableStatement interface allows the use of SQL statements to call stored procedures.

You can prepare and execute stored procedures using **CallableStatement**. 
You can use `execute()`, `executeQuery()`, and `executeUpdate()` methods to invoke stored procedures.
They can have input and output parameters, or parameters that are both input and output
Error handling for stored procedures is also explained, including handling exceptions using `SQLException`.

What about advantages of stored procedures in Java, including security context?

#### Easier maintenance


**1. Encapsulation of logic and operations**

Stored procedures allow you to encapsulate business logic and database operations into a single unit, looking like a simple script. 
This helps enforce **data integrity rules and security constraints.** 
By centralizing the implementation of data operations within the stored procedure, 
**you can ensure that security checks, access controls, and validation rules are consistently applied** across different parts of the application.


**2. Reusability of code**

Stored procedures can be called and executed from various parts of an application or by multiple users. 
This promotes code reuse and reduces redundancy, as the same logic can be executed without rewriting it.
Multiple Java applications or components can invoke the same stored procedure, 
reducing duplication of code and promoting consistency across different parts of your application.


**3. Transaction management**

Stored procedures can be used to define complex transactions that involve multiple SQL statements.
This allows for consistent and reliable data modifications, with the ability to roll back changes if necessary.
Storing SQL logic in stored procedures allows for centralized management and versioning of database operations.
Modifications to the SQL code can be made in the stored procedures without requiring changes to the Java codebase.

This separation of concerns makes it easier to track and manage changes, **ensuring that security updates and fixes can be applied more efficiently**.


**4. Database decoupling**

A stored procedure is a helpful tool when thinking about vendor-specific database independence (like, for example, future data migration).
Using stored procedures can help abstract the underlying database implementation from your Java code.
By relying on stored procedures, you can write database-agnostic Java code that can work with different database systems without major modifications.
This can simplify database migrations or switching to a different database platform in the future.

##### Better performance

Stored procedures can enhance performance by reducing network traffic.
They are typically compiled and optimized by the database server during creation or the first execution.
This can lead to faster execution times compared to dynamically generating SQL statements in Java code. 
By offloading data processing to the database server, you can reduce network latency and utilize the database's query optimization capabilities.
Instead of sending multiple SQL statements over the network, 
a single call to the stored procedure is made, reducing the overhead of multiple round trips.

**It could make a difference if someone tried to DDOS your application.**

##### Increased security

**1. Security enhanced by limited access**

Stored procedures allow you to grant permissions to execute the procedure without granting direct access to the underlying database tables. 
This means that users or applications can **interact with the database only through the stored procedure**, 
and they don't have direct control over the underlying data. It provides **a layer of abstraction and restricts unauthorized access to sensitive data**.

**2. Safe parametrized queries**

Stored procedures typically use parameterized queries, 
where user input is passed as parameters rather than directly concatenating them into SQL statements. 
This helps **prevent SQL injection attacks**, a common security vulnerability where malicious input is injected into SQL statements. 
By using parameterized queries, stored procedures can ensure that user input is properly sanitized and reduce the risk of SQL injection attacks.

**3. Auditing and logging for security control**

Stored procedures provide a natural point for implementing auditing and logging mechanisms. 
You can log the execution of stored procedures, capturing details such as **who executed the procedure, 
when it was executed, and what parameters were used**. This can help with compliance requirements, troubleshooting, 
and identifying potential security breaches or suspicious activities.

### View

SQL view can be considered as a virtual table that consolidates data from one or more tables. 
Unlike physical tables, view doesn't store data itself and exists only logically in the database, where it is saved.

Each view in a database must have a unique name, just like a regular SQL table. 
It is defined by a predefined set of SQL queries that retrieve data from the underlying database tables. 
View can incorporate tables from a single database or multiple databases.

Example of SQL view:

```sql
-- VIEW acts like reusable saved SELECT
-- is stored directly in db

CREATE VIEW company_left_join_customer AS
SELECT company.*,
       c.customer_id AS customer_number, -- table name even in different tables must be unique
       c.first_name,
       c.last_name,
       c.registration_date
FROM
    company
        LEFT JOIN customer c ON company.customer_id = c.customer_id;

-- example of use
SELECT * FROM company_left_join_customer; -- view alias

-- customer, current branch, current turnover
CREATE VIEW customer_branch_turnover_current AS
SELECT
    customer.customer_id,
    customer.first_name,
    customer.last_name,
    customer.registration_date,
    b.branch_name,
    bc.from_date AS branch_since,
    t.turnover,
    t.from_date AS monthly_turnover_since
FROM
    customer
        INNER JOIN
    branch_customers bc
    ON customer.customer_id = bc.customer_id
        INNER JOIN
    branch b
    ON bc.branch_id = b.branch_id
        INNER JOIN
    turnover t
    ON customer.customer_id = t.customer_id
WHERE
        bc.to_date = '9999-01-01'
  AND
        t.to_date = '9999-01-01'
```

### View versus procedure

What's the difference between procedure and view?

SQL view is a virtual table or tables, similar to a product of `SELECT` query, optionally with `JOIN`. 

**No logic**: There is no procedural logic in that, no conditional statements nor loops.

**No parameters**: Views don't accept parameters.

**No storage**: A view doesn't store data itself but provides a way to present data from one or more underlying tables or other views.

Views are primarily used for **data retrieval and presentation**.

**Read-only:** Views are **read-only** and can be used to simplify complex queries, filter data, 
and provide a consistent interface for users or applications.

In terms of security, **views can be used to restrict access** to specific columns or rows in a table, 
but they don't provide the same level of security and control as stored procedures.

In terms of performance, views **may improve query performance** by providing a pre-defined and optimized representation of data. 

However, **complex views may introduce performance issues**.

On the other hand, SQL procedure is like a pre-programmed query, often with custom parameters. 
Stored procedures support conditional logic, error handling, and the ability to return multiple result sets.
Stored procedures can be used for **data manipulation**, such as CRUD operations.
They can significantly improve security and performance.

