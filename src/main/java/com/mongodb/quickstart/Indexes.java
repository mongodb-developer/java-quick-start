package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Indexes {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_validator");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("student");

            /**
             * Example 1: Single field index on student_id in order 1 (Ascending order)
             */
            gradesCollection.createIndex(new Document("student_id", 1));

            /**
             * Example 2: Compound index on student_id and name of order 1 and -1 respectively
             */
            gradesCollection.createIndex(new Document("student_id", 1).append("name", -1));

            /**
             * Example 3: Multikey index on zip the scalar value of address field.
             * A scalar value refers to value that is neither an embedded document nor an array.
             */
            gradesCollection.createIndex(new Document("address.zip", 1));

            /**
             * Execution - Making use of index in the search; The winning plan in below query should include Index scan aka IXSCAN
             */
            System.out.println(gradesCollection.find(new Document("student_id", 11001)).explain());
        }
    }
}
