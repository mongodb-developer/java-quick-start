package com.mongodb.quickstart.transactions;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.quickstart.transactions.models.Cart;
import com.mongodb.quickstart.transactions.models.Product;
import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.inc;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Transactions {

    private static final BigDecimal BEER_PRICE = BigDecimal.valueOf(3);
    private static final String BEER_ID = "beer";
    private static final Bson filterId = eq("_id", BEER_ID);
    private static final Bson filterAlice = eq("_id", "Alice");
    private static final Bson matchBeer = elemMatch("items", eq("productId", "beer"));
    private static final Bson incrementTwoBeers = inc("items.$.quantity", 2);
    private static final Bson decrementTwoBeers = inc("stock", -2);
    private static MongoCollection<Cart> cartCollection;
    private static MongoCollection<Product> productCollection;

    public static void main(String[] args) {
        ConnectionString connectionString = new ConnectionString(System.getProperty("mongodb.uri"));
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();
        try (MongoClient client = MongoClients.create(clientSettings)) {
            MongoDatabase db = client.getDatabase("test");
            cartCollection = db.getCollection("cart", Cart.class);
            productCollection = db.getCollection("product", Product.class);
            transactionsDemo(client);
        }
    }

    private static void transactionsDemo(MongoClient client) {
        clearCollections();
        insertProductBeer();
        printDatabaseState();
        System.out.println("""
                           #########  NO  TRANSACTION #########
                           Alice wants 2 beers.
                           We have to create a cart in the 'cart' collection and update the stock in the 'product' collection.
                           The 2 actions are correlated but can not be executed at the same cluster time.
                           Any error blocking one operation could result in stock error or a sale of beer that we can't fulfill as we have no stock.
                           ------------------------------------""");
        aliceWantsTwoBeers();
        sleep();
        removingBeersFromStock();
        System.out.println("####################################\n");
        printDatabaseState();
        sleep();
        System.out.println("""
                           ######### WITH TRANSACTION #########
                           Alice wants 2 extra beers.
                           Now we can update the 2 collections simultaneously.
                           The 2 operations only happen when the transaction is committed.
                           ------------------------------------""");
        aliceWantsTwoExtraBeersInTransactionThenCommitOrRollback(client);
        sleep();
        System.out.println("""
                           ######### WITH TRANSACTION #########
                           Alice wants 2 extra beers.
                           This time we do not have enough beers in stock so the transaction will rollback.
                           ------------------------------------""");
        aliceWantsTwoExtraBeersInTransactionThenCommitOrRollback(client);
    }

    private static void aliceWantsTwoExtraBeersInTransactionThenCommitOrRollback(MongoClient client) {
        ClientSession session = client.startSession();
        try {
            session.startTransaction(TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
            aliceWantsTwoExtraBeers(session);
            sleep();
            removingBeerFromStock(session);
            session.commitTransaction();
        } catch (MongoException e) {
            session.abortTransaction();
            System.out.println("####### ROLLBACK TRANSACTION #######");
        } finally {
            session.close();
            System.out.println("####################################\n");
            printDatabaseState();
        }
    }

    private static void removingBeersFromStock() {
        System.out.println("Trying to update beer stock : -2 beers.");
        try {
            productCollection.updateOne(filterId, decrementTwoBeers);
        } catch (MongoException e) {
            System.out.println("########   MongoException   ########");
            System.out.println("##### STOCK CANNOT BE NEGATIVE #####");
            throw e;
        }
    }

    private static void removingBeerFromStock(ClientSession session) {
        System.out.println("Trying to update beer stock : -2 beers.");
        try {
            productCollection.updateOne(session, filterId, decrementTwoBeers);
        } catch (MongoException e) {
            System.out.println("########   MongoException   ########");
            System.out.println("##### STOCK CANNOT BE NEGATIVE #####");
            throw e;
        }
    }

    private static void aliceWantsTwoBeers() {
        System.out.println("Alice adds 2 beers in her cart.");
        cartCollection.insertOne(new Cart("Alice", List.of(new Cart.Item(BEER_ID, 2, BEER_PRICE))));
    }

    private static void aliceWantsTwoExtraBeers(ClientSession session) {
        System.out.println("Updating Alice cart : adding 2 beers.");
        cartCollection.updateOne(session, and(filterAlice, matchBeer), incrementTwoBeers);
    }

    private static void insertProductBeer() {
        productCollection.insertOne(new Product(BEER_ID, 5, BEER_PRICE));
    }

    private static void clearCollections() {
        productCollection.deleteMany(new BsonDocument());
        cartCollection.deleteMany(new BsonDocument());
    }

    private static void printDatabaseState() {
        System.out.println("Database state:");
        printProducts(productCollection.find().into(new ArrayList<>()));
        printCarts(cartCollection.find().into(new ArrayList<>()));
        System.out.println();
    }

    private static void printProducts(List<Product> products) {
        products.forEach(System.out::println);
    }

    private static void printCarts(List<Cart> carts) {
        if (carts.isEmpty()) {
            System.out.println("No carts...");
        } else {
            carts.forEach(System.out::println);
        }
    }

    private static void sleep() {
        System.out.println("Sleeping 1 second...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Oops!");
            e.printStackTrace();
        }
    }
}
