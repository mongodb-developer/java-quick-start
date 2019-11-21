package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Delete {

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");

            System.out.println(gradesCollection.deleteMany(Filters.gte("student_id", 10000)));
        }
    }
}
