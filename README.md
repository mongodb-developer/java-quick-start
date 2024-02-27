# Java Quick Start Project

This repository contains code samples for the Quick Start blog post series.

You can read more about the Quick Start series on the [MongoDB Developer Hub](https://www.mongodb.com/developer/).

- [MongoDB & Java - CRUD Operations Tutorial](https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/)
- [Java - Mapping POJOs](https://www.mongodb.com/developer/languages/java/java-mapping-pojos/)
- [Java - Aggregation Pipeline](https://www.mongodb.com/developer/languages/java/java-aggregation-pipeline/)
- [Java - Change Streams](https://www.mongodb.com/developer/languages/java/java-change-streams/)
- [Java - Client Side Field Level Encryption](https://www.mongodb.com/developer/languages/java/java-client-side-field-level-encryption/)
- [Java - Multi-Doc ACID Transactions](https://www.mongodb.com/developer/languages/java/java-multi-doc-acid-transactions/)

# MongoDB Cluster

To get started with MongoDB Atlas and get a free cluster read [this blog post](https://developer.mongodb.com/quickstart/free-atlas-cluster).

# Requirements

- Java 21
- Maven 3.8.7.

# Command lines

- Compile: 

```sh
mvn clean compile
```

- Run the `HelloMongoDB` class: 

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.HelloMongoDB"
```
- Run the `Connection` class: 

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Connection" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Create` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Create" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Read` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Read" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Update` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Update" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Delete` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Delete" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `MappingPOJO` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.MappingPOJO" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `AggregationFramework` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.AggregationFramework" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `ChangeStreams` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.ChangeStreams" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `ClientSideFieldLevelEncryption` class:
```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.csfle.ClientSideFieldLevelEncryption" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

### Transactions

Always start the `ChangeStreams` class in the `transactions` package first because it creates the `product` collection with the required JSON Schema. See the related blog post.

- Run the `ChangeStreams` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.transactions.ChangeStreams" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

- Run the `Transactions` class:

```sh
mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.transactions.Transactions" -Dmongodb.uri="mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority"
```

# Author

Maxime Beugnet
- maxime@mongodb.com
- MaBeuLux88 on [GitHub](https://github.com/mabeulux88)
- MaBeuLux88 in the [MongoDB Developer Community forum](https://www.mongodb.com/community/forums/u/MaBeuLux88/summary).
