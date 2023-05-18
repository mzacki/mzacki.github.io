---
layout: single
title: "SQL cheatsheet: part 4"
date:   2023-05-18 05:23
description: "SQL for Java developers: CRUD operations, n+1 problem, Flyway & Liquibase migrations."

categories:
- SQL, database, data

tags:
- SQL, database, data, persistency

---

Previously on SQL: [union vs join, left join, right join, inner vs outter join](/sql-cheatsheet-3)

#### CRUD

**CRUD** is an acronym that stands for Create, Read, Update, and Delete. 
It is a set of four basic operations that are commonly used in the context of database management systems and web development to manage data.

Here's a breakdown of each operation:

> Create (C): This operation involves creating new records or entities in a database. It typically involves inserting data into a database table or creating a new object in an object-oriented programming context.

> Read (R): This operation involves retrieving or reading existing data from a database. It allows you to query and fetch specific records or information from a database. Reading data could involve retrieving a single record, a subset of records, or all the records in a table.

> Update (U): This operation involves modifying or updating existing data in a database. It allows you to make changes to specific records or fields within a record. Updating data could involve modifying values, adding new information, or altering existing data.

> Delete (D): This operation involves removing or deleting existing data from a database. It allows you to delete specific records or objects from a database. Deleting data could involve removing a single record, a subset of records, or all the records in a table.

These four operations form the fundamental building blocks for performing data manipulation within a database or application. They provide the basic functionality to create, retrieve, update, and delete data, enabling developers to perform various operations on data stored in a system.

CRUD applications are often (mistakenly) perceived by programmers as super-simple, even trivial, and certainly not impressing at all. 
In fact, many corporate-grade Java applications are CRUD-type or, at least, they have a part of code that is executing CRUD requests.
This, of course, must be designed as business-oriented software that plays an important role for the client. Otherwise, nobody would pay for a simple CRUD app.

More experienced engineers will tell you that even CRUD operations are not so simple to proper execution and require some
thinking to be correctly planned. CRUD requests are commonly performed on complex,
internally related entities, and they involve a lot of records. Then performance and cost-efficiency comes into play.
Hasty and supperficial use of simple SQL queries, very often offered out of the box by ORM frameworks (like Hibernate / JPA),
may lead to peformance issues, like "n+1" problem.

#### n+1 problem

The "n+1 problem" is a term commonly used in the context of database querying, particularly in Object-Relational Mapping (ORM) frameworks. It refers to an issue that arises when retrieving data from a database with relationships between entities.

In an n+1 problem scenario, let's say you have two entities with a one-to-many relationship, such as "Blog" and "Comment," where each blog can have multiple comments. When you want to fetch a list of blogs and their associated comments, the n+1 problem occurs if the ORM framework generates n+1 queries to the database.

Here's how it typically unfolds:

The initial query retrieves a list of blogs from the database.
For each blog in the result set, the ORM framework executes an additional query to fetch the associated comments for that specific blog.
This leads to n+1 queries, where n represents the number of blogs fetched in the initial query.
The problem with the n+1 approach is that it incurs additional overhead and can result in significant performance issues, especially when dealing with large datasets. Each additional query introduces network latency and database overhead, causing the overall retrieval process to be slower and less efficient.

To mitigate the n+1 problem, ORM frameworks often provide ways to eager load or prefetch related data, allowing you to fetch the necessary information in a single query or a reduced number of queries. By doing so, you can avoid the performance pitfalls associated with the n+1 problem.

But beware: even such solution may not fully resolve the issues. Sometimes, **plain SQL** is a better (but harder) way to deal with data.

> Use ORM frameworks with moderation.

It is very important to know SQL basics and be aware of more complex topics, to be able to predict possible traps. 

#### A little more on CREATE

As part of this course, let's make a short recapitulation of fondamental `SELECT` queries, basic SQL syntax and operators.

In first lesson, we created database tables. Obviously, `CREATE` operations correspond to **Create** part of **CRUD** acronym. 
At first glance, database and table creation seemed to be complex and difficult task, but in reality, it is easier than other queries.

1. Creation is usually made step-by-step, meaning: one table after another, during time, accordingly to when it is needed. See **Flyway** or **Liquibase** **migrations**. No need to write whole script at once.
2. Creation is one-time act. An application may create or recreate database structure during deployment, but usually it is not repeated during application working time nor on-demand (e.g. via REST endpoints).
3. If creation fails, no problem, nothing is lost. The creation / migration script will be fixed.

#### Flyway and Liquibase: what are database migrations

Database migration refers to the process of modifying the structure or schema of a database in a controlled and organized manner. 
It involves making changes to the database schema, such as adding or modifying tables, columns, constraints, or indexes, while ensuring that existing data is properly migrated or transformed to accommodate the new structure.

Database migrations are typically performed to introduce changes in an application's data model, accommodate new features, fix issues, or improve performance. 
The process is crucial when working with evolving software systems that require continuous updates to the database schema.

Flyway and Liquibase are both popular database migration tools that help developers manage and version control database schema changes. 
They provide a systematic approach to perform and track database migrations, ensuring smooth and controlled updates to the database structure.

> Flyway is an open-source database migration tool. It allows developers to define database changes using SQL scripts or Java-based migrations and tracks the execution of these scripts. Flyway maintains a metadata table in the database to keep track of which migrations have been applied. When running an application, Flyway automatically checks the metadata table and applies any pending migrations, keeping the database schema up to date. 
> Flyway supports a wide range of databases and integrates well with various build tools and frameworks.

> Liquibase is another popular open-source database migration tool. It follows a similar approach as Flyway but offers additional features and flexibility. Liquibase allows developers to define database changes using XML, YAML, JSON, or SQL formats. It tracks migrations using a changelog file that specifies the sequence of changes to be applied. Liquibase supports various databases and provides features like rollback support, preconditions, and more advanced change types. 
> It also offers integration with different build tools and frameworks.

Flyway has a simpler and more lightweight design, focusing on simplicity and ease of use. 
It encourages convention over configuration and follows a strictly ordered migration approach.

Liquibase provides more flexibility and customization options. It supports a wider range of change types, offers advanced features like rollbacks, and allows more fine-grained control over migrations.
Flyway uses SQL-based migrations by default, whereas Liquibase supports multiple file formats for defining changes (XML, YAML, JSON, or SQL).
Both tools provide integrations with various build tools, frameworks, and Continuous Integration/Continuous Deployment (CI/CD) pipelines.

#### More on SELECT

`SELECT` operators are doing the **Read** part of CRUD, so they are only relatively safe to execute - data won't be modified - but there might be pitfalls.

Enough theory. Let's recall some practical skills:

```sql
-- select all columns matching both (AND) given conditions (note how operators were used for text and date values):
SELECT * FROM company WHERE hq_country='JPN' AND `established_date` < '1987-06-26';
```
```sql
-- select given columns matching at least one (OR) of two conditions
SELECT name, country FROM company WHERE hq_country='JPN' OR hq_country='KOR';
```
```sql
-- more elastic way of searching, limit the results
SELECT * FROM company WHERE name LIKE 'S%' LIMIT 2;
```

`GROUP BY` and `COUNT` are commonly used for getting some numerical values:

```sql
-- group by (counts rows grouped by country)
-- name may be replaced by any column
SELECT COUNT(name), hq_country FROM company GROUP BY hq_country;
```

Sort (order) the results. Ascending is default ordering strategy, **so `ASC` keyword is redundant here**:

```sql
-- order result
SELECT birth_date, first_name, last_name FROM customer WHERE first_name LIKE 'Fran%' ORDER BY first_name, last_name ASC; -- ASC is redundant
SELECT birth_date, first_name, last_name FROM customer WHERE first_name LIKE 'Fran%' ORDER BY `birth_date` DESC;
```

But descending is not default strategy, so do not forget `DESC` keyword.

We said that reading data is only **relatively** safe operation, because data are not modified. But the other side of the coin is that selecting data is not for free - sometimes it heavily impacts the database,
that is doing all the hard work for us. Especially when we made a complex, incorrect query that should have been optimized.

> Generally, SQL and databases are projected and optimized for data handling, even when dealing with large amount of data.
> Example: it might not be the best idea to map 100K records to ORM entities, then to Data Transfer Objects or other Java objects, in order to make
> some operations on them through Java streams, like sort or filter.
> 
> On the other hand, database might not be necessarily optimized for given use case. Not to mention, that sometimes is cheaper to fetch a bigger chunk of data
> in one query, and then to process it programatically, just to avoid n+1 problem.
> 
> Quid pro quo.

#### Update

Once database has been created and data inserted, it can be therefore updated (this is the update part of CRUD).
Modifying data is doubly burdensome. First, the data to be updated should be selected beforehand, accordingly to some cirteria.
Here, as we said before, there might be some performance issues, no matter if we want to make a single update (one time, "by hand"),
or regularly, as part of normal flow of the application.

Secondly, we are changing the data. We can lose some information or break the data integrity.

> SELECT some data first. If SELECT works correctly, then you can think of an UPDATE.

```sql
-- update record
UPDATE company SET name = 'Seoul 88' WHERE name = 'SEOUL_88';
```

``UPDATE`` with ``JOIN``:

```sql
-- update record copying column from joined table
UPDATE company
    INNER JOIN
    customer
ON company.customer_id = customer.customer_id
    SET company.name = CONCAT(
        company.name,
        '_',
        customer.first_name,
        '_',
        customer.last_name);
```

``REVERSE`` operator:

```sql
-- REVERSE name
UPDATE company
    INNER JOIN
    customer
ON company.customer_id = customer.customer_id
SET company.name = REVERSE(company.name);
```

Substract or add to date:

```sql
-- SUB / ADD DATE
UPDATE company
SET established_date = DATE_SUB(established_date, INTERVAL 1 YEAR)
WHERE
        established_date > '2020-01-01'
```

More painstaking tricks:

```sql
-- insert space before last three chars:
-- (e.g. Entity Ltd instead of EntityLtd)
-- remove last tree chars
-- concat string, space and last three chars
UPDATE company
SET name =CONCAT(LEFT(name, LENGTH(name) - 3), ' Ltd')
WHERE
        established_date > '2020-01-01'
```

```sql
-- substract 2 years from date in case of even year, odd id and given country
-- substract 1 year in case of even year, even odd and given country
UPDATE company
SET established_date = (
        CASE
            WHEN
                EXTRACT(YEAR from established_date) % 2 = 0
        AND
        company_id % 2 != 0
                AND
                        hq_country = 'USA'
            THEN DATE_SUB(established_date, INTERVAL 2 YEAR)
        WHEN
                        EXTRACT(YEAR from established_date) % 2 = 0
                AND
                        company_id % 2 = 0
                AND
                        hq_country = 'USA'
            THEN DATE_SUB(established_date, INTERVAL 1 YEAR)
        ELSE established_date
        END
    );
```
```sql
-- funny thing, UPDATE date (if null, use current) by reversing year
UPDATE company
SET established_date = CONCAT(
    -- CASE should be add for null check...
    REVERSE(EXTRACT(YEAR from CURDATE())),
                              '-',
                              EXTRACT(MONTH from CURDATE()),
                              '-',
                              EXTRACT(DAY from CURDATE()))
WHERE
        name = 'Ale Lipa';
```
#### Delete

Finally, last item of **CRUD**: data deletion. It is risky because of potential unwanted data loss.
``DELETE`` is rather not executed frequently "in real application life". 
Also, an external customer or user of a corporate-grade software hardly ever has an easy, overt possibility to trigger direct data deletion process.
More often, it is a multistep process due to security reasons. And there should be backups... but as all security experts know, sometimes there are no backups.

Here, we can use the same trick as with the update. The query should have `SELECT` instead of `DELETE`. If we selected exactly what we wanted,
we can replace the keywords (`DELETE` instead of `SELECT`).

Simple SQL syntax for delete looks like:

```sql
-- DELETE row duplicates (copies)
DELETE FROM
    company
WHERE
    company.name LIKE '%_COPY'
```

``DELETE`` with ``JOIN``:

```sql
-- JOIN and DELETE
-- joining three tables, delete records from two (branch remains intact)
DELETE customer, bc FROM
customer
INNER JOIN
    branch_customers bc
ON
    customer.customer_id = bc.customer_id
INNER JOIN
    branch b
ON
    bc.branch_id = b.branch_id
WHERE
    customer.last_name LIKE '%smith%'
AND
    bc.to_date = '9999-01-01'
AND
    customer.gender = 'M'
```

Table removal:

```sql
-- remove table
DROP TABLE company;
```

But do not do this in production (or any other important environment) (unless you are told to do so, but even then, double check it with someone).

TBC