
---
layout: single
title: "Pitfalls of polymorphism"
date:   2023-02-11 10:20
description:

categories:
- 

tags:
- 

---

### Polymorphism and binding

Polymorphism is one of OOP paradigms and presumably most complex of all of them. The others are inheritance, data abstraction and encapsulation.
Polymorphism is also called dynamic binding, late binding or run-time binding.

### Compile-time binding example: constants

The opposite of run-time binding is compile-time binding.

Constants are values marked with ```final```. Once initialized, their value never changes.
There are two types of constants:

- compile-time constant never changes

- a value initialized in runtime that should not be changed.

**Compile-time constant** allows the compiler to include its value into any calculations in which it’s used. The calculation can be performed at compile
time, eliminating some run-time overhead. Such value will not change in runtime. It must be a primitive, marked with the final keyword. The value
of such constant must be explicitly given (i.e. initialized) in the very moment of a declaration of this constant. Once initialized, their value never changes.
This is compile-time binding, a.k.a. early binding or static binding.

### But what is binding, after all?

Constants are bound in the compile-time. Method calls can also be bound. But what is binding, after all?
Binding is a proces of connecting a method call to a method body. Early binding (or compile-time binding) happens before program is run.
Consider following example based on Bruce Eckel's Thinking in Java 4th edition:

```java

interface Instrument {

    void play();
}

class Violin extends Instrument {

    @Override
    public void play() {
        // implementation
    }
}

class Piano extends Instrument {

    @Override
    public void play() {
        // implementation
    }
}
```
And now, the plot twist. In this case:

```java
class Orchestra {

    void tune(Instrument instrument) {
            instrument.play();
    }
}
```
The compiler does not know which method should be invoked, because actual method argument will be passed into the method only in runtime.
Here, runtime binding (a.k.a. dynamic or late binding) comes into play. Runtime binding is binding that takes places in runtime. 

All methods in Java use runtime binding, unless they are static, final or private (private means they are also implicitly final). So most of the methods in Java are bound
**polymorphically**.

Making a method final not only prevents from overriding this method, but also disables possibility of runtime binding, making the code slightly more efficient.

### Pitfall: “overriding” private methods

Interesting thread from Bruce Eckel's book *Thinking in Java 4th Edition*. I refactored the code to increase readibility.

Super class:

```java
class Parent {

    private void method() {
        System.out.println("Private method from Parent (superclass)");
    }
    
}
```
Subclass inheriting from Parent:

```java
class SubClass extends Parent {

    private void method() {
        System.out.println("Private method from SubClass");
    }

}
```

Which method will be called? What type of binding would be applied?

Hint: there is no ```@Override``` here.

We would expect subclass' method to be invoked. Nevertheless:

```java
class Main {
    
    public static void main(String[] args) {
        Parent parent = new SubClass(); // upcasting
        parent.method(); // prints: "private method from Parent (superclass)", so Parent method is invoked, not SubClass method
        // there is no runtime binding here, Parent method is private, hence final, prevents overriding
        // Parent "parent" refrence calls method() from Parent, although it is SubClass() object.
    }
    
}
```

> Such pseudo-overriding private methods generates no compiler
warnings. It doesn’t do what we might expect. 
> First of all, @Override should be used.
Then, we should use a different name of this method in subclass without any confusing style.

### Pitfall: fields and static methods

Direct access to a field is resolved by compilator (not polymorphically):

```java
class Parent {

    String field = "parent";

    String getField() {
        return field;
    }

    static String staticGetter() {
        return "staticGetter() parent";
    }

    String dynamicGetter() {
        return "dynamicGetter() parent";
    }

    public static void main(String[] args) {
        System.out.println(parent.field); // parent - from Parent (compile-type binding)
        System.out.println(parent.getField()); // subclass - from SubClass (polymorphism, runtime binding)
        System.out.println(subClass.field); // subclass
        System.out.println(subClass.getField()); // subclass
        System.out.println(subClass.getParentField()); // parent

        System.out.println(parent.dynamicGetter()); // dynamicGetter() subclass
        System.out.println(parent.staticGetter()); // staticGetter() parent
        System.out.println(subClass.dynamicGetter()); // dynamicGetter() subclass
        System.out.println(subClass.staticGetter()); // staticGetter() subclass
    }

}
```

```java
class SubClass extends Parent {

    String field = "subclass";

    String getField() {
        return this.field;
    }

    String getParentField() {
        return super.field;
    }

    static String staticGetter() {
        return "staticGetter() subclass";
    }

    String dynamicGetter() {
        return "dynamicGetter() subclass";
    }
    
}
```
Static methods also does not behave polymorphically. The reason is clear: static methods belong to a class itself, and not to its instance (individual object).
Constructors are not polymorphic. They’re implicitly static methods.
