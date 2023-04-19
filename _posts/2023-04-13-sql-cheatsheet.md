---
layout: single
title: "SQL cheatsheet"
date:   2023-04-13 00:23
description: "SQL cheatsheet for Java developers: basic SELECT, Boolean operators, mathematics and other tricks."

categories:

- SQL

tags:

- SQL, database, data, persistency

---

#### Purpose of this article

I met a few skilled, very experienced developers, both backend (Java) and frontend (JavaScript, TypeScript, Angular, React), who had one weakness.
They were avoiding to look into the database. There were many various reasons. One of my colleagues lacked knowledge of SQL and basics on how to connect to a relational database.
But he was good at Hibernate, he was able to create Hibernate entities, repositories and HQL queries easily, understanding even harder relational problems. In his previous project, all database infrastructure and content management had been handled by some framework, so he was responsible only for connecting to this framework through Hibernate.
No opportunity to learn SQL, no need to get out of his comfort zone. 

Another colleague was not keen on looking into our database, treating is as "a backend stuff". Being a frontend developer with 13+ years of experience,
he was not able to check if his REST POSTs and GETs requests are working properly, saving or returning some sample data from the storage. He had to rely on our help, even after we had created a special - but still simple - queries and shared it with him. That was, on the other hand, pretty much annoying.

I think that every Java developer should have at least basic knowledge of SQL and related areas. Then, life of a developer (and life of his co-workers) is easier. Not only it helps in everyday work,
but also it gives another technical skill, making a programmer better prepared for an interview. It does not take too much: the SQL entry treshold is very low.
And even basic commands are useful. More difficult areas are often handled by database specialist (lucky if you have one in your team).
If not, having SQL fondamentals in your tech stack is a good starting point to resolve more complex problems - and often it saves you and your team.

This article is for Java developers (but not only Java) who want to quickly start with simple SQL commands and queries, as well as for SQL false beginners - in order to make
a quick repetition of shamefully forgotten items.

I will start with SQL fundamentals, then I'll explain less known (but still simple) topics. And finally, we will make more theoretical summary of terms and questions 
that may appear during development or at technical interview.

#### Sample relational database

For database connection and management, you can use tools like [Workbench](https://www.mysql.com/products/workbench/) for MySQL or [pgAdmin](https://www.pgadmin.org/) for Postgres.
[DBeaver](https://dbeaver.io/) is also nice for various flavours. But why shall we use third-party software when IntelliJ IDEA offers its great, in-built feature?
All in all, we are paying for it (or a company that hires you is paying for the licence). So let's take advantage of that and have all in one place!
All the more so, because there exists [JetBrains documentation for Database](https://www.jetbrains.com/help/idea/relational-databases.html).

> Pluralitas non est ponenda sine neccesitate. (William Ockham - Ockham's Razor)

Sample database for training purpose can be found in the internet. [Microsoft](https://learn.microsoft.com/en-us/sql/samples/sql-samples-where-are?view=sql-server-ver16)
offers some SQL samples for its SQL flavour. The same is true for [MySQL](https://dev.mysql.com/doc/employee/en/). I think Postgres and MariaDB samples can be found 
at GitHub. Cloud database storage and connection may be limited, especially for Microsoft and Azure. If someone asks: the cloud costs money - although sometimes you can get something for free - 
see [No Free Lunch theorem](https://en.wikipedia.org/wiki/There_ain%27t_no_such_thing_as_a_free_lunch)

Local database can be created by installing e.g. MySQL Server on the hard drive and then filling it with sample data. But I would go or a database as [Docker image](https://hub.docker.com/search?q=MySQL&type=image).

```shell
~/IdeaProjects/blog$ docker ps
CONTAINER ID   IMAGE       COMMAND                  CREATED       STATUS       PORTS                                                  NAMES
c81d61fa3746   mysql:5.7   "docker-entrypoint.s…"   2 years ago   Up 2 hours   33060/tcp, 0.0.0.0:3307->3306/tcp, :::3307->3306/tcp   persistency_db_1
29baec3383ba   mariadb     "docker-entrypoint.s…"   2 years ago   Up 2 hours   0.0.0.0:3308->3306/tcp, :::3308->3306/tcp              persistency_mariadb_1
```

Playing with Docker is quite different field of interest, so I will skip the details. There is a lot of on-line manuals. My private rule-of-thumb is **Docker plus IntelliJ database tool**: it saves time and space.

#### Create database and tables

Suppose we do not have a database, or we want to have a brand new, with empty tables. Initial script could look like this:

```sql
DROP DATABASE IF EXISTS customers;
CREATE DATABASE IF NOT EXISTS customers;
USE customers;

DROP TABLE IF EXISTS branch_customers,
    branch_top_customer,
    field,
    turnover,
    customer,
    branch;

CREATE TABLE customer (
                           customer_id      INT             NOT NULL,
                           birth_date  DATE            NOT NULL,
                           first_name  VARCHAR(14)     NOT NULL,
                           last_name   VARCHAR(16)     NOT NULL,
                           gender      ENUM ('M','F')  NOT NULL,
                           registration_date   DATE            NOT NULL,
                           PRIMARY KEY (customer_id)
);

CREATE TABLE branch (
                             branch_id     CHAR(4)         NOT NULL,
                             branch_name   VARCHAR(40)     NOT NULL,
                             PRIMARY KEY (branch_id),
                             UNIQUE  KEY (branch_name)
);

CREATE TABLE branch_top_customer (
                              customer_id       INT             NOT NULL,
                              branch_id      CHAR(4)         NOT NULL,
                              from_date    DATE            NOT NULL,
                              to_date      DATE            NOT NULL,
                              FOREIGN KEY (customer_id)  REFERENCES customer (customer_id)    ON DELETE CASCADE,
                              FOREIGN KEY (branch_id) REFERENCES branch (branch_id) ON DELETE CASCADE,
                              PRIMARY KEY (customer_id,branch_id)
);

CREATE TABLE branch_customers (
                          customer_id      INT             NOT NULL,
                          branch_id     CHAR(4)         NOT NULL,
                          from_date   DATE            NOT NULL,
                          to_date     DATE            NOT NULL,
                          FOREIGN KEY (customer_id)  REFERENCES customer   (customer_id)  ON DELETE CASCADE,
                          FOREIGN KEY (branch_id) REFERENCES branch (branch_id) ON DELETE CASCADE,
                          PRIMARY KEY (customer_id,branch_id)
);

CREATE TABLE field (
                        customer_id      INT             NOT NULL,
                        field_name       VARCHAR(50)     NOT NULL,
                        from_date   DATE            NOT NULL,
                        to_date     DATE,
                        FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE,
                        PRIMARY KEY (customer_id,field_name, from_date)
)
;

CREATE TABLE turnover (
                          customer_id      INT             NOT NULL,
                          turnover      INT             NOT NULL,
                          from_date   DATE            NOT NULL,
                          to_date     DATE            NOT NULL,
                          FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE,
                          PRIMARY KEY (customer_id, from_date)
)
;
```
Now we have a structure, but it is empty. As sample databases often comes filled with data, or at least with data upload scripts, I will skip this part.
It is good to remember that writing loading script contains a lot of repetitive items, and it can be automated.

#### Metadata

In our use case, the structure we've just added is a sample database of B2B customers of a company, e.g. e-commerce wholesale website. 
Now, something more advanced. Suppose we'd like to find out how database internals look like?
How to get database structure (tables and columns)? [Information schema](https://dev.mysql.com/doc/refman/8.0/en/information-schema.html) will help us.
We can get some metadata - for example, about tables - for each ``TABLE_SCHEMA``, meaning for both ``information_schema`` and ``customers`` (our database):

```sql
SELECT * FROM INFORMATION_SCHEMA.TABLES
```
To limit result to the proper database:

```sql
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'customers'
```

And even more info about user privilegies, constraints, created views (explained later).

> Trick: how to check MySQL version?

```sql
SELECT @@version
```
prints MySQL version as a result, e.g. ``5.7.32``.

#### Create and alter custom table

Let's add another table to our database of business customers, optionally with constraints. 

```sql
-- create table in the db currently in use, set columns and their type
CREATE TABLE company (
    company_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR (255) NOT NULL,
    established_date DATE,
    hq_country VARCHAR (4),
    PRIMARY KEY (company_id),
    -- constraint on column
    -- CHECK (established_date >= '2000-01-01')
    -- constraint on multiple columns
    -- CONSTRAINT CHK_Company CHECK (established_date >= '2000-01-01' AND hq_country NOT LIKE 'Z%')
);
```
Alter table by inserting column:

```sql
-- add column with ALTER TABLE
ALTER TABLE company
    ADD customer_id INT;
```

Finally, insert some small amount of records:

```sql
-- insert rows
INSERT INTO company (name, established_date, hq_country)
VALUES ('Sunrise Ltd', '1987-06-26', 'JPN'),
('SEOUL_88', '1988-09-17', 'KOR');
```

Using the metadata, we can check if everything went well.

Let's take this simple table with small amount of data: ``company``. What columns are inside?
**Backticks are required in some flavours**.

```sql
SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS`
WHERE `TABLE_SCHEMA` = 'customers' AND `TABLE_NAME` = 'company';
```
It returns:

```text
company_id
name
established_date
hq_country
customer_id
```

Now we can play with basic SQL commands.

#### Basic SELECT command

Starting with legendary SELECT - it works as universal tool for everything - also for debugging:

```sql
SELECT 'Hello world!'
```
not only returns result row as it was ```'Hello world!'``` record in ```'Hello world!'``` column but also prints it to the console:

```shell
customers> SELECT 'Hello world!'
[2023-04-04 20:52:22] 1 row retrieved starting from 1 in 28 ms (execution: 7 ms, fetching: 21 ms)
```
So we may know that query console accepts and processes queries. 

#### SELECT can do the math!

``SELECT`` can work as **math operator**: it evaluates expressions, for example boolean (logic) value:

```sql
-- evaluate boolean value of query (0 as false, 1 as true)
SELECT 1 = 1 AS boolean_value; -- 1, true
SELECT 0 = 1 AS boolean_value; -- 0, false and so on.
SELECT 1 < 0 AS boolean_value;
```

It also serves for doing some Boolean algebra. ``true`` and ``false`` are boolean values keywords used in expressions. SQL returns ``1`` and ``0`` as numeric equivalents of true and false when evaluating expressions.
And all that heavily reminds me of studying logic at the university, intensive three hours course every Friday morning. Old school.

```sql
SELECT true AND true AS result; -- returns true (1)
SELECT false AND true AS result; -- returns false (0)
```
Thus, we can simulate **[XOR gate](https://en.wikipedia.org/wiki/XOR_gate)** (FYI: XOR gate returns true when **only one side is true**, no more, no less):

```sql
SELECT
    false XOR false, -- 0
    false XOR true, -- 1
    true XOR false, -- 1
    true XOR true; -- 0
```

We can use ``1`` and ``0`` instead of ``true`` and ``false``:

```sql
SELECT
    0 XOR 0, -- 0
    0 XOR 1, -- 1
    1 XOR 0, -- 1
    1 XOR 1; -- 0
```

Setting constants, using **ternary operator** and aliases, the XOR gate could be nicely formatted:

```sql
SET @false_xor := 'gate returns false';
SET @true_xor := 'gate returns true';
SELECT
    IF(0 XOR 0, @true_xor, @false_xor) AS '0 XOR 0',
    IF(0 XOR 1, @true_xor, @false_xor) AS '0 XOR 1',
    IF(1 XOR 0, @true_xor, @false_xor) AS '1 XOR 0',
    IF(1 XOR 1, @true_xor, @false_xor) AS '1 XOR 1'
```

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/XOR.png"  alt="XOR gate" title="SQL XOR gate">
</div>
<br>

**SQL ternary operator** explained:

```sql
-- TERNARY OPERATOR
SELECT IF(0 = 0, 'TRUE', 'FALSE') AS ternary_operator; -- returns second arg
SELECT IF(1 = 0, 'TRUE', 'FALSE') AS ternary_operator; -- returns third arg
```

**SQL boolean operators** explained:

```sql
-- AND (&& in some dialects)
SELECT 1 > 0 AND 1 = 1;
SELECT 1 < 0 && 1 = 1;

-- OR (|| in some dialects)
SELECT 1 < 0 OR 1 = 1;
SELECT 1 > 0 || 1 = 1;

-- <> (!=)
SELECT 1 <> 0;
SELECT 1 != 0;

-- NOT (sometimes: !)
SELECT NOT 1 != 0;

-- XOR
SELECT 1 = 0 XOR 1 = 1
```

And finally - ``SELECT`` can do the math:

```sql
-- arithmetic operators
SELECT 1 + 1 * 1 /  3;
SELECT 3 % 5
```

#### Basic SELECT use cases

Normally, ``SELECT`` is used for less trivial purposes, like selecting (querying) data from tables in various configurations:

```sql
-- select all columns from given table
SELECT * FROM company;
-- select (view) particular columns only
SELECT name, country FROM company;
-- select with where clause
SELECT * FROM company WHERE hq_country = 'JPN';
```

``LIKE`` keyword allows more elastic searching strategy:

```sql
-- PLACEHOLDER
-- returns S plus 7 other chars replaced by underscore
SELECT * FROM company WHERE name LIKE 'S_______';
```

```sql
-- WILDCARD
-- returns sth before - L - sth after
SELECT * FROM company WHERE name LIKE '%L%';
```
When ``SELECT`` is used without ``FROM``, nothing is selected (retrieved). In fact, we simulate query result with fake data and aliases:

```sql
-- SELECT WITHOUT FROM
SELECT 123;
SELECT 9999 AS id, 'mock company' AS company_name;
```

**Null checks** - well known among Java developers:

```sql
-- NULL CHECK
SELECT IFNULL('GIVEN ARGUMENT (NOT NULL)', 'Exception: given argument is NULL') AS null_check; -- returns first arg, because it's not null (String)
SELECT IFNULL(null, 'Exception: given argument is NULL') AS null_check; -- returns second arg, because first arg is null
```

Example of use:

```sql
SELECT *, IFNULL(customer_id, 'OMG, customer id is null !!!') AS null_check FROM company
```

Result of null check is placed in additional column. In case of null, the null value is replaced by exception message:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/sql_null_check.png"  alt="SQL null check example" title="SQL null check example">
</div>
<br>

``COALESCE`` function returns first non-null value available from a list:

```sql
SELECT COALESCE(NULL, NULL, 'it is a first not null arg!') AS coalesce; -- returns first not null arg available (so it prints the string in coalesce column)
```

Let's complete this part of article with ``CASE`` clause. Programmers should be familiar with it:

```sql
-- CASE
SELECT
    company.name, hq_country,
    CASE
        WHEN hq_country LIKE 'JPN' THEN 'OK'
        WHEN hq_country LIKE 'KOR' THEN 'OK'
        ELSE 'Non-Asian'
        END AS country_check
FROM
    company;

-- CASE - another version
SELECT
    company.name, hq_country,
    CASE
        hq_country
        WHEN 'JPN' THEN 'OK'
        WHEN 'KOR' THEN 'OK'
        ELSE 'Non-Asian'
        END AS country_check
FROM
    company;
```

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/sql_case.png"  alt="SQL case example" title="SQL case example">
</div>
<br>

TBC