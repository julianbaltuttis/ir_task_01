package org;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;

@Log4j
public class Indexer {

    private @Setter String indexPath;

    private @Setter String jsonFilePath;

    private IndexWriter indexWriter = null;

    private @Setter boolean overwrite;

    public Indexer(String indexPath, String jsonFilePath, boolean overwrite) {
        this.indexPath = indexPath;
        this.jsonFilePath = jsonFilePath;
        this.overwrite = overwrite;
    }

    public void createIndex(){
        log.info("--> createIndex().");
        openIndex();
        addDocuments();
        finish();
        log.info("<--createIndex().");

    }

    public void openIndex() {
        log.info("--> openIndex().");
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

        } catch (Exception e) {
            log.error("Error opening the index." + e.getMessage());
        }
        log.info("<-- openIndex().");
    }

    public void addDocuments(){
        log.info("--> addDocuments().");
        try{
            File jsonFile = new File(jsonFilePath);
            int numberOfRecords = 0;
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);
            JsonFactory factory = new MappingJsonFactory();
            JsonParser jsonParser = factory.createParser(jsonFile);

            JsonToken current = jsonParser.nextToken();
            if(current != JsonToken.START_OBJECT) {
                log.error("Error: root should be object.");
                return;
            }

            while(jsonParser.nextToken() != JsonToken.END_OBJECT) {

                current = jsonParser.nextToken();

                if(current == JsonToken.START_ARRAY) {

                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {

                        Argument argument = mapper.readValue(jsonParser, Argument.class);
                        log.info(argument.toString());
                        indexWriter.addDocument(argument.getArgumentAsDocument());

                        numberOfRecords++;
                        log.info(numberOfRecords);

                    }
                }else {
                    log.error("Error: Next node should be an array.");
                }

            }
            log.info("Records: "+ numberOfRecords);
        }catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("<-- addDocuments().");
    }

    public void finish() {
        log.info("--> finish().");
        try{
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            log.error("We had a problem closing the index: "+ e.getMessage());
        }
        log.info("<-- finish().");
    }

}




