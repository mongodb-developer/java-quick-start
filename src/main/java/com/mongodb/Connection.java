package com.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.diagnostics.logging.Loggers;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {

    public static void main(String[] args) {
        Logger.getLogger(Loggers.PREFIX).setLevel(Level.WARNING);
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            databases.forEach(db -> System.out.println(db.toJson()));
        }
    }
}
