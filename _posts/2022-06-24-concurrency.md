---
layout: post
title:  Concurrency
date:   2022-06-24 18:27
description: Basics of concurrency in Java

categories:

- Java

tags:

- concurrency, Java, multithreading

---

### Thread state values 

Enum ```Thread.State```

```NEW```

All new threads start from here. A thread not yet started. It is created, but ```start()``` method not yet called.

```RUNNABLE```

A thread during the execution in JVM: running or at least available to run once scheduled.

```BLOCKED```

A thread is waiting to acquire a monitor lock. Then it will be able to execute ```synchronized``` code.

```WAITING```

A thread that is waiting for another thread (without any timeframe specified). ```Object.wait()``` or ```Thread.join()``` called.

```TIMED_WAITING```

A thread that is waiting for another thread for a specified period of time. ```Thread.sleep()```,
```Object.wait()``` or ```Thread.join()``` called with timeout value.

```TERMINATED```

A thread completed execution. ```run()``` method exited or throwed an exception.

### First things first: 5 rules of concurrency

- Every thread has its own **stack**, but all threads share the same **heap** and  **address space** in memory.
- For threads, objects are **visible by default** - basically, every thread can access any given object by a reference or a copy of this reference. 
**A reference** is a pointer to a location in memory (where an object has been located).
- Objects are generally **mutable**.  If a reference variable is **final**, it cannot be changed by pointing to another object, 
but **the object itself can be still modified**. One can create an immutable object, but it is a different story.
- The keyword ```synchronized``` helps make code *thread safe* (or *concurrently safe*), **but it is not enough**.
- Thread safety is **not only** about **writing** operation on objects: it is also concerns **reading** objects and data **consistency**.

### Example from e-commerce

When more than one thread is trying to change the state of an object at the very same moment, there is always a problem.
A simple example of understocking or inadequate inventory values - a common issue in poorly managed online stores - 
called **a lost update** by computer scientists.
Forgive the lack of a design-pattern approach.

### What is a lock?

A lock (a monitor) is a token. Only one thread a time can have the token.
But when a thread acquires a lock, **it does not mean it has exclusive access to the locked object**. 
It only says: I need access to this ```synchronized``` code. I want to modify the object 
and make it temporarily inconsistent using ```synchronized``` block of code.

Briefly, an acquired lock does not prevent from accessing the object.
It only stops other threads from getting the same lock / monitor / token.

After the operation, the object backs to consistent state and the thread releases the lock.

**Important**: locks are coming into play only when given code is marked as ```synchronized```.

**Important**: if there exists another thread accessing the same object, but through a non-synchronized method,
there is a possibility of concurrent modification / reading inconsistent state.

### Optimistic vs pessimistic lock

Both are ways to prevent **a lost update** to happen.

**Optimistic lock**: checks whether a value to be updated has not been changed since last read.

**Pessimistic lock**: explicitly forces other threads to wait until an update is done.




