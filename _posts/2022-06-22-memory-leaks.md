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
serviced from time to time. Same thing for memory in Java. Despite garbage collecting a memory leak can still happen. 
Its probability is lower but still present.

### What is a memory leak?

A memory leak means that memory has been allocated, but never reclaimed. Memory
leak can happen in following, exemplary circumstances:
- memory is no longer needed for a program, but it is not released
- an object is stored in memory and this object cannot be accessed
- a program, garbage collector or a programmer fails to free memory occupied by objects
that have become unreachable, and it leads to memory exhaustion.

### Memory leak in Java - example

The code below is inspired by an example from a good book on Java that I read and recommend -
Java in a Nutshell. A Desktop Quick Reference 7th Edition by Ben Evans and David Flanagan.
You can find the code on my GitHub repo [Java Patterns](https://github.com/mzacki/java-patterns)
According to the authors, another potential source of a memory leak can be ```HashMap``` if it lives longer than objects
contained into it. Then it keeps the references.

```java
package edu.ant.patterns.basic.internals;

/**
 * Example of potential memory leak in Java
 * From nice book
 * Java in a Nutshell. A Desktop Quick Reference 7th Edition
 * by Ben Evans and David Flanagan
 * */

public class MemoryLeak {

    private static int computeSth(int[] array) {
        // stub method, let's return any int
        return array.length;
    }

    private static void doSomething(int result) {
        // stub methd
    }

    public static void main(String args[]) {
        // declaration of big, memory consuming array
        // initially its empty and fill in with zeroes by default
        int[] maxSizeArray = new int[2_147_483_647];
        // max size of array depends on JVM
        // circa  2^31 – 1 (max int size)
        int result = computeSth(maxSizeArray);

        /**
         * At this point, the array is no longer needed, and it could be garbage collected from now on.
         * But maxSizeArray is a local variable, and it keeps the reference to the array object
         * until main method returns.
         * But the main method never returns because it has void keyword.
         * */
        maxSizeArray = null; // explicit cleaning of the reference, otherwise no garbage collection

        // Everlasting loop handling the result derived from the array
        for(;;) doSomething(result);
    }

}

```

