package com.gs.kafka.consumer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;




public final class SimpleMongoClient {
    private SimpleMongoClient() {
    }
    public static void main(String[] args) {

        String template = "mongodb://%s:%s@%s/testdb?replicaSet=rs0&readpreference=%s";
        String username = "aps";
        String password = "foobarbaz";
        String clusterEndpoint = "docdb-2020-08-02-07-10-04.cluster-c19znyxrd7zk.us-east-1.docdb.amazonaws.com:27017";
        String readPreference = "secondaryPreferred";
        String connectionString = String.format(template, username, password, clusterEndpoint, readPreference);

        System.out.println("Conn is "+connectionString);

        MongoClientURI clientURI = new MongoClientURI(connectionString);
        MongoClient mongoClient = new MongoClient(clientURI);

        MongoDatabase testDB = mongoClient.getDatabase("testdb");
        MongoCollection<Document> numbersCollection = testDB.getCollection("test");

        Document doc = new Document("name", "pi").append("value", 3.14159);
        numbersCollection.insertOne(doc);

        MongoCursor<Document> cursor = numbersCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

    }
}

