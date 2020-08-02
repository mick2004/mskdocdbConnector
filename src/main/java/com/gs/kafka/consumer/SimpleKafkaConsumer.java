package com.gs.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
public class SimpleKafkaConsumer {

    public static void main(String[] args) {
        //create kafka consumer
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, args[0]);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-first-consumer-group");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);

        //subscribe to topic
        consumer.subscribe(Collections.singleton(args[1]));

        MongoClientURI connectionString = new MongoClientURI(args[2]);
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase(args[3]);
        MongoCollection<Document> collection = database.getCollection(args[4]);

        //poll the record from the topic
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Message received: " + record.value());

                System.out.println("Going to Document Insert.Count of docs->"+collection.count());
                Document doc = new Document("name", "MongoDB")
                        .append("type", "database")
                        .append("count", 1)
                        .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                        .append("info", new Document("x", 203).append("Message", record.value()));
                collection.insertOne(doc);

                System.out.println("Document Inserted.Count of docs->"+collection.count());
                System.out.println("Looping through list of documents: " + record.value());
                for (Document cur : collection.find()) {
                    System.out.println(cur.toJson());
                }
            }
            consumer.commitAsync();
        }
    }
}
