package com.mongodb.quickstart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.quickstart.utils.Base64Util;
//import jdk.nashorn.internal.runtime.JSONFunctions;
import org.bson.Document;
import org.bson.types.Binary;

import java.util.Base64;
import java.util.Collections;
//import org.bson.types.Binary;

public class ReadFiles {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("Files");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("1909");

            // find one document with new Document
            Document student1 = gradesCollection.find(new Document("class_id", 222)).first();

            System.out.println("Student 1_raw: " + student1);
            System.out.println("Student 1_Json: " + student1.toJson());



//            byte[] content = (byte[]) Document.parse(student1.toJson()).get("content");
//            saveBinaryData(content, "D:\\Project\\java-quick-start\\src\\main\\java\\com\\mongodb\\quickstart\\test.txt.txt");
            // convert the json string to json object
            JSONObject student1_json = JSON.parseObject(student1.toJson());
            System.out.println("Student 1_json: " + student1_json);


            String content = (String) JSON.parseObject(JSON.parseObject(student1_json.get("content").toString()).get("$binary").toString()).get("base64");
            System.out.println("Student 1_content: " + content);

            // convert the base64 string to byte array
            byte[] content_byte = Base64Util.ToByte(content);
            System.out.println("Student 1_content_byte: " + content_byte);

            // save the byte[] to a file
            saveBinaryData(content_byte, "D:\\Project\\java-quick-start\\src\\main\\java\\com\\mongodb\\quickstart\\test.txt.txt");


//            // get the "_id" value of json
//            String str = "[{'columnId':5,'columnName':'人文历史'},{'columnId':2,'columnName':'商业视野'}]}";
//            JSONArray jsonArray = null;
//            jsonArray = new JSONArray(Collections.singletonList(str));
//            System.out.println(jsonArray.getJSONObject(0).get("columnName"));

//
//            // find one document with Filters.eq()
//            Document student2 = gradesCollection.find(eq("student_id", 10000)).first();
//            System.out.println("Student 2: " + student2.toJson());
//
//            // find a list of documents and iterate throw it using an iterator.
//            FindIterable<Document> iterable = gradesCollection.find(gte("student_id", 10000));
//            MongoCursor<Document> cursor = iterable.iterator();
//            System.out.println("Student list with a cursor: ");
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next().toJson());
//            }
//
//            // find a list of documents and use a List object instead of an iterator
//            List<Document> studentList = gradesCollection.find(gte("student_id", 10000)).into(new ArrayList<>());
//            System.out.println("Student list with an ArrayList:");
//            for (Document student : studentList) {
//                System.out.println(student.toJson());
//            }
//
//            // find a list of documents and print using a consumer
//            System.out.println("Student list using a Consumer:");
//            Consumer<Document> printConsumer = document -> System.out.println(document.toJson());
//            gradesCollection.find(gte("student_id", 10000)).forEach(printConsumer);
//
//            // find a list of documents with sort, skip, limit and projection
//            List<Document> docs = gradesCollection.find(and(eq("student_id", 10001), lte("class_id", 5)))
//                                                  .projection(fields(excludeId(), include("class_id", "student_id")))
//                                                  .sort(descending("class_id"))
//                                                  .skip(2)
//                                                  .limit(2)
//                                                  .into(new ArrayList<>());
//
//            System.out.println("Student sorted, skipped, limited and projected: ");
//            for (Document student : docs) {
//                System.out.println(student.toJson());
//            }
        }
    }
    // create a function to save the binary data to a system file
    public static void saveBinaryData(byte[] data, String fileName) {
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(fileName);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
}}
