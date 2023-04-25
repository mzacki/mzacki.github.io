---
layout: single
title: "SQL cheatsheet: part 2"
date:   2023-04-19 18:23
description: "SQL for Java developers: aggregations, group by, where vs having. "

categories:
- SQL, database, data

tags:
- SQL, database, data, persistency

---

Previously on SQL: [basic SELECT, Boolean operators, mathematics and other tricks](/sql-cheatsheet)

#### Aggregations

Aggregations are SQL commands that add (sum up) values, count occurences, find minimum, maximum and average values from given columns.

Example of ``SUM`` - sum up the turnover from ``turnover`` column of ``turnover`` table, but only since the year 2000 (so since the famous Y2K bug :) ). Do not connect it with
customer ids nor anything else:

```sql
SELECT SUM(turnover) FROM turnover WHERE `from_date` > '2000-01-01';
```

``COUNT`` counts occurences:

```sql
-- count result
SELECT COUNT(name) FROM company WHERE name LIKE 'S%';
```

Select all companies from Japan or Korea and count them:

```sql
SELECT * FROM company WHERE hq_country IN ('JPN', 'KOR');
SELECT COUNT(name) FROM company WHERE hq_country IN ('JPN', 'KOR');
```

Now, ``MIN`` and ``MAX`` can find minimum and maximum among date values.
In this example, it shows the earliest and the latest date of beginning of a cooperation with given customer (when a customer started to create any turnover).

```sql
SELECT MIN(from_date), MAX(from_date) FROM turnover
```

Customers ordered by effective cooperation length descending, then max, min and average cooperation time:

```sql
SELECT customer_id, to_date, from_date, DATEDIFF(to_date, from_date) AS coop_length FROM turnover
ORDER BY coop_length DESC
```

```sql
-- count max, min, average
SELECT MAX(DATEDIFF(to_date, from_date)) AS longest,
       MIN(DATEDIFF(to_date, from_date)) AS shortest,
       AVG(DATEDIFF(to_date, from_date)) AS average
FROM turnover
```

Simple turnover average count for all customers since Y2K:

```sql
SELECT AVG(turnover) FROM turnover WHERE from_date >= '2000-01-01';
```

Detailed stats on turnover table:

```sql
-- AGGREGATION functions: COUNT, MAX, MIN, AVG, SUM
SELECT
    COUNT(*) AS 'turnover records',
    MAX(turnover) AS 'max turnover',
    MIN(turnover) AS 'min turnover',
    AVG(turnover) AS 'average turnover',
    SUM(turnover) AS 'in total'
FROM
    turnover;
```

#### Group by

Let's check whose first name is most popular in customers' database.

Select a query on how many customers have given first names.

So it translates to: select first names from customer table, group them (merge same names together), show occurences (how many identical first names were grouped / merged),
sort from most popular to least popular:

```sql
-- COUNT grouped by GROUP BY
SELECT first_name, COUNT(*) as occurences FROM customer
GROUP BY first_name
ORDER BY occurences DESC;
```

In this way we can count how many emails will be sent by emailing framework for name days (trivial example).
Grouping is easy, but, in practice, very useful.

Now, conditional grouping with ``HAVING``:

```sql
-- HAVING
SELECT first_name, COUNT(*)
FROM customer
GROUP BY first_name
HAVING COUNT(*) > 250
ORDER BY COUNT(*) DESC
```
And conditional grouping with ``WHERE``:

```sql
-- WHERE is used before GROUP BY, after GROUP BY - HAVING
SELECT first_name,
       COUNT(*)
FROM customer
WHERE first_name
          LIKE 'H%'
GROUP BY first_name
HAVING COUNT(*) > 200
ORDER BY COUNT(*) DESC;
```

What is the difference between ``HAVING`` and ``WHERE``? **WHERE** is used before **GROUP BY**, **HAVING** after **GROUP BY**.

#### Limit

In case of one argument, ``LIMIT`` simply shows first x rows, ommiting the rest. So the result set is limited to given number of rows:

```sql
SELECT * from company LIMIT 5;
```
It shows top 5 companies (sorted by ID).

In case of two arguments, ``LIMIT`` ommits first x rows, then it includes following y rows (showing y rows in total):

```sql
-- first argument: number of rows to omit
-- second argument: number of rows to include
SELECT
    company.name, hq_country,
    CASE
        hq_country
        WHEN 'JPN' THEN 'OK'
        WHEN 'KOR' THEN 'OK'
        ELSE 'Non-Asian'
        END AS country_check
FROM
    company
LIMIT 2, 10;
```
Here it skips first two rows and shows other ten rows.

#### Why and where to learn SQL?

Once, I knew a business analyst who was a professional thanks to his knowledge of **SQL** and **Python**. Later, he went to **Big Data Engineering**.

Knowledge of SQL not only makes your work easier, but it gives you an opportunity to be a better computer scientist. It extends the area of expertise.
False SQL-beginners (mainly developers) often forget SQL basic commands and persistency-related topics because they do it rather rarely. Hence, it is good to train regularly.

**Codewars** has a collection of [SQL problems for beginners](https://www.codewars.com/collections/sql-for-beginners) to solve. Their backlog also offers
[more advanced kata](https://www.codewars.com/kata/search/sql?q=&beta=false&order_by=rank_id%20desc). 

At **Hackerrank** we will find [preparation tasks](https://www.hackerrank.com/domains/sql) and [SQL certification (basic, intermediate and advanced level)](https://www.hackerrank.com/skills-verification/sql_advanced).

TBC