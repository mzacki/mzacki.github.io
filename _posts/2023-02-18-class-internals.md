---
layout: single
title: "Class internals: introduction"
date:   2023-02-18 10:20
description: Introduction to Java .class file internals basics.

categories:

- Java, JVM, security

tags:

- Java, JVM, bytecode, hex, security

---

### Class basics

Interested in learning JVM internals? It's good to start with Java classes. First of all, let's recall basic information:

- for every class in ```.java``` file (named **compilation unit** or **translation unit**), a separate ```.class``` file
  is created during compilation
- compiled ```.class``` files are packaged and compressed into a Java ARchive (JAR) file (using Java’s jar archiver)
- The Java interpreter is responsible for finding, loading, and interpreting the files
- ```CLASSPATH``` is the environment variable where the interpreter starts to look for the files
- ```CLASSPATH``` contains one or more directories that are used as roots in a search for the files, then it navigates
  through packages
- it replaces each dot with a slash to generate a path name off of the ```CLASSPATH``` root, through packages
- public class must have the same name as containing file
- only one class in a file can be public
- a top-level class cannot be private nor protected, because it does not have an entry point
- only nested and inner classes can be private or protected

> Fields, methods and classes should be as private as possible. Use default access modifier for top-level classes.
> For most of the cases, it is possible. Developers usually create public classes by force of a habit.
> So why we should increase privacy level? Reasons:

- maintaining order and security: it is well known what can be changed (implemented differently) by overriding and what
  should be kept unchanged
- **the principle of least privilege** (PoLP): well known security rule - there is no need to give more access by
  default than it is required
- backward compatibility of implementation (e.g. library implementation): without proper access to a parent class,
  developer cannot override inherited method, so when the parent class is changed,
  it does not affect the implementation
- separation: the things that change from the things that should not be changed (called **implementation hiding** or *
  *encapsulation**)

All Java code should be contained in a class within ```.java``` file. The code written in a ```.java``` file is compiled
before being executed.
Then, as already mentionned above, compilation process creates a separate ```.class``` file for each Java class in the
source file.

[How to compile and run Java code from command line in multiple ways under Java 17 and older versions](/jshell/#running-java-code-before-jshell-and-jdk9)

But what is inside ```.class``` file?

### Disassemble class file

[The Java Class File Disassembler](https://docs.oracle.com/en/java/javase/17/docs/specs/man/javap.html) command reveals
the content of ```.class``` file:

```shell
javap -v
```

Javap Disassembler may be configured as IntelliJ External Tool:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/javap_external_tool.png"  alt="javap IntelliJ external tool" title="Configure javap as external tool in IntelliJ">
</div>
<br>

First of all, let's compile and then disassemble sample Java class:

```java
public class HulloDarling {
    public static void main(String[] args) {
        System.out.println("Hullo, darling!");
    }
}
```

```shell
javac HulloDarling.java
javap -v HulloDarling
Classfile /home/user/IdeaProjects/blog/assets/java/HulloDarling.class
  Last modified Feb 18, 2023; size 433 bytes
  SHA-256 checksum c91dcb4215967f8c8b267030352ef3a87b8514b465baaea25e4288299b460d91
  Compiled from "HulloDarling.java"
public class HulloDarling
  minor version: 0
  major version: 61
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #21                         // HulloDarling
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 2, attributes: 1
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // java/lang/System.out:Ljava/io/PrintStream;
   #8 = Class              #10            // java/lang/System
   #9 = NameAndType        #11:#12        // out:Ljava/io/PrintStream;
  #10 = Utf8               java/lang/System
  #11 = Utf8               out
  #12 = Utf8               Ljava/io/PrintStream;
  #13 = String             #14            // Hullo, darling!
  #14 = Utf8               Hullo, darling!
  #15 = Methodref          #16.#17        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #16 = Class              #18            // java/io/PrintStream
  #17 = NameAndType        #19:#20        // println:(Ljava/lang/String;)V
  #18 = Utf8               java/io/PrintStream
  #19 = Utf8               println
  #20 = Utf8               (Ljava/lang/String;)V
  #21 = Class              #22            // HulloDarling
  #22 = Utf8               HulloDarling
  #23 = Utf8               Code
  #24 = Utf8               LineNumberTable
  #25 = Utf8               main
  #26 = Utf8               ([Ljava/lang/String;)V
  #27 = Utf8               SourceFile
  #28 = Utf8               HulloDarling.java
{
  public HulloDarling();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 1: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #13                 // String Hullo, darling!
         5: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 4: 0
        line 5: 8
}
SourceFile: "HulloDarling.java"
```

Now let's do a **hex dump** using Linux command-line hex dumper ```xxd```.

By the way - what is the difference between *command line* and *command-line*?

> Two words as a noun. Hyphenate as an adjective.
> [Microsoft Style Guide](https://learn.microsoft.com/en-us/style-guide/a-z-word-list-term-collections/c/command-line)

```shell
xxd HulloDarling.class
00000000: cafe babe 0000 003d 001d 0a00 0200 0307  .......=........
00000010: 0004 0c00 0500 0601 0010 6a61 7661 2f6c  ..........java/l
00000020: 616e 672f 4f62 6a65 6374 0100 063c 696e  ang/Object...<in
00000030: 6974 3e01 0003 2829 5609 0008 0009 0700  it>...()V.......
00000040: 0a0c 000b 000c 0100 106a 6176 612f 6c61  .........java/la
00000050: 6e67 2f53 7973 7465 6d01 0003 6f75 7401  ng/System...out.
00000060: 0015 4c6a 6176 612f 696f 2f50 7269 6e74  ..Ljava/io/Print
00000070: 5374 7265 616d 3b08 000e 0100 0f48 756c  Stream;......Hul
00000080: 6c6f 2c20 6461 726c 696e 6721 0a00 1000  lo, darling!....
00000090: 1107 0012 0c00 1300 1401 0013 6a61 7661  ............java
000000a0: 2f69 6f2f 5072 696e 7453 7472 6561 6d01  /io/PrintStream.
000000b0: 0007 7072 696e 746c 6e01 0015 284c 6a61  ..println...(Lja
000000c0: 7661 2f6c 616e 672f 5374 7269 6e67 3b29  va/lang/String;)
000000d0: 5607 0016 0100 0c48 756c 6c6f 4461 726c  V......HulloDarl
000000e0: 696e 6701 0004 436f 6465 0100 0f4c 696e  ing...Code...Lin
000000f0: 654e 756d 6265 7254 6162 6c65 0100 046d  eNumberTable...m
00000100: 6169 6e01 0016 285b 4c6a 6176 612f 6c61  ain...([Ljava/la
00000110: 6e67 2f53 7472 696e 673b 2956 0100 0a53  ng/String;)V...S
00000120: 6f75 7263 6546 696c 6501 0011 4875 6c6c  ourceFile...Hull
00000130: 6f44 6172 6c69 6e67 2e6a 6176 6100 2100  oDarling.java.!.
00000140: 1500 0200 0000 0000 0200 0100 0500 0600  ................
00000150: 0100 1700 0000 1d00 0100 0100 0000 052a  ...............*
00000160: b700 01b1 0000 0001 0018 0000 0006 0001  ................
00000170: 0000 0001 0009 0019 001a 0001 0017 0000  ................
00000180: 0025 0002 0001 0000 0009 b200 0712 0db6  .%..............
00000190: 000f b100 0000 0100 1800 0000 0a00 0200  ................
000001a0: 0000 0400 0800 0500 0100 1b00 0000 0200  ................
000001b0: 1c 
```

As we can see, the ```.class``` file contains **bytecode** and **metatada**.
Metadata (explained later) are contained in the first part of disassembled class and, respectively, in the first part of the hex dump.

The second part of the class - bytecode - reveals the structure and body of the class, e.g. access modifiers, methods, and so on. This information
is also encoded in the hex dump: line ```00000140``` and next.

But what is this **bytecode** after all?

### Java bytecode 

In short, [Java bytecode](https://en.wikipedia.org/wiki/Java_bytecode) means byte-structured instructions for JVM
contained in ```.class``` files. Java bytecode is generated by Java compiler.

Each [bytecode instruction](https://en.wikipedia.org/wiki/List_of_Java_bytecode_instructions) is composed of one byte
that represents the opcode, along with zero or more bytes for operands.

Such instruction can be depicted as textual mnemonic, binary or hex. For example ```ladd``` mnemonic is an eqivalent of
hex opcode 61 and means: add two arithmetic values of long type. The prefix ```l``` means long operand type.

For example, the bytecode shows the following instructions for the ```main()``` method in ```HulloDarling.class```:

```shell
0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #13                 // String Hullo, darling!
         5: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
```

By using the list of Java bytecode instructions it is possible to translate their mnemonics into hex values: b2, 12, b6, b1.
The values are visible in the hex dump - here marked with double asterisks:

```shell
00000180: 0025 0002 0001 0000 0009 **b2**00 07**12** 0d**b6**  .%..............
00000190: 000f **b1**00 0000 0100 1800 0000 0a00 0200  ................
```

[More on method invocation inside JVM.](https://www.lohika.com/methods-invocation-inside-of-a-java-virtual-machine)

### My Very Cute Animal Turns Savage In Full Moon Areas

How to interpret all this information: bytecode and
metada? [Oracle's JVM Specification Manual for JDK 17](https://docs.oracle.com/javase/specs/jvms/se17/html/index.html)
would be helpful.
Let's start from the structure.

#### The ClassFile structure

[The ClassFile structure](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.1) looks like:

```text
ClassFile {
    u4             magic;
    u2             minor_version;
    u2             major_version;
    u2             constant_pool_count;
    cp_info        constant_pool[constant_pool_count-1];
    u2             access_flags;
    u2             this_class;
    u2             super_class;
    u2             interfaces_count;
    u2             interfaces[interfaces_count];
    u2             fields_count;
    field_info     fields[fields_count];
    u2             methods_count;
    method_info    methods[methods_count];
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```

There are 10 basic sections:

- Magic number: well-known, legendary **0xCAFEBABE**
- Version of class file format: the minor and major versions of the class file
- Constant pool, meaning pool of constants for the class
- Access flags (access modifiers)
- This class: name of the current class
- Super class: name of the super class
- Interfaces
- Fields
- Methods
- Attributes: any attributes of the class (for example the name of the source file and the like)

Hence, a mnemonic for the ClassFile structure:

> My Very Cute Animal Turns Savage In Full Moon Areas

#### Magic number: 0xCAFEBABE

**A [magic number](https://en.wikipedia.org/wiki/Magic_number_(programming))** in programming language is a numeric
value that all of a sudden appears
litteraly out of nowhere. Its meaning is not obvious nor self-explanatory. It is considered to be an **antipattern** and
should be avoided.
Funny thing is, that in case of the ClassFile a magic number was used: 0xCAFEBABE - it can be seen at the beginning of
the **hex dump**:

```shell
00000000: cafe babe 0000 003d 001d 0a00 0200 0307  .......=........
```

According to Oracle:

> The magic item supplies the magic number identifying the class file format; it has the value 0xCAFEBABE.

Every [Java class file](https://en.wikipedia.org/wiki/Java_class_file) starts with the same 4 bytes - CAFEBABE in hex.
It is used to **identify a file as conforming to the class file format**. Why this
value? [James Gosling answers](http://radio-weblogs.com/0100490/2003/01/28.html):

> As far as I know, I'm the guilty party on this one. I was totally unaware of the NeXT connection.  
> The small number of interesting HEX words is probably the source of the match.
>
> As for the derivation of the use of CAFEBABE in Java, it's somewhat circuitous:
> We used to go to lunch at a place called St Michael's Alley. According to local legend, in the deep dark past, the
> Grateful Dead used to perform there before they made it big.
> It was a pretty funky place that was definitely a Grateful Dead Kinda Place.
> When Jerry died, they even put up a little Buddhist-esque shrine. When we used to go there, we referred to the place
> as Cafe Dead.
> Somewhere along the line it was noticed that this was a HEX number.
> I was re-vamping some file format code and needed a couple of magic numbers: one for the persistent object file, and
> one for classes.
> I used CAFEDEAD for the object file format, and in [grepping](https://en.wikipedia.org/wiki/Grep) for 4 character hex
> words that fit after "CAFE" (it seemed to be a good theme) I hit on BABE and decided to use it.
>
> At that time, it didn't seem terribly important or destined to go anywhere but the trash-can of history.
> So CAFEBABE became the class file format, and CAFEDEAD was the persistent object format.
> But the persistent object facility went away, and along with it went the use of CAFEDEAD - it was eventually replaced
> by RMI.

Now a small joke on numerology: 0xCAFEBABE is 3405691582 in decimal. The sum of all digits is 43. 43 is a prime number -
so it's enough to make any mathematician magically nervous.
And it is one more than
42 - [Ultimate Answer to the Life, the Universe, and Everything, that has interesting mathematical properties (after all)](https://www.scientificamerican.com/article/for-math-fans-a-hitchhikers-guide-to-the-number-42/)

#### Version

Version parameter describes Java version under which the bytecode has been generated.
[Oracle tutorial provides a table](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.1-200-B.2)
showing Java release, date of the release, major version number of the release,
and earlier versions supported by the release. If a JVM tries to run ```.class``` which have unsupported version
parameter inside, ```java.lang.UnsupportedClassVersionError```will be thrown.
For example, major version for Java 17 is 61 (0x3D in hex) and previously compiled bytecode dumped as hex includes this
number:

```shell
00000000: cafe babe 0000 003d 001d 0a00 0200 0307  .......=........
```

#### Constant pool

[Constant pool](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.4) contains information on all
string constants, including field names and method names, class and interface names, plus *other constants that are
referred to within the ClassFile structure and its substructures*.

```shell
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // java/lang/System.out:Ljava/io/PrintStream;
   #8 = Class              #10            // java/lang/System
   #9 = NameAndType        #11:#12        // out:Ljava/io/PrintStream;
  #10 = Utf8               java/lang/System
  #11 = Utf8               out
  #12 = Utf8               Ljava/io/PrintStream;
  #13 = String             #14            // Hullo, darling!
  #14 = Utf8               Hullo, darling!
  #15 = Methodref          #16.#17        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #16 = Class              #18            // java/io/PrintStream
  #17 = NameAndType        #19:#20        // println:(Ljava/lang/String;)V
  #18 = Utf8               java/io/PrintStream
  #19 = Utf8               println
  #20 = Utf8               (Ljava/lang/String;)V
  #21 = Class              #22            // HulloDarling
  #22 = Utf8               HulloDarling
  #23 = Utf8               Code
  #24 = Utf8               LineNumberTable
  #25 = Utf8               main
  #26 = Utf8               ([Ljava/lang/String;)V
  #27 = Utf8               SourceFile
  #28 = Utf8               HulloDarling.java
```

[Further details on Constant Pool](https://blogs.oracle.com/javamagazine/post/java-class-file-constant-pool)

#### Access flags

Values in this section denote access permissions to and properties of this class or interface:

```shell
flags: (0x0021) ACC_PUBLIC, ACC_SUPER
```

Full set of possible flags has been explained in [Oracle's documentation](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.1-200-E.1) - including a nice, clear table showing possible modifiers:
flag name, hex value, and it's meaning:

| Flag Name      | Value  | Interpretation                                                                    |
|----------------|--------|-----------------------------------------------------------------------------------|
| ACC_PUBLIC     | 0x0001 | Declared public; may be accessed from outside its package.                        |
| ACC_FINAL      | 0x0010 | Declared final; no subclasses allowed.                                            |
| ACC_SUPER      | 0x0020 | Treat superclass methods specially when invoked by the invokespecial instruction. |
| ACC_INTERFACE  | 0x0200 | Is an interface, not a class.                                                     |
| ACC_ABSTRACT   | 0x0400 | Declared abstract; must not be instantiated.                                      |
| ACC_SYNTHETIC  | 0x1000 | Declared synthetic; not present in the source code.                               |
| ACC_ANNOTATION | 0x2000 | Declared as an annotation interface.                                              |
| ACC_ENUM       | 0x4000 | Declared as an enum class.                                                        |
| ACC_MODULE     | 0x8000 | Is a module, not a class or interface.                                            |

It is worth noting that:

> The ACC_SUPER flag indicates which of two alternative semantics is to be expressed by [the invokespecial instruction](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.invokespecial) if it appears in this class or interface. 
> Compilers to the instruction set of the Java Virtual Machine should set the ACC_SUPER flag. 
> In Java SE 8 and above, the Java Virtual Machine considers the ACC_SUPER flag to be set in every class file, regardless of the actual value of the flag in the class file and the version of the class file.
>
> The ACC_SUPER flag exists for backward compatibility with code compiled by older compilers for the Java programming language. 
> Prior to JDK 1.0.2, the compiler generated access_flags in which the flag now representing ACC_SUPER had no assigned meaning, and Oracle's Java Virtual Machine implementation ignored the flag if it was set.

#### This class

**This class** value:

```shell
 this_class: #21                         // HulloDarling
```

contains reference to the constant pool information:

```shell
 #21 = Class              #22            // HulloDarling
```

According to [the guide](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.1-200-E.1), *the value of the ```this_class``` item must be a valid index into the constant_pool table. 
The constant_pool entry at that index must be a CONSTANT_Class_info structure representing the class or interface defined by this class file*.

#### Super class

Similarily, **super class** value:

```shell
super_class: #2                         // java/lang/Object
```

contains reference to the constant pool information in relevant record:

```shell
#2 = Class              #4             // java/lang/Object
```

Further explanation in [the manual](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.1-200-E.1).

#### Interfaces, Fields, Methods, Attributes

It's rather self-explanatory:

```shell
interfaces: 0, fields: 0, methods: 2, attributes: 1
```

It is a counter of the items like interfaces, fields, methods and attributes included into the class.

