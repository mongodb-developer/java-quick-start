package com.mongodb.quickstart.csfle;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class ConnectionHelper {

    private static final ConnectionString CONNECTION_STR = new ConnectionString(System.getProperty("mongodb.uri"));
    private static final MongoNamespace VAULT_NS = new MongoNamespace("csfle", "vault");
    private static final MongoNamespace ENCRYPTED_NS = new MongoNamespace("encryptedDB", "users");
    private static final String LOCAL = "local";
    private final Map<String, Map<String, Object>> kmsProviders;
    private final ClientEncryption encryption;
    private MongoClient client;

    public ConnectionHelper(byte[] masterKey) {
        ConsoleDecoration.printSection("INITIALIZATION");
        this.kmsProviders = generateKmsProviders(masterKey);
        this.encryption = createEncryptionClient();
        this.client = createMongoClient();
    }

    private MongoClient createMongoClient() {
        AutoEncryptionSettings aes = AutoEncryptionSettings.builder()
                                                           .keyVaultNamespace(VAULT_NS.getFullName())
                                                           .kmsProviders(kmsProviders)
                                                           .bypassAutoEncryption(true)
                                                           .build();
        MongoClientSettings mcs = MongoClientSettings.builder()
                                                     .applyConnectionString(CONNECTION_STR)
                                                     .autoEncryptionSettings(aes)
                                                     .build();
        System.out.println("=> Creating MongoDB client with automatic decryption.");
        return MongoClients.create(mcs);
    }

    private ClientEncryption createEncryptionClient() {
        MongoClientSettings kvmcs = MongoClientSettings.builder().applyConnectionString(CONNECTION_STR).build();
        ClientEncryptionSettings ces = ClientEncryptionSettings.builder()
                                                               .keyVaultMongoClientSettings(kvmcs)
                                                               .keyVaultNamespace(VAULT_NS.getFullName())
                                                               .kmsProviders(kmsProviders)
                                                               .build();
        System.out.println("=> Creating encryption client.");
        return ClientEncryptions.create(ces);
    }

    public MongoClient resetMongoClient() {
        client.close();
        try {
            // sleep to make sure we are not reusing the Data Encryption Key Cache.
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return client = createMongoClient();
    }

    public MongoCollection<Document> getVaultCollection() {
        return client.getDatabase(VAULT_NS.getDatabaseName()).getCollection(VAULT_NS.getCollectionName());
    }

    public ClientEncryption getEncryptionClient() {
        return encryption;
    }

    public MongoClient getMongoClient() {
        return client;
    }

    public void cleanCluster() {
        System.out.println("=> Cleaning entire cluster.");
        client.getDatabase(VAULT_NS.getDatabaseName()).drop();
        client.getDatabase(ENCRYPTED_NS.getDatabaseName()).drop();
    }

    private Map<String, Map<String, Object>> generateKmsProviders(byte[] masterKey) {
        System.out.println("=> Creating local Key Management System using the master key.");
        return new HashMap<String, Map<String, Object>>() {{
            put(LOCAL, new HashMap<String, Object>() {{
                put("key", masterKey);
            }});
        }};
    }

    public void closeConnections() {
        encryption.close();
        client.close();
    }
}
