---
layout: single
title: "SQL cheatsheet: part 3"
date:   2023-04-26 01:23
description: "SQL basics for Java developers: union vs join, left join, right join, inner vs outter join."

categories:
- SQL, database, data

tags:
- SQL, database, data, persistency

---

Previously on SQL: [aggregations, group by, where vs having](/sql-cheatsheet-2)

Today let's talk about joining results of different searches.

#### Union

``UNION`` merges multiple queries as one result. Here we are selecting exemplary, non-existing records and their aliases:

```sql
-- UNION merges multiple queries as one result
SELECT
    1 AS id, 'Sunrise Ltd.' AS name
UNION
SELECT
    2 AS id, 'Sunset Co.' AS name;
```

``UNION`` acts like [union operator known from set theory, algebra of sets and Boolean algebra](https://en.wikipedia.org/wiki/Union_(set_theory)).

Only distinct rows are included. There should be a difference in at least one field:

```sql
-- only distinct rows are included: prints only one record
SELECT
    1 AS id, 'Sunrise Ltd.' AS name
UNION
SELECT
    1 AS id, 'Sunrise Ltd.' AS name;

-- selects both records:
SELECT
    1 AS id, 'Sunrise Ltd.' AS name
UNION
SELECT
    1 AS id, 'Sunset Ltd.' AS name;
```

``UNION ALL`` allows duplicated results:

```sql
-- UNION ALL allows duplicated rows
SELECT
    * FROM company
UNION ALL
SELECT
    * FROM company;
```

Now test it on real data - records matching first condition (```sql WHERE hq_country = 'JPN'```) are not re-selected by second part of query (```sql SELECT * FROM company```):

```sql
SELECT * FROM company WHERE hq_country = 'JPN'
UNION
SELECT * FROM company
```

This works like simple ```sql SELECT * FROM company``` - it does not duplicate the results:

```sql
SELECT * FROM company
UNION
SELECT * FROM company
```

Finally, a clean and logical example of unioning two selects. It takes everything from first set and add everything from the second one:

```sql
SELECT * FROM company WHERE hq_country = 'JPN'
UNION
SELECT * FROM company WHERE hq_country = 'KOR'
```

Of course, it is possible to union results from different tables.

```sql
-- UNION from different tables is possible but the result set must have same number of columns
-- error:
SELECT
    * FROM company
UNION ALL
SELECT
    * FROM customer;
```

It does not work, returning ``[21000][1222] The used SELECT statements have a different number of columns``.

Let's correct it, adjusting requested number of columns:

```sql
-- works:
SELECT
    name AS company_or_customer_name, customer_id as id FROM company
UNION ALL
SELECT
    CONCAT(last_name, ' ', first_name), customer_id FROM customer;
```

#### Inner join

``INNER JOIN`` connects records from two (or even more) tables. 

To match a record from one table to relevant record from another table, it uses
fields (columns) marked as **keys**: **primary key** and **foreign key**, so that primary key from a record in one table points to the foreign key of the relevant record in connected table.

Usually, ``id`` values are used as ``primary and foreign keys``.

```sql
-- INNER JOIN returns records with matching values in both tables (here: customer_id)
SELECT * FROM
    customer
INNER JOIN
    field f ON customer.customer_id = f.customer_id
WHERE
    f.field_name = 'Engineer';
```

Step-by-step explanation of the script:

```sql
-- take all records from ``customer`` table
SELECT * FROM customer
-- connect to records from ``field`` table
INNER JOIN field f
-- but only when ``customer_id`` in ``customer`` table (for given record) matches ``customer_id`` in ``field`` table
ON customer.customer_id = f.customer_id
-- Additional condition: do it only if ``field_name`` in ``field`` table is ``Engineer`` (and discard all the rest).
WHERE f.field_name = 'Engineer';
```

Primary to foreign key connection:

```sql
customer.customer_id -- primary key in ``customer`` table
= 
f.customer_id -- foreign key in ``field`` table
```

> Primary key - unique field or combination of fields, only one row with the same PK may exist in a table.

> Foreign key - field or combination of fields, indicates Primary key of a row in another table. May be unique or not.

``INNER JOIN`` works only for the records having not null primary key. 
It is logical. Without primary key, there is no way to connect a record with another table (foreign keys point to non-null primary keys).

```sql
-- INNER JOIN shows only the records from company that have customer id NOT NULL
-- use OUTER JOINS: LEFT / RIGTH JOIN etc. if you expect null fields to be included
SELECT * FROM
    company
INNER JOIN
    customer c ON company.customer_id = c.customer_id;
```

#### Using

Instead of explicitly connecting primary key to foreign key, we can indicate it via ``USING``:

```sql
-- USING
SELECT * FROM
    company
        INNER JOIN
    customer USING(customer_id);
```

#### Inner join on more than two tables

Inner join can connect records from more than two tables, provided that they contain relevant ids (foreign keys).
It is useful when multiple conditions using information from various tables are required.

```sql
-- INNER JOIN with two more tables, both containing customer_id
SELECT  * FROM
    customer
        INNER JOIN
    field f on customer.customer_id = f.customer_id
        INNER JOIN
    turnover t on customer.customer_id = t.customer_id
WHERE
    customer.registration_date > '2000-01-01'
   OR (
            customer.birth_date < '1980-01-01'
        AND
            t.turnover < 10000
    )
   OR (
            customer.birth_date < '1960-01-01'
        AND
            f.field_name NOT LIKE  '%Engineer%'
    );
```
Another example: joining using third, helper table:

```sql
-- JOIN through third table
SELECT *
FROM customer
         INNER JOIN
     branch_customers
     ON
         customer.customer_id = branch_customers.customer_id
         INNER JOIN
     branch
     ON
         branch_customers.branch_id = branch.branch_id
```

Use case - find customer number per branch:

```sql
-- how many CUSTOMERS per BRANCH ?
SELECT branch_name, COUNT(*) AS number_of_customers
FROM customer
         INNER JOIN
     branch_customers
     ON customer.customer_id = branch_customers.customer_id
         INNER JOIN
     branch
     ON branch_customers.branch_id = branch.branch_id
GROUP BY branch_name
```

#### Cross join

``Cross join`` joins each row of the first table with each row of the second table. This join type is also known as Cartesian join.

```sql
-- CROSS JOIN joins all rows from one table with all rows of second table
-- on given condition
-- without condition it makes Cartesian product
SELECT * FROM
    customer
        CROSS JOIN
    company
WHERE customer.customer_id = company.customer_id
```

#### Left, right and outer join

``LEFT JOIN`` shows everything from left table.

```sql
-- LEFT JOIN shows all rows from left table (company) - even the records that cannot be joined
-- with customer table records due to NULL customer_id in company table
SELECT * FROM
    company
LEFT JOIN
    customer c ON company.customer_id = c.customer_id
```

``RIGHT JOIN`` takes every record from right table.

```sql
-- on the other hand, RIGHT JOIN shows all rows from right table (customer)
-- - even the records that cannot be joined with company table
-- due to missing customer_id in company table
SELECT * FROM
    company
RIGHT JOIN
    customer c ON company.customer_id = c.customer_id
```

``OUTER JOIN`` lists all records from left and right, even if they have null as their id (so that they cannot be normally joined).

```sql
-- FULL OUTER JOIN lists all rows from both tables
-- no matter if NULL
-- FULL OUTER JOIN is not supported in MySql
-- workaround: https://www.xaprb.com/blog/2006/05/26/how-to-write-full-outer-join-in-mysql/
SELECT * FROM
    company
        LEFT OUTER JOIN
    customer c ON company.customer_id = c.customer_id
UNION
SELECT * FROM
    company
        RIGHT OUTER JOIN
    customer c ON company.customer_id = c.customer_id
ORDER BY company_id DESC
```

Some other workaround of ``FULL OUTER JOIN``:

```sql
-- workaround of FULL OUTER JOIN without using LEFT / RIGHT JOIN
SELECT * FROM
    company
        INNER JOIN
    customer
    ON
            company.customer_id = customer.customer_id
UNION
SELECT *, NULL, NULL, NULL, NULL, NULL, NULL FROM
    company
WHERE
    NOT
            company.customer_id
            IN
            (
                SELECT DISTINCT
                    company.customer_id
                FROM
                    company
                        INNER JOIN
                    customer
                    ON
                            company.customer_id = customer.customer_id
            )
   OR
    company.customer_id IS NULL
UNION
SELECT
       NULL, NULL, NULL, NULL, NULL,
       customer.customer_id, customer.birth_date, customer.first_name,
       customer.last_name, customer.gender, customer.registration_date
FROM customer
ORDER BY company_id DESC
    LIMIT 10;
```

#### UNION workarounds for JOIN

```sql
-- UNION workaround instead of OUTER JOIN (without LEFT / RIGHT JOIN)
-- customer_id must be not null
SELECT name, company.customer_id FROM
    company
        INNER JOIN
    customer
        ON
    company.customer_id = customer.customer_id
UNION
SELECT name, company.customer_id FROM
    company
WHERE
    NOT
        company.customer_id
    IN
        (
        SELECT DISTINCT
            company.customer_id
        FROM
            company
                INNER JOIN
            customer
                ON
            company.customer_id = customer.customer_id
    )
```
```sql
-- above workaround with all columns from both tables included
-- and rows with null customer_id
SELECT * FROM -- returns columns from company and customer
    company
        INNER JOIN
    customer
    ON
            company.customer_id = customer.customer_id
UNION
SELECT *, NULL, NULL, NULL, NULL, NULL, NULL FROM
-- returns colums from company only (no join), hence null to replace missing columns from customer
    company
WHERE
    NOT
            company.customer_id
            IN
            (
                SELECT DISTINCT
                    company.customer_id
                FROM
                    company
                        INNER JOIN
                    customer
                    ON
                            company.customer_id = customer.customer_id
            )
    OR
        company.customer_id IS NULL;
```

TBC