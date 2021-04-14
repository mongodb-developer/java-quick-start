package com.mongodb.quickstart.csfle;

import com.mongodb.MongoException;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.model.vault.EncryptOptions;
import com.mongodb.client.vault.ClientEncryption;
import org.bson.*;
import org.bson.json.JsonWriterSettings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.quickstart.csfle.ConsoleDecoration.printSection;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ClientSideFieldLevelEncryption {

    private static final int SIZE_MASTER_KEY = 96;
    private static final String DETERMINISTIC = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    private static final String RANDOM = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";
    private static final String LOCAL = "local";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String MASTER_KEY_FILENAME = "master_key.txt";
    private static final MongoNamespace ENCRYPTED_NS = new MongoNamespace("encryptedDB", "users");
    private static final JsonWriterSettings INDENT = JsonWriterSettings.builder().indent(true).build();
    private static final String BOBBY = "Bobby";
    private static final String ALICE = "Alice";

    public static void main(String[] args) {
        new ClientSideFieldLevelEncryption().demo();
    }

    private void demo() {
        printSection("MASTER KEY");
        byte[] masterKey = generateNewOrRetrieveMasterKeyFromFile(MASTER_KEY_FILENAME);
        System.out.println("Master Key: " + Arrays.toString(masterKey));

        ConnectionHelper connectionHelper = new ConnectionHelper(masterKey);
        connectionHelper.cleanCluster();

        ClientEncryption encryption = connectionHelper.getEncryptionClient();
        MongoClient client = connectionHelper.getMongoClient();
        MongoCollection<Document> vaultColl = connectionHelper.getVaultCollection();
        MongoCollection<Document> usersColl = client.getDatabase(ENCRYPTED_NS.getDatabaseName())
                                                    .getCollection(ENCRYPTED_NS.getCollectionName());

        printSection("CREATE KEY ALT NAMES UNIQUE INDEX");
        createKeyAltNamesUniqueIndex(vaultColl);

        printSection("CREATE DATA ENCRYPTION KEYS");
        createBobbyAndAliceKeys(encryption);

        printSection("INSERT ENCRYPTED DOCUMENTS FOR BOBBY & ALICE");
        createAndInsertBobbyAndAlice(encryption, usersColl);

        printSection("FIND BOBBY'S DOCUMENT BY PHONE");
        readBobby(encryption, usersColl);

        printSection("READING ALICE'S DOCUMENT");
        readAliceIfPossible(usersColl);

        printSection("REMOVE ALICE's KEY + RESET THE CONNECTION (reset DEK cache)");
        removeAliceDataEncryptionKey(vaultColl);
        client = connectionHelper.resetMongoClient();
        usersColl = client.getDatabase(ENCRYPTED_NS.getDatabaseName()).getCollection(ENCRYPTED_NS.getCollectionName());

        printSection("TRY TO READ ALICE DOC AGAIN BUT FAIL");
        readAliceIfPossible(usersColl);

        connectionHelper.closeConnections();
    }

    private void removeAliceDataEncryptionKey(MongoCollection<Document> vaultColl) {
        long deletedCount = vaultColl.deleteOne(eq("keyAltNames", ALICE)).getDeletedCount();
        System.out.println("Alice key is now removed: " + deletedCount + " key removed.");
    }

    private void readAliceIfPossible(MongoCollection<Document> usersColl) {
        try {
            String aliceDoc = usersColl.find(eq("name", ALICE)).first().toJson(INDENT);
            System.out.println("Before we remove Alice's key, we can read her document.");
            System.out.println(aliceDoc);
        } catch (MongoException e) {
            System.err.println("We get a MongoException because 'libmongocrypt' can't decrypt these fields anymore.");
        }
    }

    private void createKeyAltNamesUniqueIndex(MongoCollection<Document> vaultColl) {
        vaultColl.createIndex(ascending("keyAltNames"),
                              new IndexOptions().unique(true).partialFilterExpression(exists("keyAltNames")));
    }

    private void createBobbyAndAliceKeys(ClientEncryption encryption) {
        BsonBinary bobbyKeyId = encryption.createDataKey(LOCAL, keyAltName(BOBBY));
        BsonBinary aliceKeyId = encryption.createDataKey(LOCAL, keyAltName(ALICE));
        System.out.println("Created Bobby's data key ID: " + bobbyKeyId.asUuid());
        System.out.println("Created Alice's data key ID: " + aliceKeyId.asUuid());
    }

    private void createAndInsertBobbyAndAlice(ClientEncryption encryption, MongoCollection<Document> usersColl) {
        Document bobby = createBobbyDoc(encryption);
        Document alice = createAliceDoc(encryption);
        int nbInsertedDocs = usersColl.insertMany(asList(bobby, alice)).getInsertedIds().size();
        System.out.println(nbInsertedDocs + " docs have been inserted.");
    }

    private Document createAliceDoc(ClientEncryption encryption) {
        BsonBinary phone = encryption.encrypt(new BsonString("09 87 65 43 21"), deterministic(ALICE));
        BsonBinary bloodType = encryption.encrypt(new BsonString("O+"), random(ALICE));
        return new Document("name", ALICE).append("age", 28).append("phone", phone).append("blood_type", bloodType);
    }

    private Document createBobbyDoc(ClientEncryption encryption) {
        BsonBinary phone = encryption.encrypt(new BsonString("01 23 45 67 89"), deterministic(BOBBY));
        BsonBinary bloodType = encryption.encrypt(new BsonString("A+"), random(BOBBY));
        BsonDocument medicalEntry = new BsonDocument("test", new BsonString("heart")).append("result", new BsonString("bad"));
        BsonBinary medicalRecord = encryption.encrypt(new BsonArray(singletonList(medicalEntry)), random(BOBBY));
        return new Document("name", BOBBY).append("age", 33)
                                          .append("phone", phone)
                                          .append("blood_type", bloodType)
                                          .append("medical_record", medicalRecord);
    }

    private void readBobby(ClientEncryption encryption, MongoCollection<Document> usersColl) {
        BsonBinary phone = encryption.encrypt(new BsonString("01 23 45 67 89"), deterministic(BOBBY));
        String doc = usersColl.find(eq("phone", phone)).first().toJson(INDENT);
        System.out.println("Bobby document found by phone number:");
        System.out.println(doc);
    }

    private EncryptOptions deterministic(String keyAltName) {
        return new EncryptOptions(DETERMINISTIC).keyAltName(keyAltName);
    }

    private EncryptOptions random(String keyAltName) {
        return new EncryptOptions(RANDOM).keyAltName(keyAltName);
    }

    private DataKeyOptions keyAltName(String altName) {
        return new DataKeyOptions().keyAltNames(singletonList(altName));
    }

    private byte[] generateNewOrRetrieveMasterKeyFromFile(String filename) {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        try {
            retrieveMasterKeyFromFile(filename, masterKey);
            System.out.println("An existing Master Key was found in file \"" + filename + "\".");
        } catch (IOException e) {
            masterKey = generateMasterKey();
            saveMasterKeyToFile(filename, masterKey);
            System.out.println("A new Master Key has been generated and saved to file \"" + filename + "\".");
        }
        return masterKey;
    }

    private void retrieveMasterKeyFromFile(String filename, byte[] masterKey) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            fis.read(masterKey, 0, SIZE_MASTER_KEY);
        }
    }

    private byte[] generateMasterKey() {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        SECURE_RANDOM.nextBytes(masterKey);
        return masterKey;
    }

    private void saveMasterKeyToFile(String filename, byte[] masterKey) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(masterKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
