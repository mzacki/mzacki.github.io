---
layout: single
title:
date:   2023-02-11 10:20
description:

categories:
- 

tags:
- 

---

Classes:

- for every class in ```.java``` file (named **compilation unit** or **translation unit**), a separate ```.class``` file is created during compilation
- compiled ```.class``` files are packaged and compressed into a Java ARchive (JAR) file (using Java’s jar archiver)
- The Java interpreter is responsible for finding, loading, and interpreting the files
- ```CLASSPATH``` is the environment variable where the interpreter starts to look for the files
- ```CLASSPATH``` contains one or more directories that are used as roots in a search for the files, then it navigates through packages
- it replaces each dot with a slash to generate a path name off of the CLASSPATH root, through packages
- public class must have the same name as containing file
- only one class in a file can be public
- a top-level class cannot be private nor protected, because it does not have an entry point 
- only nested and inner classes can be private or protected


Fields, methods and classes should be as private as possible. Use default access modifier for top-level classes. 
For most of the cases, it is possible: developers create public classes by force of a habit.  Reasons:
- maintaining order and security
- the principle of least privilege (PoLP)
- backward compatibility of implementation or library
- separation: the things that change from the things that should not be changed (called **implementation hiding** or **encapsulation**)