package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Update {

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");

            // update one document
            Bson filter = Filters.eq("student_id", 10000);
            Bson updateOperation = Updates.set("comment", "You should learn MongoDB!");
            UpdateResult updateResult = gradesCollection.updateOne(filter, updateOperation);
            System.out.println("\nUpdating the doc with {\"student_id\":10000}. Adding comment.");
            System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));
            System.out.println(updateResult);

            // upsert
            filter = Filters.eq("student_id", 10002);
            updateOperation = Updates.push("comments", "You will learn a lot if you read the MongoDB blog!");
            UpdateOptions options = new UpdateOptions().upsert(true);
            updateResult = gradesCollection.updateOne(filter, updateOperation, options);
            System.out.println("\nUpsert document with {\"student_id\":10002} because it doesn't exist yet.");
            System.out.println(updateResult);
            System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));

            // update many documents
            filter = Filters.eq("student_id", 10001);
            updateResult = gradesCollection.updateMany(filter, updateOperation);
            System.out.println("\nUpdating all the documents with {\"student_id\":10001}.");
            System.out.println(updateResult);

            // findOneAndUpdate
            filter = Filters.eq("student_id", 10000);
            Bson updateOperation1 = Updates.inc("x", 10); // increment x by 10. As x doesn't exist yet, x=10.
            Bson updateOperation2 = Updates.rename("x", "newX"); // rename variable "x" in "newX".
            Bson updateOperation3 = Updates.mul("newX", 2); // multiply "newX" by 2.
            Bson updateOperation4 = Updates.addToSet("comments",
                                                     "This comment is uniq"); // creating an array with a comment.
            Bson updateOperation5 = Updates.addToSet("comments",
                                                     "This comment is uniq"); // array behave as a set so adding the same comment has no effect.
            List<Bson> updates = Arrays.asList(updateOperation1, updateOperation2, updateOperation3, updateOperation4,
                                               updateOperation5);
            // findOneAndUpdate returns the old version of the document before the update.
            Document oldVersion = gradesCollection.findOneAndUpdate(filter, updates);
            System.out.println("\nFindOneAndUpdate operation. Printing the old version by default:");
            System.out.println(oldVersion.toJson(prettyPrint));

            // but I can also request the new version
            filter = Filters.eq("student_id", 10002);
            FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
            Document newVersion = gradesCollection.findOneAndUpdate(filter, updates, optionAfter);
            System.out.println("\nFindOneAndUpdate operation. But we can also ask for the new version of the doc:");
            System.out.println(newVersion.toJson(prettyPrint));
        }
    }
}
