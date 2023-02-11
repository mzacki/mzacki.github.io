---
layout: single
title: "The Groovy Truth"
date:   2022-08-10 12:45
description: Groovy truth explained.

categories:

- Groovy

tags:

- Groovy, Spock

---

### What is Groovy Truth?

Groovy evaluates variables of various types (including reference types) as boolean values (similar to Python, but contrary to Java).
In Java, only variable of primitive boolean type or Boolean object wrapper can be evaluated to boolean value.
E.g. null is always null - lack of object. It cannot be translated to any value.
On the contrary, in Groovy, null is considered false and a reference to non-null object is treated as boolean true.
According to [the documentation](https://groovy-lang.org/semantics.html#the-groovy-truth), Groovy decides whether an expression is true or false by applying certain rules.

### Groovy Truth test cases - Spock specification

```groovy
import spock.lang.Specification
import java.util.regex.Pattern

class GroovyTruth extends Specification {

    def "assert true"() {
        expect:
        assert true
    }

    def "assert !false"() {
        expect:
        assert !false // redunant, simplification possible
    }

    def "assert true || false"() {
        expect:
        assert true || false // redunant, simplification possible
    }

    def "assert true && !false"() {
        expect:
        assert true && !false // redundant, simplification possible
    }

    def "non-empty string is true"() {
        given:
        String name = "non-empty"
        expect:
        name // no need to use assert keyword
    }

    def "empty string is false"() {
        given:
        String name = ""
        expect:
        !name
    }

    def "valid (non null) reference is true"() {
        given:
        def ref = new GroovyExample()
        expect:
        assert ref
    }

    def "null object is false"() {
        given:
        def ref
        // or
        // GroovyExample ref = new GroovyExample()
        expect:
        assert !ref
    }

    def "zero is false"() {
        given:
        def zero = 0
        expect:
        assert !zero
    }

    def "non-zero is true"() {
        given:
        def nonzero = 99
        expect:
        assert nonzero
    }

    def "empty collection is false"() {
        given:
        def empty = []
        expect:
        assert !empty
    }

    def "non-empty collection is true"() {
        given:
        def nonempty = ["item"]
        expect:
        assert nonempty
    }

    def "regex is true if matches at least once"() {
        given:
        Pattern myRegex = ~/needle/
        expect:
        assert myRegex.matcher("needle in haystack")
        assert !myRegex.matcher("Wrong haystack")
    }

    def "equality and identity"() {
        given:
        List<String> list1 = ["A", "B", "C"]
        List<String> list2 = ["A", "B", "C"]
        expect:
        // equality (not identity / not reference) test
        list1 == list2 // equivalent of equals in Java
        // identity (reference) test
        !list1.is(list2) // equivalent of == in Java
    }

}

```

