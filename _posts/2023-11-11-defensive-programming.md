---
layout: single
title: "Defensive programming in Java"
date:   2023-11-11 16:53
description: "Defensive programming: exception handling, try-with-resources, AutoCloseable and Closeable, assertions."

categories:
- security

tags:
- defensive_programming, security

---

In this article we will discuss the principles of defensive programming in Java.
Defensive programming involves writing code that anticipates and protects against potential errors and unexpected situations 
to ensure the reliability and robustness of a software.

### Ensure proper exception handling

As a reminder: all exceptions (including errors) are subclasses of `Throwable` class.

`Throwable` objects - commonly called **Exceptions** - can be either **exceptions** or **errors**.

Because of that, `Throwable` has two subclasses - `Error` and `Exception`:

`Throwable -> Error`

`Throwable -> Exception`

**What is the difference between error and exception?**

> Errors in Java represent serious, usually unrecoverable problems that occur at runtime.
> They are rather related to a sudden change of state of the application and its environment.
> They are typically caused by issues outside the control of the program, such as system failures, hardware problems, or severe environmental conditions.

Errors are **unchecked**: they are not meant to be caught or handled by the application code. 

Examples of errors: `OutOfMemoryError`, `StackOverflowError`

> Exceptions are related to abnormal conditions or unexpected situations during the execution of a program. 
> Exceptions are often caused by faulty code or invalid inputs.

Exceptions are either meant to be caught and handled by the application (when they are "checked"), or they should be avoided by correct program logic, including input validation (when they are "unchecked").

Proper handling of checked exceptions  requires try-catch blocks to catch exceptions, implementing error-handling logic (exception handlers, e.g. using Spring framework) or recovery mechanisms (also possible with Spring).

Examples of unchecked exceptions: infamous `NullPointerException`, `ArrayIndexOutOfBoundsException`. `<--- should not be caught & handled, should be avoided by correct coding!`

Examples of checked exceptions: `IOException`, `FileNotFoundException`, `SQLException`. `<--- we should predict them and be prepared (catch & handle)!`

So exceptions can be **checked** or **unchecked**.
Unchecked exceptions are subclasses of `RuntimeException`.
All other exceptions are checked. They are directly under `Exception` class.

`Throwable -> Exception -> RuntimeException -> (unchecked exceptions)`

`Throwable -> Exception -> (checked exceptions)`

To sum up, unchecked issues are Errors, RuntimeException and its descendants. 

The Exception class and its descendants are checked issues, and they require proper handling. 
When such exception has been thrown, the control flow is transferred to the nearest exception handler.
In Java, checked exceptions are tracked by the compiler.

**Exception rethrowing and chaining**

**Exception rethrowing** is simply throwing again caught exception in `catch` clause.

**Exception chaining** is wrapping caught exception into a new one (of another class) 
It is also useful if a checked exception occurs in a method that is not allowed to throw a checked exception. 
You can catch the checked exception and chain it to an unchecked one.

Use `initCause`, `getCause()` and a constructor with `Cause` to pass original exception so that it could be retrieved later.

### Exception handling and security

Now it is important to ask what is the meaning of proper exception handling for application security.

Handling the exceptions may be seen as trivial, but often it is not, leading to non-readible, overcomplicated spaghetti code, with nested try-catch clauses.

Not all business scenarios are "happy path".

When exception handling is simple and robust, potential vulnerabilities or sensitive information exposure are handled safely to prevent security breaches.
It increases software integrity then. The control over the system is enhanced, with greater stability and easier monitoring.
It allows to prepare right responses to exceptional situations.

### Close the resources: try-with-resources, Autocloseable vs Closeable

Exception handling in resource management is another problem.

```java
// suppose there is some collection to iterate through it:
var input = new ArrayList<String>();
// resources comes into play...
PrintWriter out = new PrintWriter("output.txt");
for (String row : input) {
out.println(row.toLowerCase());
}
// when exception occurs, this code is never reached:
out.close();
```

Try-with-resources guarantees that the resources are closed regardless the exception:

```java
var out = new PrintWriter("output.txt");
try (out) {
for (String row : input)
out.println(row.toLowerCase());
} // out.close() is called implicitely!
```
`out.close()` method is called behind the scenes, because `PrintWriter` implements `AutoCloseable`.
The resources will be closed when try-with-resources exits, no matter if an exception has been thrown.
And no need to worry about closing resources when the code executes normally. It happens automagically!

> Closeable implements AutoCloseable so it can use try-with resources to automatically close the resources.
> Autocloseable: close() throws Exception
> Closeable: close() throws IOException. Older interface, specifically designed for I/O-related classes.

When there are two resources declared and initialized in try() clause (which is a valid and acceptable case):

```java
try (Scanner in = new Scanner(Paths.get("/usr/share/dict/words"));
PrintWriter out = new PrintWriter("output.txt")) {
while (in.hasNext())
out.println(in.next().toLowerCase());
}
```
Here:

- [x] Resources are closed in reverse order of their initialization: `out` is closed before `in`.
- [x] When PrintWriter throws exception, try() mechanism closes `in` and propagates the exception from `out`.

### Suppression mechanism

Very interesting aspect is discussed by Cay S. Horstmann in his Core Java:

> Some close methods can throw exceptions. If that happens when the try block completed normally, the exception is thrown to the caller. 
> However, if another exception had been thrown, causing the close methods of the resources to be called, and one of them throws an exception, 
> that exception is likely to be of lesser importance than the original one. In this situation, the original exception gets rethrown, and the exceptions
> from calling close are caught and attached as “suppressed” exceptions.

After catching the first exception, it is possible to get to the supressed exception using `getSuppressed()`:

```java
try {
// something here throws the more important exception
} catch (IOException ex) {
Throwable[] secondaryExceptions = ex.getSuppressed();
// here we can catch supressed
}
```
**Do not throw from finally**.

The suppression mechanism works only for try-with-resources. **Do not throw exceptions in `finally` clause**. 
If try() block terminates with an exception, this exception is masked by an exception
in `finally` clause.

**Do not return from finally**.

If try() has `return` statement and there is another `return` in `finally`, the latter (`return` from `finally`) overshadows the former (from `try`).

### Resource management and security

While try-with-resources itself is not a security feature per se, 
it does play a role in promoting secure coding practices and **preventing resource leaks** that could lead to security vulnerabilities.

By automatically managing the closing of resources, try-with-resources reduces the likelihood of resource leaks. 
Leaked resources, such as open file handles or network connections, can pose security risks and impact the stability of an application.

Properly closing resources prevents **resource exhaustion attacks** where an attacker intentionally consumes available resources 
(e.g., file descriptors) to degrade system performance or cause **denial-of-service** conditions.

For security-sensitive resources like cryptographic streams or database connections, try-with-resources ensures that they are properly released, 
**reducing the window of opportunity** for potential security vulnerabilities related to resource mismanagement.

Last but not least, with try-with-resources, handling exceptions related to resource cleanup is simplified.

### Assertions

Java supports assertions, which are boolean expressions that the programmer believes will be true at that point in the code. 
They are useful during development and testing to catch potential issues early.
They can be disabled in production code (actually, assertions are disabled by default).

Instead of checking the condition with `if`:

`if (x < 0) throw... `

which is expensive and slows down the program, we can use assertions.

Enable assertions:

```shell
java -ea MainClass
```

and then put into code:

```java
assert x > 0 : "x should be greater than 0";
```

We can enable assertions for single classer or packages as well:

```shell
java -ea:MyCustomClass -ea:com.mycompany.somepackage... MainClass
```

Assertions are handled by the class loader.
When they are disabled (by default!), the class loader removes all assertion code so that it won’t slow execution (e.g. in production environment).

We can customize disabling assertions as well using `-da` flag (a.k.a. switch):

```shell
java -ea:... -da:MyCustomClass MainClass
```

During the development phase, assertions can serve as a form of security audit by checking that security-critical conditions or assumptions are met. 
However, assertions should not be relied upon as the primary means of enforcing security.

### Many faces of defensive programming

Other rules of defensive programming are related to:

- [x] Null-checks
- [x] Immutable classes and defensive copying
- [x] Input validation (already discussed a propos SQL injection)
- [x] Proper logging
- [x] Design patterns & the use of tested and known algorithms 

It is particularly important not reinvent the wheel, when it comes to the existing algorithms. 
They are well-grounded in computer science theory, battle-tested by wide community and mesurable
in terms of efficiency and outcome. Writing own implementation is not always a good idea, as it may lead to inefficient behaviour and unpredictable results.
And unless you are a cryptography maven, do not try to implement your own cryptography solutions into industry-grade code.

TBC in next articles.




