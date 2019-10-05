# Java Quick Start Project

This repository contains code samples for the Quick Start blog post series.

You can read more about the Quick Start series on the [MongoDB blog Quick Start section](https://www.mongodb.com/blog/channel/quickstart).

# MongoDB Cluster

To get started with MongoDB Atlas and get a free cluster read [this blog post](https://www.mongodb.com/blog/post/quick-start-getting-your-free-mongodb-atlas-cluster).

# Requirements

- Java JDK 8 to 13.
- Maven 3.6.2.

# Command lines

- Compile: 

```sh
mvn clean compile
```

- Run the `HelloMongoDB` class: 

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.HelloMongoDB"
```
- Run the `Connection` class: 

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.Connection" -Dmongodb.uri=mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority
```

- Run the `Create` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.Create" -Dmongodb.uri=mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority
```

# Author

Maxime Beugnet <maxime@mongodb.com>
