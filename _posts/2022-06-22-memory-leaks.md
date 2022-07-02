---
layout: post
title: Memory leaks in Java
date:   2022-06-22 13:53
description: Memory leaks in Java

categories:

- Java, computer science

tags:

- Java, memory leak, GC, garbage collection

---

### Memory management in Java

So it is often said that we don't have to bother about memory managment in Java,
because there exist **garbage collection process** that cares about memory.
It is a wrong assumption.

**The garbage collector** is a mechanism in Java that handles automated memory management.
GC was first introduced in **Lisp** (*nota bene*, List is also known as first **functional language**).
GC is also used, *inter alia*, in Haskell, Ruby, Julia. On the other hand, languages like C or C++ require
programmers to explicitly manage the memory. We can compare GC to automatic transmission gearbox in motor vehicles,
contrary to manual transmission when there is no GC and memory management has to be done "by hand".

So even if we have this AT in our car, it doesn't mean we can forget about it. It still has to be handled properly, used by the book and
serviced from time to time. Same thing for memory in Java. Despite garbage collecting, a memory leak can still happen.

### What is a memory leak?

Memory leak means that memory has been allocated, but never reclaimed. Memory
leak can happen in following, exemplary circumstances:
- memory is no longer needed for a program, but it is not released
- an object is stored in memory and this object cannot be accessed
- a program, garbage collector or a programmer fails to free memory occupied by objects
that have become unreachable, and it leads to memory exhaustion.