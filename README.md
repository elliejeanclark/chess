# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## Server Design
Link to Sequence Diagram for Service Design. [Sequence Diagram](https://sequencediagram.org/index.html#initialData=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)

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
