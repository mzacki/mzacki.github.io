---
layout: single
title: "SQL injection and how to mitigate"
date:   2023-10-01 20:23
description: "SQL injection, query parametrization, prepared statements, input validation."

categories:
- SQL, database, data, security

tags:
- SQL, database, data, persistency, security

---

Previously on SQL: [Intro to SQL security](/sql-security-1)

### SQL injection attack

**SQL injection** - vulnerability that occurs when an attacker is able to manipulate SQL query or inject own SQL command (a part of SQL query) into an SQL query.

**SQL injection attack** - happens when a vector of attack is closely related with a SQL vulnerability and an attacker takes advantage of such vulnerabilty, injecting malicioius SQL code.

> SQL injection is widespread because it is easily detected and exploited!

Possible results of SQL injection attack: unauthorized access to database, unauthorized read of data, such as user login and passwords, 
data manipulation, taking control over the operating system.

### Malicious SQL

Suppose there is a web application that exposes the endpoint:

```txt
https://somecompany.com/customers/search?last_name=something
```
This endpoint allows searching for customers of this company in the application database. 
Let's imagine the app is connected to the `customers` database, similar to the one discussed in previous posts. 
Request sent to the endpoint executes some code of the application, performing search in the database. 
Let's say it is a method that triggers following query:

```sql
SELECT * FROM customer WHERE last_name LIKE '%something%' AND active=true
```

where `something` is mocking user input, pasted into the browser, e.g. into html input field.

In Java such method could look like this:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQueryExecutor {

    public static void executeSearchQuery(String lastName) {
        Connection connection = null;
        Statement statement = null;

        try {
            // Set up the database connection (replace with your database URL, username, and password)
            String dbUrl = "jdbc:mysql://your-database-url";
            String dbUser = "your-username";
            String dbPassword = "your-password";

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            statement = connection.createStatement();

            // Construct the SQL query using placeholders for search terms
            String sqlQuery = "SELECT * FROM customer WHERE last_name LIKE '%" + lastName + "%' AND department =1;

            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Process the results (you can replace this with your specific logic)
            while (resultSet.next()) {
                // Retrieve and process data here
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in the reverse order of their creation
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

```
With URL request for word: `malicious'`:

`https://somecompany.com/customers/search?first_name=malicious'`

SQL should be:

```sql
SELECT * FROM customer WHERE last_name LIKE '%malicious'%' AND active=true
```

but there is syntax error due to unescaped character (apostrophe / single quote symbol).

Single quote symbol denotes opening and closing a string; adding redundant character of this type closes string prematurely and opens another one without proper closing.

So when you put some special character into input box of a web application and the result is similiar to this message: `Incorrect syntax near...`,
it might suggest that the input had been directly injected into some backend SQL query.
And, apparently, this input character was executed as part of SQL query, which ended with SQL error message.
The conclusion: a bad actor (as bad as the actors from Mick Herron's Slough House series) can try to tinker with this vulnerability, 
looking for an opportunity to execute SQL injection attack.

There is a way to get rid of this error, commenting the rest of query, that should not be executed with two dashes:

```sql
-- this is SQL comment
```

> Note, that in some flavours (e.g. MySQL, MariaDB), the dashes should be followed by a new line character. See documentation.

With the comment trick, the URL will look like `https://somecompany.com/customers/search?first_name=malicious'-- `,
which triggers this query underneath:

```sql
SELECT * FROM customer WHERE last_name LIKE '%malicious'-- %' AND active=true
```

(Kramdown in Jekyll has been set to MySQL, so in a Markdown file we can see that with a trailing space it works perfectly fine. It should look fine
also for other plugins.)

By commenting the end of query, the second part of `WHERE` condition has been disabled.

As a result, the query returns all customers with given name (just put something instead of `malicious`), disregarding their department.

Let's add infamous `OR 1=1--` to the URL:

`https://somecompany.com/customers/search?first_name=malicious' OR 1=1-- `

so that we trigger another SQL query through above-mentionned Java method (which is, by the way, a strong evidence of bad practice):

```sql
SELECT * FROM customer WHERE last_name LIKE '%malicious' OR 1=1-- %' AND active=true
```

As the `OR` condition is tautology (always true), the database will return all customers irrespectively of their name.

By adding a boolean SQL command, evaluated to true, and attaching it to the `WHERE` clause, one can make the query always true. Open sesame!

Given that `OR` clause has precedence, the `WHERE` condition will be skipped and the query executed because of `true` condition after `OR`.

### Methods of SQL injection attack

- [x] Union-based

Using previously discussed mechanisms, in this scenaro `UNION` clause is attached to the initial query. By trial and error procedure
(using brute force or `ORDER BY` command), an attacker will know the numbers of columns
in a given table. He will also discover database metada from `information_schema.tables` (in some database engines), such as name of tabels and their columns.
During following steps, the perpetrator can see the content of database, including user logins, passwords (hased or not) and the like.
I will not paste examples of SQL queries nor detailed instruction, as it is easily accessible in the internet and in the books. 

- [x] Error-based

Error messages can contain information useful to execute the attack. An example is casting string to integer or concatenating character to the result of version function.
Actual syntax depends on SQL dialect.

- [x] Blind (content-based)

Using boolean condition and substring function, blind attack is similar to bruteforcing. Its goal is to get the table content by fetching entries by single characters.

- [x] Blind (time-based)

The fundamentals for this type of SQL injection is to check time between request and response.

- [x] Stacked queries

In Java, `java.sql.Statement.executeQuery()` method allows to execute only a single query.
If other possibility has been enabled in the database, a malicious actor can attach another SQL query to the initial one, executing SQL injection attack.
For example, by attaching `DROP DATABASE users--` command.

- [x] Login bypassing, saving & loading a file, execution of OS command, and the like

In badly secured applications, where stored passwords are not hashed, it is possible to bypass login gateway.
A hacker can also modify / update data, execute code remotely, load or save a file on a disk, and finally, inject commands into OS.

### How to detect vulnerability?

At the very basic level, SQL injection vulnerabilities can be noticed on the front-end side (UI) and by REST API behaviour (given that the REST API
fetches data from the database). Playing with web application search engines and endpoints through UI and http calls might be useful.
Use examples described earlier or find more SQL injection test cases for this purpose.

At the same time, the code base should be checked for vulnerabilities. 
Linters, code-style checkers, security scanners may detect potential dangers.
As we could see earlier, the main problem lies in String concatenation.

### Mitigation - query parametrization, prepared statements, stored procedures

From programmer's point of view, SQL injection can be mitigated using query parametrization, where input is not literally treated as part of SQL, but as a separate variable of type String,
which is substituted into wild cards of pre-prepared SQL query in a way that seriously limits the possibility of SQL injection.
In this solution, there is no String concatenation, so the risk is lower.

In Java, we can also use PreparedStatement. The refactored method looks like this:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SafeDatabaseQueryExecutor {

    public static void executeSearchQuery(String lastName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Set up the database connection (replace with your database URL, username, and password)
            String dbUrl = "jdbc:mysql://your-database-url";
            String dbUser = "your-username";
            String dbPassword = "your-password";

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Construct the SQL query with a placeholder for the last name
            String sqlQuery = "SELECT * FROM customer WHERE last_name LIKE ? AND department = 1";
            preparedStatement = connection.prepareStatement(sqlQuery);

            // Set the parameter for the prepared statement
            preparedStatement.setString(1, "%" + lastName + "%");

            // Execute the SQL query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the results (replace with your specific logic)
            while (resultSet.next()) {
                // Retrieve and process data here
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in the reverse order of their creation
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

```

Stored procedures are predefined queries stored on the database server, allowing repeated execution across various applications. 
When implemented appropriately, stored procedures provide a level of defense against SQL injection attacks comparable to that of prepared statements. 
Controlling permissions for executing a stored procedure enables us to regulate access and, if needed, limit direct interaction with the underlying table, 
thus mitigating the potential impact of SQL injection.

Similar to prepared statements, stored procedures support parameterized queries, treating user input as data rather than executable SQL code. 
Moreover, the database automatically sanitizes the parameters transmitted to the procedure, preventing malicious code from being injected by potential attackers.

> Stored procedures, like SQL queries, can be vulnerable to injection. 
> To prevent this, parameterize the stored procedure queries instead of concatenating parameters.

Do not concatenate query in Stored procedures:

```sql
    SET @Statement = CONCAT('SELECT * FROM customer WHERE name = ', customer_name, ' );
```

I would rather go for prepared statement with parameter:

```sql
PREPARE statement FROM 'SELECT * FROM customer WHERE name = ?';
```

More on avoiding SQL injection in Java and other languages (e.g. Python): [Bobby Tables](https://bobby-tables.com/java) and [OWASP](https://owasp.org/www-community/attacks/SQL_Injection)

### Frontend vs backend input validation? Both!

Sanitization involves the removal of undesirable characters (such as curly braces, quotes, slashes and backslashes) or unsafe code from user-supplied data. 
Validation, on the other hand, ensures that user-supplied data adheres to the expected format defined by the database. 
For instance, we can verify input length and reject excessively long inputs, as well as enforce specific formats for email addresses and dates. 
This approach effectively thwarts attackers attempting to submit specially crafted input values containing malicious SQL statements.

While sanitizing and validating input contributes to controlling the input in SQL queries, it's important to note that it's not foolproof. 
Attackers may employ techniques like double encoding to circumvent these safeguards.

Front-end validation is useful and user-friendly. User receives immediate information if an input is invalid (along with the reason - at least in well-designed applications).
Thanks to that the backend does not need to handle multiple incorrect request coming from the UI.

But front-end validation can be bypassed by a bad actor (malicious user or attacker). 
Validation on the front-end side will not secure against SQL injections.
Angular, JavaScript, TypeScript and other front-end code seen in the browser developer's tools as script can be manipulated and exploited. 

Not to mention that in client-server architecture one may execute direct HTTP call (e.g. via Postman or `curl`) 
to the backend application, bypassing UI validation. It would deliver a payload that has not been validated yet to the backend API.
Such payload could contain SQL query to execute during the attack on the backend side.

In fact, front-end validation should always be paired with backend validation, like Java & Spring Validation API.

### Whitelisting vs blacklisting: don’t rely on blacklisting

A whitelist (allowlist) enables us to establish precise rules that exclusively permit specific characters or patterns in the input, ensuring the rejection of any malicious input.

Compared to a blacklist (denylist), a whitelist proves to be a superior strategy for thwarting SQL injection attacks. 
By explicitly defining the permissible input types, a whitelist leaves less room for maneuvering, unlike a blacklist, which can be circumvented by attackers through input variation. 
In essence, a whitelist provides greater control over the accepted input.

Require proper formatting for text, data and numerical values. Use selecting option (drop-down, calendars) where possible.

### Restricted access - Principle of the least privilege (PoLP)

According to this well-known rule:

> Every module (such as a process, a user, or a program, depending on the subject) 
> must be able to access only the information and resources that are necessary for its legitimate purpose.
>  Saltzer, Jerome H.; Schroeder, Michael D. (1975). "The protection of information in computer systems"

Ensure that database accounts used by your application have the least necessary privileges to reduce the impact of a successful attack.

When establishing a database user for your application, it's essential to carefully consider the privileges assigned to that user. 
For instance, does your application necessitate full access to read, write, and modify all databases? 
Should it have the authority to truncate or drop tables? By restricting your application's access to the database, you can mitigate the potential impact of SQL injection attacks. Instead of relying on a single database user for your application, 
it is advisable to create multiple database users and associate them with specific application roles. 
Security vulnerabilities often propagate like a chain reaction, so it's imperative to remain vigilant about each link in the chain to prevent significant harm.

### Database hardening

- [x] An app should not have admin privileges when connecting to the database. Even is someone injects some malicious code, chances are the damage will be limited.
- [x] Users should be separated when connecting to given database, even within one application. Preferably, databases should be separated also. This should be doable in microservices. 
Thus, SQL injection to one table most probably won't extend to other tables, and breaking into one database will not hurt the others.
- [x] Disable **stacked queries** so that another SQL query would not be attached to the initial one during the attack. It will be more tedious and time-consuming
to fetch or alter the data step by step than to delete a table or a database at once.
- [x] Check given database engine for vulnerabilities. Disable dangerous options.
- [x] Database should not have `root` permissions in the operating systems

### ORM layer

Object-relational mapping (ORM) layer can also be your line of defense.

But do not shoot yourself in the foot: easy-to-use Hibernate framework applied with superficial knowledge only can be far less efficient than low-level JPA & JDBC solutions, not to mention pure SQL written by an expert in given database engine!

An ORM layer translates data between the database and objects bidirectionally, reducing explicit SQL queries and minimizing the risk of SQL injection. 
However, when custom queries are needed, Hibernate in Java introduces Hibernate Query Language (HQL), requiring careful use of the createQuery() function to mitigate injection risks. 
Despite the benefits, it's crucial to acknowledge that ORM libraries must convert logic back to SQL statements, necessitating trust in proper parameter escaping. 
To ensure the absence of SQL injection vulnerabilities, regularly scan for known weaknesses and avoid outdated library versions.