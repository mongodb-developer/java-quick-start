package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.quickstart.models.Grade;
import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.changestream.FullDocument.UPDATE_LOOKUP;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ChangeStreams {

    public static void main(String[] args) {
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
            List<Bson> pipeline;

            // Only uncomment one example at a time. Follow instructions for each individually then kill all remaining processes.

            /** => Example 1: print all the write operations.
             *  => Start "ChangeStreams" then "MappingPOJOs" to see some change events.
             */
            grades.watch().forEach(printEvent());

            /** => Example 2: print only insert and delete operations.
             *  => Start "ChangeStreams" then "MappingPOJOs" to see some change events.
             */
            // pipeline = singletonList(match(in("operationType", asList("insert", "delete"))));
            // grades.watch(pipeline).forEach(printEvent());

            /** => Example 3: print only updates without fullDocument.
             *  => Start "ChangeStreams" then "Update" to see some change events (start "Create" before if not done earlier).
             */
            // pipeline = singletonList(match(eq("operationType", "update")));
            // grades.watch(pipeline).forEach(printEvent());

            /** => Example 4: print only updates with fullDocument.
             *  => Start "ChangeStreams" then "Update" to see some change events.
             */
            // pipeline = singletonList(match(eq("operationType", "update")));
            // grades.watch(pipeline).fullDocument(UPDATE_LOOKUP).forEach(printEvent());

            /**
             * => Example 5: iterating using a cursor and a while loop + remembering a resumeToken then restart the Change Streams.
             * => Start "ChangeStreams" then "Update" to see some change events.
             */
            // exampleWithResumeToken(grades);
        }
    }

    private static void exampleWithResumeToken(MongoCollection<Grade> grades) {
        List<Bson> pipeline = singletonList(match(eq("operationType", "update")));
        ChangeStreamIterable<Grade> changeStream = grades.watch(pipeline);
        MongoChangeStreamCursor<ChangeStreamDocument<Grade>> cursor = changeStream.cursor();
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
        assert resumeToken != null;
        grades.watch(pipeline).resumeAfter(resumeToken).forEach(printEvent());
    }

    private static Consumer<ChangeStreamDocument<Grade>> printEvent() {
        return System.out::println;
    }
}
