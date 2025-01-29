## Instance vs Static

Instance variables
- Each object (instanct) gets its own copy of all the instance variables defined in its class.
- Most variables should be instance variables.
- IE. Allows two date objects to represent different dates
  Static variables
- Static variables are associated with the class not with instances.
- Use in special cases where you won't create instances of a class, or all instances should share the same values.
- Static methods can only access static variables.
- IE. If the variables of a date class were static, all dates in a program would represent the same date.

## Records

Data classes (basic classes) all have similar parts and pieces.
Records are a shorter way of generating these data classes. Paramaters are the different variables that are needed
to make an instance of this object. Methods can also be put into your records. Records are immutable, so "changing"
a pet just makes a new instance of the pet with a different name in the example below.

```java
public record PetRecord(int id, String name, String type) {
    PetRecord rename(String newName) {
        new PetRecord((id), newName, type);
    }
}
```

## Inheritance

A code reuse mechanism. Subclasses inherit from Superclasses. the Subclass will gain all the variables and methods of the
superclass, and can then add things that are specific to the subclass. When you are initializing the variables in a subclass,
you also need to intialize all the variables on the super class.

public Student(String name, int age, YearInSchool year, float gpa) {
super(name, age);
setYear(year);
setGpa(gpa);
}

You can then override superclass functions if you want them to do different things.

Protected methods can be accessed by other classes if they are in the same package, or if they are a subclass.
More restricted than public, but more open than private.

Abstract classes and methods are not fully defined, they are just supposed to be the building blocks for subclasses.

Final methods and variables CANNOT be overridden.

## Interface

An interface is kinda like a class, but all it has is abstract method declarations. So by default a method will be public
and abstract, so you don't need to declare the method as public and abstract.
Interfaces can implement any number of interfaces and still subclass some other class.

Creating an interface:
```java
public interface Moveable {
    void go();
}
```

Extending an interface
Interfaces can inherit from each other

Implementing an interface
```java
public class Person implements Moveable {
    public void go() {
        //Code to make the person go.
    }
}
```

## Classes

Determining an object's class
- o.getClass()
- if (o.getClass() == King.class) {...}
- instance of
- if (o instanceof ChessPiece) {...}

## Copying Objects

There are two ways to copy an object

- Shallow Copy
    - Copy the variable values from the original object to the copy. AKA primatives and object references. Just copying the
      top level information of an object.
- Deep Copy
    - Copy the object and all the objects it references, recursively

Writing classes that support copying
- "clone" method on each class
- Copy Constructors

## Exceptions and Exception Handling

Overview
- Abnormal conditions that can occur in a java class
- May be, but are not necessarily errors
- Allow you to separate normal processing logic from abnormal processing logic
- Represented by classes and objects in java
- You throw objects.

Throwable Objects

Anything that inherits from throwable (errors and exceptions) can be thrown. There are many prebuilt exception classes.

Try/Catch Blocks

Code you are trying to run goes in the try block, and the catch block is where you list the different kinds of exceptions
that may happen, and if it does the catch block lists code to execute if the error happens.
If you are not going to catch and handle the error in the method, you can declare in the method that this throws a certain
exception type, and then eventually resolve it later.

Finally Blocks

Code that happens regardless of if the exception was thrown and caught or not. This is good for closing files, because it
will still close the file regardless of whether something failed while the file was open or not.

## Inner Classes

### Static inner classes
If an inner class is not static, then the nested class will have access to all the variables and methods of that it is
nested inside. So if you want an inner class to have access to the other variable and methods of the outer class
you don't make it static.

### Non-Static inner classes
You declare inner classes just like you would declare any other class, but you don't have to put it into another file.

### Local inner classes
When you move a class inside a method so that you don't need to pass in additional parameters when creating an
instance of a that inner class.

### Anonymous Inner Classes
An inner class doesn't have a name 