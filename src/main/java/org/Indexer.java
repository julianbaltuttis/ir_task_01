package org;


import lombok.Setter;
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
import java.util.List;


public class Indexer {

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
        JSONArray jsonObjects = parseJSONFile();
        openIndex();
        addDocuments(jsonObjects);
        finish();
    }
    public JSONArray parseJSONFile() {
    try {
        InputStream jsonFile = getClass().getResourceAsStream(jsonFilePath);
        Reader readerJson = new InputStreamReader(jsonFile);

        Object fileArray = ((JSONObject) JSONValue.parse(readerJson)).get("arguments");
        readerJson.close();
        jsonFile.close();

        return (JSONArray) fileArray;

    }catch(NullPointerException e){
        e.printStackTrace();
        return null;
    } catch (IOException e) {
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




