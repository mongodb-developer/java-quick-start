# Java Quick Start Project

This repository contains code samples for the Quick Start blog post series.

You can read more about the Quick Start series on the [MongoDB Developer Hub](https://developer.mongodb.com/learn/).

- [MongoDB & Java - CRUD Operations Tutorial](https://developer.mongodb.com/quickstart/java-setup-crud-operations)
- [Java - Mapping POJOs](https://developer.mongodb.com/quickstart/java-mapping-pojos)
- [Java - Aggregation Pipeline](https://developer.mongodb.com/quickstart/java-aggregation-pipeline)
- [Java - Change Streams](https://developer.mongodb.com/quickstart/java-change-streams)
- [Java - Client Side Field Level Encryption](https://developer.mongodb.com/quickstart/java-client-side-field-level-encryption/)

# MongoDB Cluster

To get started with MongoDB Atlas and get a free cluster read [this blog post](https://developer.mongodb.com/quickstart/free-atlas-cluster).

# Requirements

- Java JDK 8 to 15.
- Maven 3.6.3.

# Command lines

- Compile: 

```sh
mvn clean compile
```

- Run the `HelloMongoDB` class: 

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.HelloMongoDB" -Dexec.cleanupDaemonThreads=false
```
- Run the `Connection` class: 

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Connection" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `Create` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Create" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `Read` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Read" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `Update` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Update" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `Delete` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Delete" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `MappingPOJO` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.MappingPOJO" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `AggregationFramework` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.AggregationFramework" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `ChangeStreams` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.ChangeStreams" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```

- Run the `ClientSideFieldLevelEncryption` class:
```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.csfle.ClientSideFieldLevelEncryption" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority" -Dexec.cleanupDaemonThreads=false
```


# Author

Maxime Beugnet <maxime@mongodb.com>
