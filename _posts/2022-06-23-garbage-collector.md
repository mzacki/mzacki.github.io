---
layout: post
title: Garbage collector
date:   2022-06-23 20:10
description: Garbage collector in Java

categories:

- Java
- JVM

tags:

- garbage collector
- JVM

---

### Garbage collector - important terms

**reachable object** - object with a reference pointing to it - currently in use; 
such object can be reached via a reference, starting from a local variable, through the stack trace of a thread (note that each thread
has its own stack but threads share the heap); reachable objects are said to be live - a term parallel to Go game and live stones
(when they are connected to a still living group of stones), contrary to a dead stone (separated, without such connection).

**stop-the-world (STW)** - the runtime finds a safepoint, when it can safely halt all threads within the application.
This point of safety could be for example just before start of a loop or before a method call - 
for all threads in the same time; only then GC can safely happen with no risk that
an object is removed / moved to another space of a heap when another thread is actually using it.

**weak generational hypothesis (WGH)** - an assumption that objects have - in general - a short life expectancy;
only a small part of objects is used in a program for longer (or very long) time; WHG can be depicted as kind of 
**long tail** phenomenon.

**parallel GC**  - uses multiple threads during garbage collection (involves STW), default for < JDK8 

**concurrent GC** - can run concurrently with application threads (more expensive in terms of CPU time, but becomes increasingly popular)

All GC and their strategies try to balance between throughput, latency and memory usage:
- **throughput** means workload being done by GC (the more the better)
- **latency** means duration of the pause in application's work (lower is better)
- **memory** (memory footprint) - how much extra memory is needed for GC to perform its tasks (the less the better)


### Garbage collectors & collecting strategies

- Mark-and-Sweep
- Stop-and-Copy
- G1
- Parallel
- ParallelOld
- ZGC
- Shenandoah
- Serial

### Heap areas and object generations

- Eden / Nursery space (young generation)
- Survivor space (young generation)

For young objects created just before garbage collection. Their instant evacuation to tenured space would be less effective,
because if they died afterwards, they would occupy place in tenured space until next full GC. 
That is why they have their own, dedicated space.

- Tenured space (old generation)

### Mark-and-Sweep

**How it works?**

**Mark-and-sweep** is a most popular algorithm in Hot-Spot JVM. Given **weak generational hypothesis (WGH)**, GC marks all object as dead by default.
There is a great probability that most of them is already dead. Then GC checks their references and re-marks live objects only. 
Finally, GC sweeps dead objects reclaiming memory space. 

**Evacuation process**

Initial approach would be to reclaim space of dead objects and move it to some kind of free space area (in memory).
But there is much more dead objects than live objects - hence a reverse approach would be better.
Once GC knows who is dead and who is not, it employs **evacuating collectors** to process live objects **evacuation**
to a different space where they will continue to live. Less work to do, because there is less live objects than dead objects.

**Compaction process**

Considered an alternative to **evacuation**. **Compacting collectors** do not move living objects to a different space,
but they are "compacted" to a single contiguous area within the memory area where the garbage collection process
is taking place. **Compaction** is a process similar to hard-disk defragmentation.

### G1 - The Garbage First collector

- balanced between throughput and latency
- generational GC
- region-based collector (**region** is an area of memory where all objects belong to the same memory pool, size: 1Mb to 32Mb)
- does **incremential compaction**
- is **evacuating collector**
- allows to set duration and frequency of the **pause** by ```-XX:MaxGCPauseMillis=200``` (200 is default)
- default since JDK9
- STW

### Parallel

- default in JDK8 and earlier
- generational GC
- focused on throughput (maximum work), minimal focus on latency (pauses)
- performs evacuation (copying)
- STW


### ParallelOld

- default for the old generation in JDK8
- concurrent, but not parallel GC
- uses mark-and-sweep strategy
- is not an evacuating collector
- applies compaction
- STW
- drawback: pause depends on the heap size (bigger heap = longer pause)

### Concurrent Mark-and-Sweep

- alternative collector for HotSpot
- used for the old generation when parallel collector cleans young generation
- for low-pause applications, of acceptable STW duration lower than a few millis
- for old generation only
- concurrent
- complex and rather difficult to handle

### ZGC

- since JDK15
- focus on latency
- non-generational

### Shenandoah

- since JDK12
- focus on latency
- non-generational

### Serial

- focus on memory and startup time
- single-threaded
- generational
- good for small, short-running apps

---

*Sources - basics and beyond:*

Thinking in Java 4th Edition, Bruce Eckel, Pearson, 2006

Java in a Nutshell. A Desktop Quick Reference 7th Edition, Ben Evans, David Flanagan, O'Reilly, 2018

Thomas Schatzl, [Java garbage collection: The 10-release evolution from JDK 8 to JDK 18](https://blogs.oracle.com/javamagazine/post/java-garbage-collectors-evolution)

