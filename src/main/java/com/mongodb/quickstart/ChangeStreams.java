package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.quickstart.models.Grade;
import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ChangeStreams {

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        ConnectionString connectionString = new ConnectionString(System.getProperty("mongodb.uri"));
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            MongoDatabase db = mongoClient.getDatabase("sample_training");
            MongoCollection<Grade> grades = db.getCollection("grades", Grade.class);

            // Only uncomment one example at a time. Follow instructions for each individually then kill all remaining processes.

            /** => Example 1: print all the write operations.
             *  => Start "ChangeStreams" then "MappingPOJOs" to see some change events.
             */
            grades.watch().forEach(print());

            /** => Example 2: print only insert and delete operations.
             *  => Start "ChangeStreams" then "MappingPOJOs" to see some change events.
             */
            // grades.watch(asList(match(in("operationType", asList("insert", "delete"))))).forEach(print());

            /** => Example 3: print only updates without fullDocument.
             *  => Start "ChangeStreams" then "Update" to see some change events (start "Create" before if not done earlier).
             */
            // grades.watch(asList(match(eq("operationType", "update")))).forEach(print());

            /** => Example 4: print only updates with fullDocument.
             *  => Start "ChangeStreams" then "Update" to see some change events.
             */
            // grades.watch(asList(match(eq("operationType", "update")))).fullDocument(UPDATE_LOOKUP).forEach(print());

            /**
             * => Example 5: iterating using a cursor and a while loop + remembering a resumeToken then restart the Change Streams.
             * => Start "ChangeStreams" then "Update" to see some change events.
             */
            // exampleWithResumeToken(grades);
        }
    }

    private static void exampleWithResumeToken(MongoCollection<Grade> grades) {
        MongoChangeStreamCursor<ChangeStreamDocument<Grade>> cursor = grades.watch(asList(match(eq("operationType", "update"))))
                                                                            .cursor();
        System.out.println("==> Going through the stream a first time & record a resumeToken");
        int indexOfOperationToRestartFrom = 5;
        int indexOfIncident = 8;
        int counter = 0;
        BsonDocument resumeToken = null;
        while (cursor.hasNext() && counter != indexOfIncident) {
            ChangeStreamDocument<Grade> event = cursor.next();
            if (indexOfOperationToRestartFrom == counter) {
                resumeToken = event.getResumeToken();
            }
            System.out.println(event);
            counter++;
        }
        System.out.println("==> Let's imagine something wrong happened and I need to restart my Change Stream.");
        System.out.println("==> Starting from resumeToken=" + resumeToken);
        grades.watch(asList(match(eq("operationType", "update")))).resumeAfter(resumeToken).forEach(print());
    }

    private static Consumer<ChangeStreamDocument<Grade>> print() {
        return System.out::println;
    }
}
