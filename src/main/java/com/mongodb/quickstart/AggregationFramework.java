package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Accumulators.push;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class AggregationFramework {

    public static void main(String[] args) {
        String connectionString = System.getProperty("mongodb.uri");
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase db = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> zips = db.getCollection("zips");
            MongoCollection<Document> posts = db.getCollection("posts");
            threeMostPopulatedCitiesInTexas(zips);
            threeMostPopularTags(posts);
        }
    }

    /**
     * find the 3 most densely populated cities in Texas.
     * @param zips sample_training.zips collection from the MongoDB Sample Dataset in MongoDB Atlas.
     */
    private static void threeMostPopulatedCitiesInTexas(MongoCollection<Document> zips) {
        Bson match = match(eq("state", "TX"));
        Bson group = group("$city", sum("totalPop", "$pop"));
        Bson project = project(fields(excludeId(), include("totalPop"), computed("city", "$_id")));
        Bson sort = sort(descending("totalPop"));
        Bson limit = limit(3);

        List<Document> results = zips.aggregate(Arrays.asList(match, group, project, sort, limit))
                                     .into(new ArrayList<>());
        System.out.println("==> 3 most densely populated cities in Texas");
        results.forEach(printDocuments());
    }

    /**
     * find the 3 most popular tags and their post titles
     * @param posts sample_training.posts collection from the MongoDB Sample Dataset in MongoDB Atlas.
     */
    private static void threeMostPopularTags(MongoCollection<Document> posts) {
        Bson unwind = unwind("$tags");
        Bson group = group("$tags", sum("count", 1L), push("titles", "$title"));
        Bson sort = sort(descending("count"));
        Bson limit = limit(3);
        Bson project = project(fields(excludeId(), computed("tag", "$_id"), include("count", "titles")));

        List<Document> results = posts.aggregate(Arrays.asList(unwind, group, sort, limit, project)).into(new ArrayList<>());
        System.out.println("==> 3 most popular tags and their posts titles");
        results.forEach(printDocuments());
    }

    private static Consumer<Document> printDocuments() {
        return doc -> System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build()));
    }
}
