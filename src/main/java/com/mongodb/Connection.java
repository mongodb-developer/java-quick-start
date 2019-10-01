package com.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.diagnostics.logging.Loggers;
import org.bson.Document;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {

    private static final String connectionString = "mongodb+srv://<USERNAME>:<PASSWORD>@cluster0-abcde.mongodb.net/test?w=majority";

    public static void main(String[] args) {
        Logger.getLogger(Loggers.PREFIX).setLevel(Level.WARNING);
        MongoClient mongoClient = MongoClients.create(connectionString);
        ArrayList<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
        databases.forEach(System.out::println);
        mongoClient.close();
    }
}
