package org;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class Indexer {

    public static final String DOC_ARGUMENTS ="arguments";
    public static final String DOC_ID ="id";
    public static final String DOC_CONCLUSION="conclusion";
    public static final String DOC_PREMISES="premises";
    public static final String DOC_TEXT="text";
    public static final String DOC_STANCE="stance";
    public static final String DOC_CONTEXT="context";
    public static final String DOC_SOURCE_ID="sourceId";

    private String indexPath = "";

    private @Setter String jsonFilePath ="";

    private IndexWriter indexWriter = null;

    private @Setter boolean overwrite = true;

    public Indexer(String indexPath, String jsonFilePath, boolean overwrite) {
        this.indexPath = indexPath;
        this.jsonFilePath = jsonFilePath;
        this.overwrite = overwrite;
    }

    public void createIndex(){
        //JSONArray jsonObjects = parseJSONFile();
        openIndex();
        //addDocuments(jsonObjects);
        addDocuments();
        finish();
    }
    public JSONArray parseJSONFile() {
    try {
        BufferedInputStream jsonFile = (BufferedInputStream) getClass().getResourceAsStream(jsonFilePath);
        Reader readerJson = new InputStreamReader(jsonFile);

        //Object fileArray = ((JSONObject) JSONValue.parse(readerJson)).get("arguments");
        //readerJson.close();
        //jsonFile.close();

        return (JSONArray) ((JSONObject) JSONValue.parse(readerJson)).get("arguments");
    }catch(NullPointerException e){
        e.printStackTrace();
        return null;
    }
    }

    public boolean openIndex() {
        try {
            File indexDirectory = new File(indexPath);
            Directory dir = FSDirectory.open(indexDirectory.toPath());

            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            if(overwrite) {
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            } else {
                iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
            }

            indexWriter = new IndexWriter(dir, iwc);

            return true;
        } catch (Exception e) {
            log.error("Error openin the index." + e.getMessage());
        }
        return false;
    }
    public void addDocuments() {
        File jsonFile = new File(jsonFilePath);
        JsonFactory jsonfactory = new JsonFactory();
        int numberOfRecords = 0;
        try {

            JsonParser jsonParser = jsonfactory.createParser(jsonFile);
            Document doc = new Document();

            JsonToken jsonToken = jsonParser.nextToken();
            while (jsonToken != JsonToken.END_OBJECT) {
                String fieldName = jsonParser.getCurrentName();

                if(DOC_ARGUMENTS.equals(fieldName)) {
                    jsonToken = jsonParser.nextToken();

                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        String argumentFieldName =jsonParser.getCurrentName();

                        if(DOC_ID.equals(argumentFieldName)){
                            jsonToken = jsonParser.nextToken();
                            doc.add(new StringField(argumentFieldName, jsonParser.getText(), Field.Store.NO));
                        }

                        if(DOC_CONCLUSION.equals(argumentFieldName)){
                            jsonToken =jsonParser.nextToken();
                            doc.add(new StringField(argumentFieldName, jsonParser.getText(),Field.Store.YES));
                        }

                        if(DOC_PREMISES.equals(argumentFieldName)){
                            jsonToken = jsonParser.nextToken();
                            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                String premiseFieldName = jsonParser.getCurrentName();

                                if(DOC_TEXT.equals(premiseFieldName)) {
                                    jsonToken = jsonParser.nextToken();
                                    doc.add(new StringField(premiseFieldName, jsonParser.getText(), Field.Store.NO));
                                }

                                if(DOC_STANCE.equals(premiseFieldName)) {
                                    jsonToken = jsonParser.nextToken();
                                    doc.add(new StringField(premiseFieldName, jsonParser.getText(), Field.Store.YES));
                                }
                                else {
                                    jsonToken = jsonParser.nextToken();
                                }

                            }
                        }

                        if(DOC_CONTEXT.equals(argumentFieldName)) {
                            jsonToken = jsonParser.nextToken();
                            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                                String contextFieldName = jsonParser.getCurrentName();

                                if(DOC_SOURCE_ID.equals(contextFieldName)) {
                                    jsonToken = jsonParser.nextToken();
                                    doc.add(new StringField(contextFieldName, jsonParser.getText(), Field.Store.NO));
                                }
                                else {
                                    jsonToken = jsonParser.nextToken();
                                }
                            }
                        }
                        if(jsonToken == JsonToken.END_OBJECT) {
                            indexWriter.addDocument(doc);
                            doc = new Document();
                            numberOfRecords++;
                        }
                        jsonToken = jsonParser.nextToken();
                    }
                }
                jsonToken = jsonParser.nextToken();
            }
        } catch(IOException e) {
            log.error("Failed parsing JSON file"+ e.getMessage());
        }
    }
    public void addDocuments(JSONArray jsonObjects) {

        for(JSONObject object : (List<JSONObject>) jsonObjects){
            Document doc = new Document();
            doc.add(new StringField("id", (String)object.get("id"), Field.Store.NO));
            doc.add(new StringField("conclusion", (String)object.get("conclusion"), Field.Store.YES));

            JSONArray premisesArray = (JSONArray)object.get("premises");
            for(JSONObject premisObject : (List<JSONObject>) premisesArray) {
                doc.add(new StringField("text",(String)premisObject.get("text"), Field.Store.NO));
                doc.add(new StringField("stance",(String)premisObject.get("stance"), Field.Store.YES));
            }

            doc.add(new StringField("sourceID", (String)((JSONObject)object.get("context")).get("sourceId"), Field.Store.NO));

            try {
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                System.err.println("Error adding documents to the index" + e.getMessage());
            }
        }
    }
    public void finish() {
        try{
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            System.err.println("We had a problem closing the index: "+ e.getMessage());
        }

    }
}




