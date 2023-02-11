---
layout: single
title: "Java: Abstract class vs interface"
date:   2023-01-01 09:19
description: Comparing abstract classes and interfaces. Similarities and differences.

categories:
- Java, OOP

tags:
- Java, OOP, mnemonics

---

In Java, there are five (5) reference types: classes, interfaces, enums, annotations and arrays. 

More often than not, comparing various types of classes
with interfaces may be confusing for a begginer Java programmer. 
But even if you have spent multiple years in IT industry, answering the question:

**What are differences and similarities between abstract classes and interfaces in Java?**

when woken up in the middle of a night could not be
a straightforward task. With passing years of coding, programmers become increasingly focused on practice, completion of down-to-earth
assignements and project management. Language theory becomes more and more abstract over time.

An **abstract class** has some interesting features:

- it's impossible to create an instance of an abstract class
- it can contain abstract methods which must be implemented in non-abstract subclasses inheriting from this abstract class
- it also can contain fields and non-abstract methods (including static)
- an abstract class can extend another class, including abstract
- it can contain a constructor 

**Can abstract class be instantiated?** 

Nope. But an abstract class can be invoked if contains a main method.
Also, an abstract class may contain both abstract and nonabstract methods.

To sum up, an abstract class has two main differences from regular (concrete) classes: no instances and abstract methods.

**Can abstract class implement interface?**

Yes, but no need to provide the implementation of interface method!

To add further complexity to the matter, since Java 8, an interface can have **default and static** methods that contain an implementation. 
It makes interface more similar to an abstract class.
In case of **a functional interface**, which may contain a single abstract method (SAM type) only, static and default methods are allowed as well.


| Abstract class                                                                                           | Interface                                                                                                    |
|----------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| an **abstract class** can have **abstract** and **non-abstract** instance methods                        | an **interface** can have abstract, default and static (since Java 8) or privte instance methods             |
| an **abstract class** can **extend** another *abstract* or *regular class*<br/>using keyword **extends** | an **interface** can only **extend** another *interface*<br/>using keyword **extends** (not implements!)     |
| hence, an **abstract class** can only extend (inherit from) only one class                               | hence, **an interface cannot extend** (inherit from) another class (no matter what type)                     |
| an **abstract class** can implement (inherit from) multiple interfaces using keyword **implements**      | an **interface** can also inherit from multiple interfaces, but it uses keyword **extends** for this purpose |
| an abstract class can have final, non-final, static, non-static variables (regular fields)               | an interface has only static final variables                                                                 |
| an abstract class can provide an implementation of an interface                                          | an interface can't provide an implementation of an abstract class                                            |
| an abstract class can have a constructor                                                                 | a constructor not allowed                                                                                    |
| in an abstract class, the keyword abstract is mandatory to declare a method as an abstract               | in an interface, the keyword abstract is redundant                                                           |


### Postscriptum I

Speaking of the Java reference types, there is a useful mnemonic to remember:

**C**lass - C

**I**nterface - I

**A**nnotation - A 

**E**num - E

**A**rray - A


It's CIA - EA, like Central Inteligence Agency - Electronic Arts (legendary game studio).

### Postcriptum II

Speaking of arrays, let's recall static utils methods for arrays. There are eight of them:

- equals(), to compare two arrays for equality
- deepEquals() for multidimensional arrays
- fill()
- sort()
- binarySearch( ) to find an element in a sorted array only
- toString( )
- deepToString() for multidimensional arrays
- hashCode( )

Another important method used with arrays is: Arrays.asList( ) converting an array to a list.

And arrays use methods inherited from the class java.lang.Object:
clone , equals , finalize , getClass , hashCode , notify , notifyAll , toString.