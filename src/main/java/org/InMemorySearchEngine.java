package org;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.*;


public class InMemorySearchEngine {

    public JSONArray parseJSONFile() {

        InputStream jsonFile = getClass().getResourceAsStream("filepath");
        Reader readerJson = new InputStreamReader(jsonFile);

        Object fileObjects = JSONValue.parse(readerJson);
        JSONArray arrayObjects = (JSONArray)fileObjects;

        return arrayObjects;
    }
    String indexPath;
    String jsonFilePath;

    IndexWriter indexWriter = null;

    public InMemorySearchEngine(String indexPath, String jsonFilePath) {
        this.indexPath = indexPath;
        this.jsonFilePath =jsonFilePath;
    }

    public void createIndex(){
        JSONArray jsonObjects = parseJSONFile();
    }

}

