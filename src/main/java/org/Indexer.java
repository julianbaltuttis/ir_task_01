package org;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
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
    public static final String DOC_ANNOTATIONS="annotations";
    public static final String DOC_ASPECTS="aspects";

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
        //addDocuments();
        addDocuments2();
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
            log.error("Error opening the index." + e.getMessage());
        }
        return false;
    }
    public void addDocuments() {
        log.info("--> addDocuments().");

        File jsonFile = new File(jsonFilePath);
        JsonFactory jsonfactory = new JsonFactory();
        int numberOfRecords = 0;
        try {

            JsonParser jsonParser = jsonfactory.createParser(jsonFile);
            Document doc = new Document();


            
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jsonParser.getCurrentName();

                System.out.println("FieldName: "+fieldName);
                
                if(DOC_ARGUMENTS.equals(fieldName)) {

                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        String argumentFieldName =jsonParser.getCurrentName();

                        System.out.println("ArguementFieldName: "+argumentFieldName);

                        if(DOC_ID.equals(argumentFieldName)){
                            doc.add(new StringField(argumentFieldName, jsonParser.getText(), Field.Store.NO));
                            jsonParser.nextToken();
                            continue;
                        }

                        if(DOC_CONCLUSION.equals(argumentFieldName)){
                            doc.add(new StringField(argumentFieldName, jsonParser.getText(),Field.Store.YES));
                            jsonParser.nextToken();
                            continue;
                        }

                        if(DOC_PREMISES.equals(argumentFieldName)){

                            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                String premiseFieldName = jsonParser.getCurrentName();

                                System.out.println("PremiseFieldName: "+premiseFieldName);

                                if(DOC_TEXT.equals(premiseFieldName)) {
                                    doc.add(new StringField(premiseFieldName, jsonParser.getText(), Field.Store.NO));
                                    jsonParser.nextToken();
                                    continue;
                                }

                                if(DOC_STANCE.equals(premiseFieldName)) {
                                    doc.add(new StringField(premiseFieldName, jsonParser.getText(), Field.Store.YES));
                                    jsonParser.nextToken();
                                    continue;
                                }
                                if(DOC_ANNOTATIONS.equals(premiseFieldName)) {

                                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                        jsonParser.nextToken();
                                        System.out.println("Annotations loop");
                                    }
                                }
                                jsonParser.nextToken();

                            }
                        }
                        if(DOC_ASPECTS.equals(argumentFieldName)){
                            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                jsonParser.nextToken();
                                System.out.println("Aspects loop");

                            }
                            continue;
                        }

                        if(DOC_SOURCE_ID.equals(argumentFieldName)) {
                            doc.add(new StringField(argumentFieldName, jsonParser.getText(), Field.Store.NO));
                            jsonParser.nextToken();
                            continue;

                        }
                        if(jsonParser.nextToken() == JsonToken.END_OBJECT) {
                            indexWriter.addDocument(doc);
                            doc = new Document();
                            numberOfRecords++;
                            jsonParser.nextToken();
                        }
                        jsonParser.nextToken();
                    }
                }
                jsonParser.nextToken();
            }
            log.info("Records found: "+ numberOfRecords);
        } catch(IOException e) {
            log.error("Failed parsing JSON file"+ e.getMessage());
        } finally {
            log.info("<-- addDocuments().");
        }
    }
    public void addDocuments2(){
        try{
            File jsonFile = new File(jsonFilePath);
            int numberOfRecords = 0;
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            JsonParser jsonParser = mapper.getFactory().createParser(jsonFile);
            JsonNode arguments = mapper.readTree(jsonFile).at("/arguments");

            jsonParser.nextToken();

            while(jsonParser.nextToken() != JsonToken.END_ARRAY) {

                Argument argument = mapper.readValue(jsonParser, Argument.class);
                numberOfRecords++;
            }
            log.info("Records: "+ numberOfRecords);
        }catch (IOException e) {
            log.error(e.getMessage());
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
    public void test() {
        File jsonFile = new File(jsonFilePath);
        JsonFactory jsonfactory = new JsonFactory();
        int numberOfRecords = 0;
        try {

            JsonParser jsonParser = jsonfactory.createParser(jsonFile);
            Document doc = new Document();


            jsonParser.nextToken();

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                String fieldName = jsonParser.getCurrentName();
                System.out.println("FieldName: " + fieldName);
                jsonParser.nextToken();
            }
        }catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}




