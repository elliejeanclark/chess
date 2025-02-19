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
An inner class doesn't have a name and does not require a new call because it has a new keyword attached to it.

## Collections
Essentially the library of data structures that are included in the Java Library. A collection is a set of values.
There are things like lists and sets and queues, and subsets of those like sortedsets and deque etc.

### Data Structures
These collections have methods that you can use that are in the collection interface. Each collection interface
then inherits from the collection interface and add their own required methods to each data type. 

Most of these collections have iterator methods that you can use to iterate through the library.

These collections can **only** store objects, and not primitive data types. 

### List
They have an order to them. A first element, second, and so on. 
They are accessed by an index. 

Array list uses an array internally to store the list

Linked list uses a doubly-linked list implementation

### Set
A collection that contains no duplicates. It has add, contains, and remove methods.

#### Hash Set
Hash table implementation.
Unordered. You need good hash methods or these will be slow. 

#### Tree Set
Comes out in sorted order. You need to implement comparable on objects if you are going to use a tree set. 

#### Linked Hash Set
Uses the hash table and something else that I missed.

### Queue
A collection designed for holding elements prior to processing. Has add method, peek(look at 
next value before remove), and remove. 

- Array Deque
  - FIFO, resizable array implementation.
- LinkedList
  - FIFO, linked list implementation.
- PriorityQueue
  - Priority queue, binary heap implementation. 

### Deque
A queue that supports efficient insertion and removal at both ends. 
addFirst, addLast, peekFirst, peekLast, removeFirst, removeLast

- ArrayDeque
  - resizable array implementation
- LinkedList
  - linked list implementation

### Stack
Java's stack class is deprecated. It has been phased out because there is a new and improved way to do something.
Use Deque instead.

### Map
A collection that maps keys to values. Called dictionaries in other languages. Keys must be unique. There is no
uniqueness guarantee on the map.
put, get, contains, remove, keySet, values, entrySet.

- HashMap
  - Hash table implementation
- TreeMap
  - bst implementation
- LinkedHashMap
  - Hash table and linked list implementation

### Iterable Interface
All collections, but not maps, implement the iterable interface

### Hash, Equals, Comparison
You have to think about what kind of objects you are putting in the collection, and what kind of equality you want
to implement for the class. There is value equality and identity equality. 

#### Hashing-Based Collections
If you are using any of the collections implemented with hash tables, you may need to override the hashCode method.
By default, the Object.hashCode method simply returns the object's address. If you want a hash function to be based
on value rather than identity, override the hashCode method.

**RULE**: If equals is based on identity, so should hashCode be. If equals is based on a value, so should hashCode be,
and equals and hashCode should use hte same fields in their equality and hashCode calculations.

#### Sorted Collections
AKA TreeSet(BST), TreeMap (BST), PriorityQueue(binary heap)

The elements of a sorted collection must be sortable. This means that we must be able to compare any two objects and
determine their relationship. 

Comparable and Comparator for tree-based collections. 

## Generics
Generic classes have their own parameters that are passed into the pair when a new instance of that class is created. 

You can now use var syntax to instantiate a generic class 

### Inheriting from a generic class
Specify types in the extends clause. It then fills in the type parameters from its super class. You can also pass in 
generic types from a generic subclass. 

### Generic Interfaces
Example:
```java
public interface Function<T, R> {
    R apply(T param);
}
```

### Generic Type Wildcards
Wildcards with special syntax can be used to expand the acceptable types. 
ie
```java
public class GenericClassExample<T> {
    public void method1(List<? super T> param) {}
    public T method2() {return null;}
}
```
In this example, method1 accepts a list of whatever T is, or any of T's super classes. Replacing super with extends
results in the method accepting a list of T's or any of T's subclasses

## Lambdas
A lambda is an anonymous function, AKA a function that is not bound to a name. This enables you to pass functions around
as pieces of data. For example you could create a queue of functions that you want to call at a specific point in your 
code. Lambdas are all about deferred initiation. 

Example: 
```java

public static void main(String[] args) {
    String [] values = {"this", "is", "a", "test", "but", "only", "a", "test"};
    Arrays.sort(values, new StringLengthComperator());
    //#2 Anonymous inner class
    Arrays.sort(values, new Comperator<String>() {
        @Override
        public int compare(String first, String second) {
            return first.length() - second.length();
        }
    });
  
    //#3 with lambda
    Arrays.sort(values, (String first, String Second) -> { return first.length() - second.length(); });
    
    //#4 with concise lambda
    Arrays.sort(values, (first, second) -> first.length() - second.length());
}

private static class StringLengthComperator implements Coomperator<String> {
    @Override
    public int compare(String first, String second) {
        return first.length() - second.length();
    }
}
```

### Method References
Simplified syntax for lambda expressions is that simply call an existing method.

The lambda expression simply calls an existing method, passing it's parameter to the method so that it can be 
replaced with a method reference.

A double colon indicates a method reference, instead of a method call. A parameter list is not needed or allowed for a 
method reference and there is no -> operator. 

Method references can be used for static method, instance method, and constructor invocations. 

## IO
At the lowest level, Java represents I/O with a data abstraction known as a stream. A stream derives its meaning from 
a stream of liquid where you are either consuming the fluid from the outlet of the stream, or you are channeling liquid
into the stream for later consumption. Streams can flow forever or dry up. 

The two base classes for dealing with streams in Java are InputStream, and OutputStream. You read data from an InputStream
and you write data to an OutputStream. Both of these classes are abstract and require some subclasses in order to use their
functionality. For example, you can use a FileInputStream to read bytes of data from a file. Likewise you can use a
FileOutputStream to write data to a file. 

### Reader and Writer
Working with streams is fine for bytes, but for higher level object you need Reader and Writer classes. 

### Scanner
Scanner classes take the low level one byte/character at a time advantage of streams and readers and writers and then
can parse into different words as defined by a regular expression. By default, it parses based on any whitespace. 
Scanners can read lots of different data types like ints, bytes, booleans, and all out of a text scanner and do the
heavy lifting there for you.

## JSON & Serialization
A standardized format for sharing data. They contain start and end tags that have key value pairs to represent data.
"JavaScript Object Notation"

### xml
You can add attributes to the start tags, as well as nest attributes inside of the tags. Usually nested elements are more
complex objects. 

### JSON Data Types
- Strings
- Number
- Boolean
- Array
  - Can be mixed types
- Object
- Null

Objects have a bunch of key value pairs inside of curly braces. By nesting arrays inside arrays, and objects inside of objects,
you can build up a representation of pretty much whatever you want. You can think of JSON as essentially a tree.

### Parsing

#### DOM Parsers
Convert JSON text to an in-memory tree data structure. The tree is called the DOM or the "document object model"
after running the parser to create a DOM, traverse the DOM to extract the data you want.
Not great for huge data files because it will run out of memory after taking a really long time. 

#### Stream Parsers
Tokenizers that return one token at a time from the Json data file.
Last option that you would go to. It is low level. This is better for those reaaaaaly big files so that you can start and stop 
and process data a little at a time. Streaming parsing is also good if you are looking for one very specific and small thing
out of a file. 

#### Serializers/Deserializers
Use a library to convert from JSON to Java Objects (and vise versa)
Gson and Jackson are both popular libraries. 

### Serializing
Turning data into an array of characters and bytes so that it can be passed or taken from a database.
Essentially making the big json object into a big string.
Pretty printing makes the data that is returned really human-readable and indented and so on and so forth. 
Serialization converts each object recursively into a json string.

### Deserialize
This is just the opposite of serialization, it takes a json string, and expands it into a full Json object so that it 
can be read by a human and more operations can be done with the JSON. 

## Design Principles
The bigger a program becomes, the more important it is to organize it well. You will want to find ways to identify 
actors, objects, and interactions necessary to represent the application's domain. 

Focus on the following high level goals:
- It does what the customer wants it to do
- It is easy to understand, debug, and maintain
- It is extensible to required changes. 

### Design is inherently iterative
- design, implement, test, design, implement, test...
- Feedback loop from the implementation back into design provides valuable knowledge.
- Designing everything before beginning implementation doesn't work. 
- Beginning implementation without doing any design also doesn't work.
- The appropriate balance is achieved by interleaving design and implementation activities in relatively short iterations. 

### Abstraction
- Each class ha sa carefully designed public interface that defines how the rest of the system interacts with it. 
- A client can invoke operations on an object without understanding how it works internally.
- This is a powerful technique for reducing the cognitive burden of building complex systems. 

#### Building abstract classes
- Classes often model complex, real-world objects.
- You can't fully represent the thing you are abstracting, so you make domain appropriate decisions 
about what to represent in methods and variables. 
  - Would you use a heart-rate or fingerprint in a person class?
  - Probably not, but what if you are building a system for a heart surgeon or for the FBI?

### Naming
- Selecting good names for things is critical.
- Clearly relay function or purpose.
- Class and variable names are usually nouns.
- Method names are usually verbs. They could also be named the thing that they are returning in some cases.

### Single-Responsibility Principle
Each class/method/function should have one well-defined concept. All features on that class should be highly related 
to that concept.

### Decomposition
Divide large problems in to smaller sub-problems. Process of discovering the abstraction part.
Levels of decomposition
- System
- Subsystem
- Packages
- Classes
- Methods

### Good Algorithm & Data Structure Selection
Pick the right algorithm and data structure for the problem. No amount of decomposition or abstraction will hide
a fundamentally flawed selection of algorithm or data structure.

### Low Coupling
The less the classes know about each other the better. Minimize the number of other classes a class interacts 
with or knows about. Low coupling reduces ripple effects when a class changes.
Classes should hide or "encapsulate" information and methods that other classes do not need to know about. 

### Avoid Code Duplication
- So many copies to maintain.
- Bugs are duplicated. 
- Makes program longer, decreasing maintainability.
Solutions
- Factor Common code into a separate method or class
- Shared code might be placed in a common superclass.

## HTTP

### Client Connecting to Server:
In order to connect, both the client machine and the server machine must have IP addresses. 
The client program must know the server's IP address in order to connect. 
IP addresses are hard to work with and remember, so we normally specify a server's ip address using a "domain name"
The client uses the "domain name service" DNS to connect. 

Because the server machine will probably be running multiple programs, you need more than just an IP address to connect. 
Each server program communicates on a particular "port" number. A port number is an unsigned integer (1-65535)
The client can then connect to the machine with the IP address, and then the program with the port number. 

### URL
URL references many things: protocol, domain name, port number, and path. 

### Requests
Requests have a method (i.e. request type), the url path, and an optional http version.
Headers have information that is passed to the server which gives more details about the request you are making.
You can specify what to accept, what compressed data you can accept, what kind of browser can make this request, and more.

### Responses
Responses have the http version, a status code (what type of response you are getting) and a reason phrase.
The headers specify information about the content. Such as type, length, date info, and more. 
After an empty line, we then have the response body which is what is returned from your request. 

## cURL
Client for URLs. A tool you can use for debugging http requests. It is a command line tool. 

# Servers - Phase 3

## Creating a server

### Using Lambda Expressions
```java
import spark.Spark;

public class SimpleHelloBYUServer {
    public static void main(String[] args) {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
    }
}
```
The lambda function has a request, and a response. 

### Method Reference
```java
import spark.Spark;
import spark.Request;
import spark.Resonse;

public class SimpleHelloBYUServer {
    public static void main(String[] args) {
        Spark.get("/hello", SimpleHelloBYUServer::handleHello);
    }
    
    private static Object handleHello(Request req, Response res) {
        return "Hello BYU!";
    }
}
```

### Route Class
```java
import spark.Spark;
import spark.Request;
import spark.Resonse;
import spark.Route;

public class SimpleHelloBYUServer {
    public static void main(String[] args) {
        Spark.get("/hello", new HelloHandler());
    }
}

class HelloHandler implements Route {
    
    public Object handle(Request req, Response res) {
        return "Hello BYU!";
    }
}
```

## Specifying port
args[0] is the first argument given not the name as in other c-based languages. 
```java
import spark.Spark;

public class HelloBYUServer {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Spark.port(port);

            createRoutes();

            Spark.awaitInitialization();
            System.out.println("Listening on port " + port);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    private static void createRoutes() {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
    }
}
```

## Spark Routes
Routes are matched in the order they are defined. The first route that matches the request is invoked. There are 
get, post, put and delete routes.

### Named Parameters
Users can pass in parameters in the url. For example:

```spark
get("/hello/:name", (request, response) -> { 
  return "Hello: " + request.params(":name"); 
});
```

### Splat Parameters
You can also get wildcard parameters in a url, it will return an array of the parameters passed in. 

### Request and Response methods

#### Request
- body() - retrieve the request body
- headers() - retrieve all headers as a set of strings
- header("...") - retrieve the specified header

#### Response
- body(“…”) – set the response body (i.e. “Hello” sets the response body to “Hello”)
- status(404) – sets the status code to 404 (not found)

## Response example
```java
import spark.Spark;

public class HelloBYUServer {
    public static void main(String[] args) {
        try {
            //...
            createRoutes();
            //...
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }
    private static void createRoutes() {
        Spark.get("/hello", (req, res) -> {
            res.status(200);
            res.type("text/plain");
            res.header("CS240", "Awesome!");
            res.body("Hello BYU!");
            return res.body();
        });
    }
}
```

## Serving Static Files
You tell spark where your website is. ```Spark.staticFiles.location("/public");```

## Overriding the default not found page
```Spark.notFound("<html><body>My custom 404 page</body></html>");```

## Filters
A filter is just a http handler that you can put in front of the other ones to have it essentially restrict
which handlers can run.
```Spark
before((request, response) -> { 
  boolean authenticated; 

  // ... check if authenticated 

  if (!authenticated) { 
    halt(401, "You are not welcome here"); 
  } 
});
```
Optionally, you can pass in a pattern to restrict the routes to which they are applied. 
There are also after filters. You can have multiple before and/or after filters, which are executed in the order presented.

# Handlers
What do web api handlers do?
Some steps:
- If a web api requires an auth token, the handler can validate the auth token
- Deserialize JSON request body to Java request object
- Call service class to perform the requested function, passing it the java request object
- Receive Java response object from service
- Serialize Java response object to JSON
- Send HTTP response back to client with appropriate status code and response body.

# Data Access Classes
Methods will be CRUD methods. (Create, Read, Update, Delete). 

# Unit Testing
Individual parts should be verified before being integrated with other parts. Whenever you add a new class or
function test to make sure they do the right thing. 
We always want to automate test cases so that we can run the test cases over and over again when we edit and rewrite code.
Just because something works now doesn't mean it will work in the future.

## The testing Pyramid
Unit tests lead to integration tests, lead to end-to-end tests.

## What unit tests do
- Create objects, call methods, and verify that the returned results are correct. 
- Actual results vs. Expected results. 
- Unit tests should be automated to run multiple times. 
- Notifies you when changes have introduced bugs, and helps to avoid destabilizing the system.

## Test Driver Program
- The tests are run by a "test driver", which is a program that just runs all the unit test cases. 
- It must be easy to add new tests to the test driver
- After running the test cases, the test driver either tells you that everything worked, or gives you a list of tests
that failed.

## Rules of thumb
- Fast, we want to run them often. 
- Cohesive, each should test one specific thing and have a good name that describes what it tests.
- Independent, they shouldn't have side effects that impact the execution of other test cases. 
- Unique, each unit test should test something different.

## The JUnit Testing Framework
For every class in your program, you have a corresponding test class. 
To make a test for a class, you right-click on the class name, click go to, and then click the test. If you have one already
it will pop up, otherwise you will have an option to create a new one.
Stating ```Assertions.assertEquals(thing one, thing two)``` is how you check if something is equal, aka what your code returns is
what you actually wanted it to return. 

### imports
You will need to create a dependency for JUnit for the project. 
```java
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
```

### Tags
Adding the @Test tag to a method will tell JUnit that this method is a test. 
@BeforeEach - before every single test case
@BeforeAll - once before all the test cases you told to run
@AfterAll
@AfterEach
@Timeout(5) -- fails if the code takes longer than (5) seconds to run. 

### Setup
Most test cases have similar set up, so you can add the @BeforeEach methods which will run before any test that you run.
This is often how you initialize the values that you are expecting. 

## Code Coverage
How do you know if your tests are doing a good job of completely testing your code. 
- Line coverage: lines executed by tests/total lines
- Statement coverage: same as line coverage if every statement is on it's own line. 
- Branch coverage: Statement coverage + a measure of how many unique code branches were executed. 
  - If an if statement with no else, do tests exercise the cases where the if statement is both true and false?
  - Statement coverage wouldn't detect whether the false case is covered.
- Function coverage: covering each function. 

If you "run all tests with coverage" in intellij, it will measure how completely your test cases measure your code.
If after this there are lines in yellow or red, then the lines haven't been tested. 