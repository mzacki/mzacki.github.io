---
layout: single
title: "Class internals: hacking Java bytecode"
date:   2023-02-21 09:53
description: Hacking Java bytecode and hex editing.

categories:
- Java, JVM, security

tags:
- Java, JVM, bytecode, hex, security, Python

---

Read first: **[Deep dive into class internals: viewing bytecode, hex dump and first look at the ClassFile structure.](/class-internals)**

### Class hacking use case

Suppose there is a Java library with closed implementation enclosed in a private method.
This library is imported into a project that is currently under development.

```java
public class Lib {

    public static void main(String[] args) {
        System.out.println(implementationDetails());
    }

    private static String implementationDetails() {
        return "The method is closed.";
    }

}
```
Suppose there is a need for some change within the ```implementationDetails()``` method. The method behaviour should be different, at least for some time (during development or for testing).
Extension of imported```Lib``` class and writing own implementation is futile as private method overriding is forbidden. Let's assume there is no usual workaround (like forking the library repo,
cloning and changing local version. We have the compiled file and nothing else. There must be a way of changing access modifier or even tampering with the body of the method.

### Java bytecode editing

As previously explained, command-line ```javap``` tool only shows the content of a compiled class. **[It reveals bytecode and metadata](/class-internals/#disassemble-class-file)** without edit option. 

IntelliJ does not allow editing of compiled class. And it shows Java code, not bytecode, using default **FernFlower** decompiler.

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/lib.png"  alt="IntelliJ class viewer" title="View .class in IntelliJ">
</div>
<br>

**IntelliJ tool: bytecode viewer** - no edit option:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/bytecode_viewer.png"  alt="IntelliJ bytecode viewer" title="View bytecode in IntelliJ">
</div>
<br>

There are multiple, unofficial plugins for IntelliJ. I tried the most recommended, and it failed a few times, so I gave up and uninstalled.
And probably it is not quite a good idea to plug something "unofficial" into IDE used for developing production code, I would say.
Then I found **[Recaf](https://github.com/Col-E/Recaf)**: better option, open-source code. It's a simple, external Java application with plesant GUI and several features.

Now, we can use editor in decompiler mode and assembler tool to change access modifer or method body to reqiured extend:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/recaf_edited_class.png"  alt="Recaf bytecode viewer" title="View bytecode in Recaf">
</div>
<br>

Save and export command applies changes that are mirrored in decompiled ```.class``` view in IntelliJ:

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class Lib {
    public Lib() {
    }

    public static void main(String[] var0) {
        System.out.println(implementationDetails());
    }

    public static String implementationDetails() {
        return "The method is now open.";
    }
}
```

and when the class file is run:

```shell
$ java Lib
The method is now open.
```

This way is enough at a basic level.

### Java hex editing

Simple decompilation view, even with Recaf assembler tool, does not reveal all the details of ```.class``` file.
On the other hand, ```javap``` shows more interesting things: the bytecode and metadata without editing possibility:

```shell
$ javap -v -c Lib.class
Classfile /home/kotfot/IdeaProjects/blog/assets/java/Lib.class
  Last modified Feb 20, 2023; size 620 bytes
  SHA-256 checksum ad5de624d6828ca76eeb8b9bd62e0d52090bf5c7905ea4f4fa3dadeb303e2ab9
  Compiled from "Lib.java"
public class Lib
  minor version: 0
  major version: 61
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #14                         // Lib
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 3, attributes: 1
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
  #13 = Methodref          #14.#15        // Lib.implementationDetails:()Ljava/lang/String;
  #14 = Class              #16            // Lib
  #15 = NameAndType        #17:#18        // implementationDetails:()Ljava/lang/String;
  #16 = Utf8               Lib
  #17 = Utf8               implementationDetails
  #18 = Utf8               ()Ljava/lang/String;
  #19 = Methodref          #20.#21        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #20 = Class              #22            // java/io/PrintStream
  #21 = NameAndType        #23:#24        // println:(Ljava/lang/String;)V
  #22 = Utf8               java/io/PrintStream
  #23 = Utf8               println
  #24 = Utf8               (Ljava/lang/String;)V
  #25 = String             #26            // The method now open.
  #26 = Utf8               The method now open.
  #27 = Utf8               Code
  #28 = Utf8               LineNumberTable
  #29 = Utf8               LocalVariableTable
  #30 = Utf8               this
  #31 = Utf8               LLib;
  #32 = Utf8               main
  #33 = Utf8               ([Ljava/lang/String;)V
  #34 = Utf8               var0
  #35 = Utf8               [Ljava/lang/String;
  #36 = Utf8               SourceFile
  #37 = Utf8               Lib.java
{
  public Lib();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   LLib;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: invokestatic  #13                 // Method implementationDetails:()Ljava/lang/String;
         6: invokevirtual #19                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         9: return
      LineNumberTable:
        line 5: 0
        line 6: 9
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  var0   [Ljava/lang/String;

  public static java.lang.String implementationDetails();
    descriptor: ()Ljava/lang/String;
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=0, args_size=0
         0: ldc           #25                 // String The method now open.
         2: areturn
      LineNumberTable:
        line 9: 0
}
SourceFile: "Lib.java"
```

Finally,```xxd``` makes a dump to hexadecimal code. There are ways to [edit the hex from command line](https://www.baeldung.com/linux/edit-binary-files).
**Recaf** also gives this opportunity:

<div style="text-align: center;" class="image-medium">
<img src="/assets/images/recaf_hex.png"  alt="Recaf hex viewer" title="View hex in Recaf">
</div>
<br>

The hex view consists on three parts: **offset** on the left is not a part of the bytecode (it belongs to the viewing application). 
In the centre, there is the bytecode itself (as hexadecimal code). On the left, **ASCII transcription** of textual layer (with dots when the layer could not be rendered).

### Hex dump as Java stream

```shell
xxd -p Lib.class >> hex.txt
```


```shell
jshell> String hex = "/home/IdeaProjects/blog/assets/java/hex.txt"
hex ==> "/home/IdeaProjects/blog/assets/java/hex.txt"

jshell> Stream<String> stream = Files.lines(Paths.get(hex))
stream ==> java.util.stream.ReferencePipeline$Head@2d363fb3

jshell> stream.forEach(s->System.out.println(s)) 
# or as one String: System.out.println(stream.collect(Collectors.joining()))
cafebabe0000003d00260a000200030700040c000500060100106a617661
2f6c616e672f4f626a6563740100063c696e69743e010003282956090008
000907000a0c000b000c0100106a6176612f6c616e672f53797374656d01
00036f75740100154c6a6176612f696f2f5072696e7453747265616d3b0a
000e000f0700100c001100120100034c6962010015696d706c656d656e74
6174696f6e44657461696c7301001428294c6a6176612f6c616e672f5374
72696e673b0a001400150700160c001700180100136a6176612f696f2f50
72696e7453747265616d0100077072696e746c6e010015284c6a6176612f
6c616e672f537472696e673b295608001a010014546865206d6574686f64
206e6f77206f70656e2e010004436f646501000f4c696e654e756d626572
5461626c650100124c6f63616c5661726961626c655461626c6501000474
6869730100054c4c69623b0100046d61696e010016285b4c6a6176612f6c
616e672f537472696e673b2956010004766172300100135b4c6a6176612f
6c616e672f537472696e673b01000a536f7572636546696c650100084c69
622e6a6176610021000e00020000000000030001000500060001001b0000
002f00010001000000052ab70001b100000002001c000000060001000000
03001d0000000c000100000005001e001f00000009002000210001001b00
000038000200010000000ab20007b8000db60013b100000002001c000000
0a00020000000500090006001d0000000c00010000000a00220023000000
09001100120001001b0000001b00010000000000031219b000000001001c
0000000600010000000900010024000000020025
```
or InputStream from byte array:

```shell
InputStream is = new ByteArrayInputStream(Files.readAllBytes(Paths.get(hex)))
```

Slightly reworked (but still working) example of parsing hex file into Python stream
[from Jared Folkins](https://www.jaredfolkins.com/hacking-java-bytecode-for-programmers-part1-the-birds-and-the-bees-of-hex-editing/)

```python
import os

_directory = "/path/to/dir"
_file = "Lib.class"
if os.path.exists(_directory):
    with open(_directory + _file, "rb") as f:
        stream = f.read()
        f.close()
        print(stream.hex())
```

### Breaking Java bytecode in hex editor

Every Java ```.class``` file must start with hex magic number **CAFEBABE** in the bytecode.
Let's change magic number ```CAFEBABE``` to ```CAFE DEAD``` using any hex editor and see what happens.

```shell
$ java Lib
Error: LinkageError occurred while loading main class Lib
	java.lang.ClassFormatError: Incompatible magic value 3405700781 in class file Lib
```

Now, changing Java version encoded in the ClassFile to any unsupported one should break it.
Following [this guide showing how to find Java version in hex dump](https://www.baeldung.com/java-find-class-version) it appears as:

```text
cafebabe0000003d
```
After changing ```003d``` to ```003e``` (or higher - not supported) we got:

```shell
$ java Lib
Error: LinkageError occurred while loading main class Lib
	java.lang.UnsupportedClassVersionError: Lib has been compiled by a more recent version of the Java Runtime (class file version 62.0), 
	this version of the Java Runtime only recognizes class file versions up to 61.0
```

To be continued.

**[Other tricks with ```.jar``` hacking](https://www.jaredfolkins.com/hacking-java-bytecode-for-programmers-part4-krakatau-and-the-case-of-the-integer-overflow/)**