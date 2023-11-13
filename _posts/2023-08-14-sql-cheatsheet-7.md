---
layout: single
title: "SQL cheatsheet: part 7"
date:   2023-08-14 20:23
description: "Advanced SQL for Java developers: cursor, function, index."

categories:
- SQL, database, data

tags:
- SQL, database, data, persistency

---

Previously on SQL: [Advanced SQL for Java developers: procedure, view](/sql-cheatsheet-6)

### Coursor

The provided code is a SQL script that creates a stored procedure in a database.
This stored procedure uses a cursor to iterate through records in a table and display the values of the columns in each row as it traverses them.

```sql
-- db cursor is a kind of a pointer similar to Java iterator
-- implemented as stored procedure
-- cursor iterates through records one by one
DELIMITER $$
CREATE PROCEDURE pointer()
BEGIN
    -- variables holding values of colums in row that are currently being traversed by cursor
    DECLARE cursor_company_id INT;
    DECLARE cursor_company_name VARCHAR(255);
    DECLARE cursor_country_code VARCHAR(4);
    -- boolean variable (false / true flag) showing if cursor iteration is finished
    DECLARE iteration_completed BIT DEFAULT 0;
    -- cursor declaration
    DECLARE company_cursor CURSOR FOR
    SELECT company_id, name, hq_country FROM company;
    -- handler of continue type launched when not found occurs
    -- not found means no more rows to iterate
    -- in case of not found flag is raised
    DECLARE CONTINUE HANDLER FOR NOT FOUND
    SET iteration_completed = 1;
    -- opening cursor and fetching first row, procedure starts
    -- first row is mapped to declared variables
    OPEN company_cursor;
    FETCH company_cursor INTO cursor_company_id, cursor_company_name, cursor_country_code;
    -- 'WHILE' loop (do as long as no empty rows)
    WHILE iteration_completed = 0 DO
    SELECT cursor_company_id, cursor_company_name, cursor_country_code; -- displaying declared variables containing values currently being traversed by cursor
    FETCH company_cursor INTO cursor_company_id, cursor_company_name, cursor_country_code; -- map another row to declared variables, repeat the flow
    END WHILE;
    -- procedure ends, cursor closed
    CLOSE company_cursor;
END

```
Let's go through it step by step.

DELIMITER $$: This statement changes the delimiter used in the SQL script to $$. It allows you to define the stored procedure using multiple SQL statements within the procedure.

CREATE PROCEDURE pointer(): This line begins the definition of the pointer stored procedure. The procedure has no parameters.

BEGIN: This keyword marks the beginning of the procedure's executable code block.

DECLARE statements: In this section, several local variables are declared for storing values from the rows as the cursor iterates through the result set. These variables include cursor_company_id, cursor_company_name, cursor_country_code, and iteration_completed.

cursor_company_id: It will hold the company_id value from the current row.
cursor_company_name: It will hold the name value from the current row.
cursor_country_code: It will hold the hq_country value from the current row.
iteration_completed: This is a boolean variable used to indicate whether the cursor iteration is finished. It's initialized to 0 (false).
DECLARE company_cursor CURSOR FOR ...: Here, a cursor named company_cursor is declared. The cursor is associated with a SELECT statement that retrieves data from the company table, specifically the company_id, name, and hq_country columns.

DECLARE CONTINUE HANDLER FOR NOT FOUND ...: This line declares a handler for the NOT FOUND condition. It means that when the cursor reaches the end of the result set (no more rows to iterate), the iteration_completed variable will be set to 1, indicating that the cursor iteration is completed.

OPEN company_cursor;: This statement opens the cursor, allowing it to start iterating through the rows of the result set.

FETCH company_cursor INTO ...: This line fetches the first row from the cursor's result set and maps the values of the columns (company_id, name, and hq_country) to the corresponding declared variables.

WHILE iteration_completed = 0 DO ... END WHILE;: This section of the code creates a WHILE loop. The loop will continue executing as long as the iteration_completed variable is 0. Inside the loop, it displays the values of the declared variables containing the current row's data and then fetches the next row.

CLOSE company_cursor;: After the loop finishes, this statement closes the cursor to release resources.

END: This keyword marks the end of the stored procedure definition.

In summary, this stored procedure (pointer) iterates through the records of the company table using a cursor. 
It displays the values of each row's columns as it traverses them. 
The loop continues until there are no more rows to fetch, at which point the cursor is closed, and the procedure ends.

### What is SQL coursor for? 

In SQL, a cursor is a database object that allows you to retrieve and manipulate rows from a result set one at a time. 
Cursors are commonly used within stored procedures or other database objects to navigate through the records in a result set, perform operations on each record, and manage the flow of data processing.

Common use cases:

Iterating through records - cursors are used to loop through the rows returned by a query one by one, allowing you to perform actions on each row.

Processing and transforming data - cursors are helpful when you need to apply complex calculations, transformations, or business logic to individual rows within the result set.

Data validation and error handling - cursors can be used to validate data, perform data integrity checks, and handle exceptions or errors on a per-row basis.

Cursor-based pagination - cursors can be used for paginating large result sets. You fetch a certain number of rows at a time, improving performance and reducing memory consumption.

### Various kinds of  SQL coursor

There are multiple types of coursor, depending on SQL flavour:

Forward-only. This type of cursor can only navigate forward through the result set, making it suitable for read-only operations.

Scrollable cursors can move both forward and backward within the result set, allowing you to revisit previous rows.

A static cursor populates the result set at the time of cursor creation and the query result is cached for the lifetime of the cursor. 
A static cursor can move forward and backward direction. A static cursor is slower and use more memory in comparison to other cursor. 
Hence, you should use it only if scrolling is required and other types of cursors are not suitable.
No UPDATE, INSERT, or DELETE operations are reflected in a static cursor (unless the cursor is closed and reopened). 
By default, static cursors are scrollable. SQL Server static cursors are always read-only.

A dynamic cursor allows you to see the data update, deletion and insertion in the data source while the cursor is open. 
Hence, a dynamic cursor is sensitive to any changes to the data source and supports update, delete operations. By default, dynamic cursors are scrollable.

MySQL cursor is read-only, non-scrollable and asensitive.

Read-only: you cannot update data in the underlying table through the cursor.
Non-scrollable: you can only fetch rows in the order determined by the SELECT statement. You cannot fetch rows in the reversed order. In addition, you cannot skip rows or jump to a specific row in the result set.
Asensitive: there are two kinds of cursors: asensitive cursor and insensitive cursor. An asensitive cursor points to the actual data, whereas an insensitive cursor uses a temporary copy of the data. 
An asensitive cursor performs faster than an insensitive cursor because it does not have to make a temporary copy of data. However, any change that made to the data from other connections will affect the data that is being used by an asensitive cursor, therefore, it is safer if you do not update the data that is being used by an asensitive cursor. MySQL cursor is asensitive.

### Function

In SQL, a function is a database object that allows you to encapsulate a set of SQL statements or expressions into a reusable and named unit. 
Functions take zero or more input parameters, perform specific operations or calculations, and return a single value as their result. SQL functions can be used in queries, data manipulation, and various SQL statements to simplify and modularize database operations. 
There different types of SQL functions: scalar functions (return a single value), table-valued (return result set), aggregations, date-time functions and the like.
This is another story related to SQL server internals.

How can we program SQL function to count logarithm:

```sql
-- CREATE FUCTION
DELIMITER $$
CREATE FUNCTION logarithm(
    base INT,
    n INT
    )
    RETURNS INT DETERMINISTIC -- like idempotent but does not alter db state even in first call
BEGIN
-- DECLARE local variables
DECLARE a INT DEFAULT 2;
DECLARE b INT;
SET b = n;
IF base > 1
    THEN SET a = base -- log base cannot be 0 or 1, use default 2 in such case
IF n <= 0
    THEN RETURN NULL -- n must be > 0, return null as log cannot be counted
RETURN LOG(a, n);
END$$

-- HOW TO CALL
SELECT logarithm(2, 128)$$
```

### Index

It is a separate bunch of data, created from indexed field (column) and pointer to full record containing such field.
SQL indexes work by providing (theoretically) a faster way to retrieve data from a database table. 
Indexing creates a data structure that maps specific column values to their corresponding rows.
It's smaller than full record, contains less disk space, it's sorted allowing binary search, so it's faster to iterate through it.
As index record contains only the indexed field and a pointer to the original record, it stands to reason that it will be smaller than the multi-field record that it points to.
So the index itself requires fewer disk blocks than the original table, which therefore requires fewer block accesses to iterate through.

According to nice explanation in [MySQL manual](https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html):

> Indexes are used to find rows with specific column values quickly. 
> Without an index, MySQL must begin with the first row and then read through the entire table to find the relevant rows. 
> The larger the table, the more this costs. If the table has an index for the columns in question, 
> MySQL can quickly determine the position to seek to in the middle of the data file without having to look at all the data. 
> This is much faster than reading every row sequentially.

This is similar to hashing in data structures, like Hash Map. In fact, some SQL indexing methods are using hashing.
Most of MySQL indexes use B-Trees, some use R-Trees and hashes.

OK, but what are B-Trees? MySQL manual offers helpful [glossary of terms](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_b_tree)
with B-Tree concept explained. B-Tree is a data structure, but not the same as binary tree. B-Tree can have multiple children, binary tree only two children per node.

The index allows the database to avoid a full table scan (row by row), resulting in significantly faster query execution (in theory).
Instead of going through all the rows in the table, the database directly accesses the row(s) that match the condition.

Why I am writing "in theory"?

Indexes are especially beneficial for SELECT, WHERE, JOIN, and ORDER BY clauses, as they help the database engine quickly pinpoint the desired data. 
**However, it's important to note that indexes come with some trade-offs. **
They consume storage space and can slightly slow down write operations (INSERT, UPDATE, DELETE) because the index must be updated when the data changes.

[Postgres manual is also a great source of knowledge on indexes](https://www.postgresql.org/docs/current/indexes.html).

### Index - detailed explanation

Data stored on disk-based storage devices is organized into blocks, which serve as the fundamental unit of disk access. 
Each block is accessed as a whole, representing the smallest disk access (atomic) operation. 
The structure of disk blocks resembles that of linked lists, with each block consisting of a data section and a pointer indicating the location of the next node or block. 
Importantly, these blocks do not necessarily need to be stored consecutively on the disk.

Search operation on unsorted data is called linear search.

What is linear search and why it requires `(n+1)/2` accesses on average - when searching an unordered list with n elements?

##### Searching mechanism of linear search

In a linear search, you start searching from the beginning of the list and examine each element one by one until you find the target element or determine that it doesn't exist in the list. You stop as soon as you find a match.

##### Best-case scenario 

The best-case scenario is when the target element is found in the first position of the list. In this case, only one access is required.

##### Worst-case scenario 

The worst-case scenario is when the target element is located at the end of the list or is not present at all. In this case, you must access every element in the list to determine that the target element is not there.

##### Average-case scenario

To calculate the average number of accesses, you need to consider all possible positions of the target element in the list. On average, you would expect to find it somewhere in the middle, requiring roughly `(n+1)/2` accesses. This average assumes that the target element is equally likely to be in any position in the list.

> The formula (n+1)/2 represents the arithmetic mean or average of all possible access scenarios in a linear search. 
> It provides a reasonable estimate of the expected number of accesses needed to find an element when the position of the target element is not known in advance.

This is a result of the way linear searches work and is based on the concept of "expected number of accesses." It's just an estimation.

In practice, the actual number of accesses in a specific search may vary, but the `(n+1)/2` formula provides a useful average estimation for the linear search algorithm's performance.


##### What in case of non-unique fields?

`(n+1)/2` is appropriate only if we search for a unique value (which cannot be doubled) - so once it is found, no need to search for more of them.
If searched record is a non-key field (i.e. doesn’t contain unique entries), we must find all fields that matches expectation, 
so the entire table must be searched. Then it requires `n` block accesses.

##### What if data are sorted?

When data is stored in a sorted field, you can employ a Binary Search algorithm to locate specific values. 
Binary Search is highly efficient and typically requires `log2(n)` block accesses to find a particular value. Here, `n` represents the number of elements or records in the sorted field.
(In contrast, a linear search in an unsorted field might require `n` block accesses in the worst case, which is significantly less efficient.)

##### What about duplication problem?

In a sorted field, once a value higher than the target value is found during the search, you can be confident that the target value doesn't exist in the remaining portion of the field. 
This is because, in a sorted field, all values are ordered, and any duplicate values would be adjacent to each other. 
Therefore, you don't need to continue searching for duplicate values once a higher value is encountered.

The combined effect of using Binary Search and the elimination of duplicate searches in a sorted field results in a substantial performance increase compared to an unsorted field. 
It allows for quicker and more efficient retrieval of data, especially when searching for specific values or performing range-based queries.

In summary, the key advantages of using a sorted field include the efficiency of Binary Search and the ability to eliminate duplicate searches, both of which significantly enhance query performance and data retrieval speed.

##### Index comes into play

And now, indexing comes into play.

As this great [StackOverflow article](https://stackoverflow.com/questions/1108/how-does-database-indexing-work) explains:

> Indexing is a way of sorting a number of records on multiple fields. 
> Creating an index on a field in a table creates another data structure which holds the field value, and a pointer to the record it relates to. 
> This index structure is then sorted, allowing Binary Searches to be performed on it.

To sum up, indexing takes advantage of the fact that data are sorted, and it allows to use searching algorithms that are more efficient than simple linear search.

##### Are there any drawbacks of indexes?

Here, I would like to quote StackOverflow again:

> The downside to indexing is that these indices require additional space on the disk since the indices are stored together in a table using the MyISAM engine, 
> this file can quickly reach the size limits of the underlying file system if many fields within the same table are indexed.









