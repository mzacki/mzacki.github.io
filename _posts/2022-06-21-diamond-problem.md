---
layout: post
title: "Diamond problem in Java"
date:   2022-06-21 21:15
description: Diamond inheritance problem in Java

categories:

- Java, programming

tags:

- Java, OOP, inheritance

---

### Default methods in Java

Since Java 8, interfaces can have so-called **default methods**, i.e. methods,
that already have an **implementation**.
If a class implements such interface, it does not need to provide its own implementation
of a default method. Of course, it is still possible to override inherited implementation
and to provide a custom body of the method.

### What is it for?

The reason behind default methods is **backward compatibility**.
If an updated version of an interface is released - with a new method that 
had not been present before the new release - old code could be broken.
Why? Suppose we have implemented the interface - so we have implemented its
method (or methods) accordingly - like we normally have to do in Java, by the book
(preferably **Horstmann** or **Eckel**).
Unless it was **a marker interface**, without methods at all. But still.
And now, a new version of this interface appears, with a new method, not implemented in our code.
Our project reimports dependencies and rebuilds... or not, because we are suddenly facing
a case of an unimplemented interface method.

### Backward compatibility

Hence, the idea of default implemented methods in interfaces. No need to bother about them anymore.
Their goal was to upgrade the core Java Collections libraries, and introduce methods that
made use of **lambda expressions**. They may appear in new releases, but they are "plug and play". We can think of custom implementation, but
without stress. Almost.

### Diamonds are forever

So what is the problem? Is there an issue or not? It might be.
As we know, so-called **multiple inheritance** is not possible and not allowed in Java.
**Single inheritance** is a must.
Meaning: a class can only extend one **parent class**, no more. What about interfaces?
A class is allowed to implement multiple interfaces, because before Java 8 all their methods
shoud have had a custom implementation provided by a programmer. 
So it was up to a developer to ensure that this kind of multiple inheritance would not create issues, like **colliding methods**.
But with default methods, we are not obliged to implement them.
And what if we implemented - for example - two interfaces and each of them has similar default method, with
same name and parameters? They are clashing. Our class simply does not know which of the two methods should be called.

### Diamond inheritance is a problem

This problem has to be resolved. How?
- the implementing class must override the clashing method and provide a custom implementation - 
then it would be clear which method to call and what is the meaning;
- or instead of providing own implementation, the implementing class can clearly call one of the interfaces and methods
with the keyword **super** (indication in the body of the problematic method), so that we know what method should be called.


