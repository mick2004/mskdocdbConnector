# mskdocdbConnector

Usage

java -cp .:msk-docdb-1.0-SNAPSHOT.jar com.gs.kafka.consumer.SimpleKafkaConsumerToDocDB <<BootstrapBrokerString>> << KafkaSourceTopic >> << MongoURL >> << database >> << collection >>
  
 Example
  
java -cp .:msk-docdb-1.0-SNAPSHOT.jar com.gs.kafka.consumer.SimpleKafkaConsumerToDocDB b-1.mskcluster.13vj89.c1.kafka.us-east-1.amazonaws.com:9092,b-2.mskcluster.13vj89.c1.kafka.us-east-1.amazonaws.com:9092,b-3.mskcluster.13vj89.c1.kafka.us-east-1.amazonaws.com:9092 amazonmskapigwblog "mongodb://aps:<<paasword>>@docdb-2020-08-02-05-14-43.cluster-c19znyxrd7zk.us-east-1.docdb.amazonaws.com:27017/testdb?replicaSet=rs0&readPreference=secondaryPreferred" testdb test

