---
layout: post
title:  "Compiler vs interpreter"
date:   2022-06-20 19:31:11 +0200
description: Compiler and interpreter - differences in simple words
categories: computer_science
tags:

- compiler
- interpreter
- computer science
- IT
- programming
- Java
- bytecode
- machine code

---
What is the difference between a compiler and an interpreter? 

In simple terms, both are used to translate code to another programming language but being on lower-level.
Meaning: the target languange is often low-level languange. Example: compiling Java class (.java file) 
to bytecode (.class file) or compiling / interpreting bytecode to machine code.

But what are the differences?

**COMPILATION**
- a program is being compiled as a whole
- compiler is creating an executable module that can be run multiple times
- it often concerns areas of code that are planned to be frequently executed
- compilation takes place before program runs (it creates an artifact that is run only after that)
- hence we often speak about "compile time"
- compilation may include code-optimization process
- owing to that, the executable module (result of the compilation) can use less system resources
- takes more time

**INTERPRETATION**
- limited to some part of a program - the part that is needed in a given moment
- takes less time
- may be reproduced frequently, but it is rather related to one-timer code
- does not include code optimization - as optimization is a time-consuming process
- takes place when program / script is running (during runtime)

I found this easy-to-understand explanation in a 20-years-old book: "Informatyka. Wirtualna podróż w świat systemów i sieci komputerowych" (2022)
by Mirosław Hajder, Heorhii Loutski and Wiesław Stręciwilk. It is still an up-to-date knowledge source for computer programmers.
Some chapters are very easy, some others contain a lot of mathematics, formulas and computer science theory. Overall, a nice introduction into several areas of computer science.
