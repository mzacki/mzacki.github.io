---
layout: single
title: "Heisenbugs in Java"
date:   2023-02-13 05:58
description: Debugging Heisenbugs in Java and other creatures.

categories:

- Java

tags:

- Java, QA, Heisenbug

---

### Various kinds of bug creatures

| bug          | named after                 | reference to physics                                   | behaviour of the bug                                                                                                                                                                                                                                                                                                                                                                                                                              |
|--------------|-----------------------------|--------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Heisenbug    | Werner Heisenberg           | Heisenberg's uncertainty principle<br/>observer effect | changes behaviour, appears to be random, may disappear during debugging                                                                                                                                                                                                                                                                                                                                                                           |
| Bohrbug      | Niels Bohr                  | Bohr atom model                                        | good, old, solid bug, visible, easily detectable                                                                                                                                                                                                                                                                                                                                                                                                  |
| Mandelbug    | Benoît Mandelbrot           | Mandelbrot fractal                                     | so complex it defies repair, or makes its behavior appear chaotic or even non-deterministic<br/> exhibits fractal behavior (self-similarity) by revealing more bugs <br/> the deeper one goes into the code to fix it, the more bugs one find                                                                                                                                                                                                     |
| Hindenbug    | Hindenburg Zeppelin airship | Hindenburg disaster                                    | causes catastrophic impact                                                                                                                                                                                                                                                                                                                                                                                                                        |
| Schrödinbug  | Erwin Schrödinger           | Schrödinger's cat                                      | doesn't manifest until someone reading source or using the program in an unusual way notices that it never should have worked, at which point the program promptly stops working for everybody until fixed                                                                                                                                                                                                                                        |
| Higgs-bugson | Peter Higgs                 | the Higgs boson particle                               | bug that is predicted to exist based upon other observed conditions <br/>(most commonly, vaguely related log entries and anecdotal user reports) but is difficult, if not impossible, to artificially reproduce in a development or test environment.<br/>The term may also refer to a bug that is obvious in the code (mathematically proven), but which cannot be seen in execution (yet difficult or impossible to actually find in existence) |

More on [Heisenbug and others](https://en.wikipedia.org/wiki/Heisenbug#Related_terms)

Compare to [the Jargon File and The New Hacker's Dictionary](http://www.catb.org/jargon/)

### Heisenbug in detail

What are the symptoms of a Heisenbug? It is a bug that disappears, or seems to disappear during debugging. 
It may randomly alter its behavior, or it alters its behavior without understandable logic. It may appear and disappear during runtime, which makes it similar to Schrödinbug in some way.
The most important property of Heisenbug is that attempt of catching it impacts its behaviour or presence, which may disturb or even impede the debugging process.

The bug is named after physicist Werner Heisenberg, who introduced two famous terms: the uncertainty principle and the observer effect.
The former states that the more precisely the position of some particle is determined, the less precisely its momentum can be predicted from initial conditions, and vice versa.
In general, it is not possible to predict with arbitrary certainty the values of pairs of certain physical quantities that characterise a system to which quantum mechanics is applied. 
They cannot be determined simultaneously with any accuracy. Such pairs are, for example, 
the position and momentum of a particle, the energy and the time at which this energy was measured.

The uncertainty principle is often confused with the latter term - the observer effect - which states that the act of observing a system inevitably alters its state.

In case of Heisenbugs, both aspects apply. 

It is impossible, or at least extremely difficult to catch or observe a bug in predictable environment, but the bug appear when the circumstances are hard to reproduce, depict as a snapshot or mimicked in a sandbox.
Examples are multi-dependent architecture, concurrency, complex software, difference in computing power and memory between local workstation and server or test and production environment.

Another factor is developer's action. Debbuging may change the environment state, and that impacts the condition of appearance of a Heisenbug.

> The use of a debugger sometimes alters a program's operating environment significantly enough that buggy code, 
> such as that which relies on the values of uninitialized memory, behaves quite differently.
> 
> The Jargon File

Running a debugger or playing with the software may have subtle side effects, like changing memory addresses of variables or timing of its execution.
The latter is particularly important in multi-threading applications. An example of a time-sensitive bug is **race condition**. A program may be accidentally slowed down by the use of debugger
(running instructions one by one) or by adding additional log or statement.

### Where to look for Java Heisenbug?


- [x] **Timing:** execution time difference due to computing power or debugger run. Number of cores in processor.
- [x] **Threads:** hard to spot concurrency issues in the code base. Interaction between threads. Operating system may be potential reason of a Heisenbug, 
because concurrent execution depends on a given OS and runtime resources. So concurrent executions could be described as highly non-deterministic from programmer's standpoint.
- [x] **Memory:** change of variable addresses. Optimization during compilation. According to [Marcello La Rocca](https://dev.to/mlarocca/how-to-catch-your-heisenbug-33nh): 

> Running code compiled without optimization might also cause some variables to be moved from registers to RAM, 
> and this, in some languages/compilers, can affect the precision used for floating point comparisons.
> 

Note that native libraries can be infected by non-Java Heisenbugs.

- [x] **Debugger:** side effects or slowness of execution.
- [x] **Randomisation** and pseudo-randomisation.
- [x] **Charm of Java:** (un)predictability of finalizers, weak references, (un)predictable Garbage Collector behaviour, VM internals
- [x] **Caching** (even Spring caching)
- [x] **Hashing collisions**
- [x] **Object interning:** (mistake in using ```==``` instead of ```equals()``` for String, wrapper class or Object)
- [x] **Tests:** wrong test methodology or faulty design (e.g. overriding global setup, invalid use of ```static```, avoiding ```@DirtiesContext``` etc. for the sake of performance)


### How to catch Heisenbug (a.k.a. The Fugitive)?

- [x] narrow down area of searching to the smallest possible code fragment
- [x] log in details at every step of processing, forwarding, redirecting or transforming, extremely important in case of data pipelines
- [x] be sure if input data is really correct and invariant
- [x] use reverse debugging tools
- [x] [check other ideas](https://dev.to/mlarocca/how-to-catch-your-heisenbug-33nh) and [this thread at StackOverflow](https://stackoverflow.com/questions/34679813/likely-and-unlikely-causes-of-heisenbugs-in-java)

By the way, what is reverse debugging? Normally, a debugger embedded into IDE, like Intellij IDEA, cannot move backwards. Once a program failed, we cannot get back to check past behaviour nor state.
E.g. it's impossible to evaluate an expression two steps before, view recent variables assignments, and the like. Reverse debugger allows execution recording. For Java, it's [Chronon](https://chrononsystems.com/).