package org;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;


public class InMemorySearchEngine {

    String indexPath = "";

    String jsonFilePath ="";

    IndexWriter indexWriter = null;

    public InMemorySearchEngine(String indexPath, String jsonFilePath) {
        this.indexPath = indexPath;
        this.jsonFilePath = jsonFilePath;
    }
    public InMemorySearchEngine(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    public void createIndex(){
        JSONArray jsonObjects = parseJSONFile();
        openIndex();
        addDocuments(jsonObjects);
        //finish();
    }
    public JSONArray parseJSONFile() {
    try {
        InputStream jsonFile = getClass().getResourceAsStream(jsonFilePath);
        Reader readerJson = new InputStreamReader(jsonFile);

        Object fileObjects = JSONValue.parse(readerJson);
        JSONObject object = (JSONObject) fileObjects;
        Object fileArray = object.get("arguments");

        JSONArray arrayObjects = (JSONArray) fileArray;
        return arrayObjects;
    }catch(NullPointerException e){
        e.printStackTrace();
        return null;
    }
    }
    public boolean openIndex() {
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig();

            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(dir, iwc);

            return true;
        } catch (Exception e) {
            System.err.println("Error openin the index." + e.getMessage());
        }
        return false;
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


            }
        }
    }




