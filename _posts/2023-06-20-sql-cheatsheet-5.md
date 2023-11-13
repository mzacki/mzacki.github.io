---
layout: single
title: "SQL cheatsheet: part 5"
date:   2023-06-20 04:23
description: "Medium SQL for Java developers: recapitulation."

categories:
- SQL, database, data

tags:
- SQL, database, data, persistency

---

Previously on SQL: [CRUD, n+1, migrations](/sql-cheatsheet-4)

### Summary: SQL basics for Java devs

Practice your SQL skills. Do not have a feeling that you need to start from the scratch over and over again! 
In particular, if you're a beginner, or you do not work with SQL very often (it is not uncommon).
SQL problems can be like a Nemesis: I saw senior architects hairs growing grey because of complex SQL issues affecting company performance.
They were stammering trying to admit that there is a bug that no one is able to easily resolve.

You can excercise SQL kata at various programming website that I've already mentionned earlier.

You are also in position to use any of SQL playgrounds (a.k.a. fiddles), accessible in the web, to run and test some simple query, like [db-fiddle](https://www.db-fiddle.com/){:target="_blank"}
It is more like shadow-fighting: you try something, and when it fails, you need to counter your imaginative opponent.
For testing more advance queries and their performance, local database, Docker database or remote cloud database would be better along with any of SQL
clients, like Workbench or IntelliJ.

Create a basic SQL schema - to have a sample table:

```sql
CREATE TABLE test (
    id INT
);
INSERT INTO test (id) VALUES (1);
INSERT INTO test (id) VALUES (2);
```

Let's check what this fiddle offers:

```sql
SELECT @@version;
-- 5.7.38
```

and now sample queries to play with - note that this fiddle requires backticks, which weren't needed in previous examples:

```sql
ALTER TABLE `test` add customer_id int;
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE `TABLE_NAME` = 'test';
SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME` = 'test';
SELECT 'anything';
```

Now, remember, that ```SELECT``` can evaluate Boole's algebra expressions, as well as it can execute arithmetic calculations:

```sql
SELECT 1 < 0 AS boolean_value;
SELECT IF(2 + 2 = 4, 'TRUE', 'FALSE') AS two_plus_two_is_four;
```

The SQL playground I tested did not have problems with more advanced XOR gate example (see first part of this SQL series):

```sql
SET @false_xor := 'gate returns false';
SET @true_xor := 'gate returns true';
SELECT
    IF(0 XOR 0, @true_xor, @false_xor) AS '0 XOR 0',
    IF(0 XOR 1, @true_xor, @false_xor) AS '0 XOR 1',
    IF(1 XOR 0, @true_xor, @false_xor) AS '1 XOR 0',
    IF(1 XOR 1, @true_xor, @false_xor) AS '1 XOR 1'
```

Basic ```SELECT``` can be wrapped into **null check clause**: if value of the field is null, it will be filled with given substitute:

```sql
SELECT IFNULL(customer_id, 'it is null, though!') AS 'null checked customer_id' FROM test WHERE id = 1
```

```text
---

**Query #1**

    SELECT IFNULL(customer_id, 'it is null, though!') AS 'null checked customer_id' FROM test WHERE id = 1;

| null checked customer_id |
| ------------------------ |
| it is null, though!      |

---

```

Let's extend the table with some text column:

```sql
-- create new column:
ALTER TABLE `test` add country varchar(3);
-- and then add something there:
INSERT INTO test (id, customer_id, country) VALUES (7, null, 'FIN');
INSERT INTO test (id, customer_id, country) VALUES (8, null, 'NOR');
```

or update existing records with new values:

```sql
UPDATE test SET country = 'FIN' WHERE id = 1;
UPDATE test SET country = 'SWE' WHERE id = 2;
```

To check equality, use **equal sign**:

```sql
SELECT * FROM test WHERE country = 'FIN'
```

To make loose comparision, use **the percent sign** as a wildcard on a given side of look up expression:

```sql
SELECT * FROM test WHERE country LIKE '%N%';
SELECT * FROM test WHERE country LIKE '%N';
SELECT * FROM test WHERE country LIKE 'N%';
```
One wildcard replaces one or more characters.
The underscore replaces one character:

```sql
SELECT * FROM test WHERE country LIKE '__N'
```

The last but not least, the coalesce keyword returns first non-null value of these listed in parentheses:

```sql
SELECT COALESCE(country, "Unknown") FROM test;
```
It is used to replace null value with a substitute:

```shell
FIN
Unknown
FIN
NOR
```

### Aggregations

Let's create new schema with two tables to test aggregations:

```sql
CREATE TABLE country
(
    id   INT,
    code VARCHAR(3)
);
INSERT INTO country (id, code)
VALUES (1, 'SWE'),
       (2, 'FIN'),
       (3, 'NOR'),
       (4, 'ISL'),
       (5, 'DNK');


CREATE TABLE player
(
    id    INT,
    name  VARCHAR(5),
    city  VARCHAR(10),
    games INT
);

INSERT INTO player (id, name, city, games)
VALUES (1, 'Swen', 'Kiruna', 10),
       (2, 'Antti', 'Kotka', 11),
       (3, 'Marit', 'Bergen', 13),
       (4, 'Katja', 'Keflavik', 4),
       (5, 'Karin', 'Odense', 22);
```

Here are all aggregation commands:

```sql
SELECT COUNT(*) AS count_all_records,
MAX(games), 
MIN(games), 
AVG(games)
FROM player
```

Counting occurences of each name (and grouping by the same name):

```sql
SELECT name, COUNT(*) AS occurences
FROM player
GROUP BY (name)
```

Counting occurences of names with 'K':

```sql
SELECT name, COUNT(name) AS occurences
FROM player
WHERE name LIKE '%K%'
GROUP BY (name)
```

Group by played games and count how many players achieved this number:

```sql
SELECT games, COUNT(games) AS players_with_this_qty_of_games
FROM player
GROUP BY (games)
HAVING count(games) < 13
```

**Remember**: `WHERE` is used before `GROUP BY`, `HAVING` after `GROUP BY`. 

**Both can be used in the same query!**

### Joining

```UNION``` joins results of one query with results of another query from the same table, but omits duplicated rows (which matches both queries).
You can ```UNION``` different tables, but number of colums in one table must equal numer of columns in the other table.

```sql
SELECT * FROM player WHERE games > 10
UNION
SELECT * FROM player WHERE name LIKE '%K%'
```

```UNION ALL``` allows duplicates, so Karin would be listed twice.

Let's change id column names to precisely indicate what id they are referring to.

```sql
CREATE TABLE country (
                         country_id INT,
                         code VARCHAR(3)
);
INSERT INTO country (country_id, code) VALUES (1, 'SWE'),
(2, 'FIN'), (3, 'NOR'), (4, 'ISL'), (5, 'DNK');


CREATE TABLE player (
                        player_id INT,
                        name VARCHAR(5),
                        city VARCHAR(10),
                        games INT
);
INSERT INTO player (player_id, name, city, games) VALUES (1, 'Swen', 'Kiruna', 10),
(2, 'Antti', 'Kotka', 11),(3, 'Marit', 'Bergen', 13),(4, 'Katja', 'Keflavik', 4),(5, 'Karin', 'Odense', 22);
```
Let's update schemas by adding primary and foreign keys, required in joining operations.

```sql
ALTER TABLE player ADD country_id INT;
-- this does the trick: by accident, country_id should be the same as player id, so let's take advantage of that
UPDATE player SET country_id = player_id;
```

```INNER JOIN``` joins table using primary and foreign keys. Inner join syntax looks like this:

```sql
SELECT * FROM
    customer
INNER JOIN
    field f ON customer.customer_id = f.customer_id
```

Apply it to our schema:

```sql
SELECT * FROM player INNER JOIN country ON player.country_id = country.id
```

Alternative syntax with ```USING```. Foreign key column name: country_id in `Player` table matches country_id in `Country` table.
It is required when using ```USING``` keyword, so that SQL knows how to connect tables via columns.

```sql
-- USING
SELECT * FROM
    company
        INNER JOIN
    customer USING(customer_id);
```

With `Player` and `Country` tables:

```sql
SELECT * FROM player INNER JOIN country USING (country_id)
```

```INNER JOIN``` can be applied to more than two tables. You can also join using third, "helper" table.

```CROSS JOIN``` makes Cartesian product if no `WHERE` is specified (each row x each row). With `WHERE`, it joins:

```sql
SELECT * FROM player CROSS JOIN country WHERE country.country_id = player_id
```

### LEFT JOIN, RIGHT JOIN, OUTER JOIN

What is the difference between them?

**ChatGPT offers concise summary:**

`LEFT JOIN`
* Also known as a `LEFT OUTER JOIN`.
* Returns all the rows from the left table (the table mentioned before the `LEFT JOIN` clause) and the matching rows from the right table (the table mentioned after the `LEFT JOIN` clause).
* If there are no matching rows in the right table, `NULL` values are returned for the columns of the right table.
* This type of join ensures that all rows from the left table are included in the result, with the possibility of additional data from the right table if a match exists.

```sql
SELECT employees.name, departments.department_name
FROM employees
LEFT JOIN departments ON employees.department_id = departments.id;
```
`RIGHT JOIN`
Also known as a `RIGHT OUTER JOIN`.
Returns all the rows from the right table and the matching rows from the left table.
If there are no matching rows in the left table, `NULL` values are returned for the columns of the left table.
This join is less commonly used than the `LEFT JOIN` but has the same purpose, ensuring that all rows from the right table are included in the result.

```sql
SELECT employees.name, departments.department_name
FROM employees
RIGHT JOIN departments ON employees.department_id = departments.id;
```

`FULL OUTER JOIN` (`OUTER JOIN`):
A `FULL OUTER JOIN` combines the result sets of both the left and right tables.
It returns all the rows from both tables and matches rows where the join condition is met. If there are no matches in either table, `NULL` values are returned for the columns from the table without a match.
The result includes all rows from both tables, ensuring that no data is excluded.

```sql
SELECT employees.name, departments.department_name
FROM employees
FULL OUTER JOIN departments ON employees.department_id = departments.id;
```
It's important to note that **not all database systems support `RIGHT JOIN` and `FULL OUTER JOIN` directly**, 
and you may need to use alternative methods to achieve the same results in those cases, such as swapping the order of tables or using UNION clauses.

See previous article on `JOIN`s: [union vs join, left join, right join, inner vs outter join](/sql-cheatsheet-3)