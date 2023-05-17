package com.mongodb.quickstart;

import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;

public class SchemaValidation {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://main_user:test123@analytics.fmxyq.mongodb.net/?retryWrites=true&w=majority")) {
            MongoDatabase validatorTestDB = mongoClient.getDatabase("sample_validator");

            // Create a validator specifying intended fields for your collection
            ValidationOptions collOptions = new ValidationOptions().validator(
                    Filters.and(
                            Filters.exists("name"),
                            Filters.exists("age"),
                            Filters.exists("grade")
                    ));

            // Create cluster with the validator
            validatorTestDB.createCollection("student",
                    new CreateCollectionOptions().validationOptions(collOptions));
            System.out.println("==> Created collection with validator");

            // Create collection object of the above created
            MongoCollection<Document> studentCol = validatorTestDB.getCollection("student");

            insertInvalidDocument(studentCol); // Inserting an invalid document; this is supposed to fail
            insertValidDocument(studentCol); // Inserting valid document; should return success
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserting an invalid document to show validator is in-place. This is supposed to fail.
     * @param studentCol sample_validator.student collection to add document.
     */
    public static void insertInvalidDocument(MongoCollection<Document> studentCol) {
        // Insert invalid document
        try {
            studentCol.insertOne(new Document("name", "John Doe").append("balance", 200));
            System.out.println("==> Inserted invalid document successfully");
        }
        catch (Exception e) {
            System.out.println("==> Failed due to :" + e.getMessage());
        }
    }

    /**
     * Inserting a valid document with the required fields as set in the validator; this will return success.
     * @param studentCol sample_validator.student collection to add document.
     */
    public static void insertValidDocument(MongoCollection<Document> studentCol) {
        // Insert valid document
        try {
            studentCol.insertOne(new Document("name", "John Doe").append("age", 10).append("grade", "A"));
            System.out.println("==> Inserted valid document successfully");
        } catch (Exception e) {
            System.out.println("==> Failed due to :" + e.getMessage());
        }
    }

}

