package org;

import lombok.extern.log4j.Log4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class Main {

    public static void main(String[] args) {
        log.info("--> Main().");
        System.out.println("Hello World!");

        /*Indexer index = new Indexer("indexDir","codebase/parliamentary.json", true);
        index.createIndex();

        index.setOverwrite(false);
        index.setJsonFilePath("codebase/debateorg.json");
        index.createIndex();

        index.setJsonFilePath("codebase/debatepedia.json");
        index.createIndex();

        index.setJsonFilePath("codebase/debatewise.json");
        index.createIndex();

        index.setJsonFilePath("codebase/idebate.json");
        index.createIndex();*/


        Topics topics = new Topics("codebase/topics.xml");
        List<Topic> topicList = topics.getTopics();

        //TODO Put everything in own method.
        try {
            QueryProcessor query = new QueryProcessor();
            File outputFile = new File("out/run.txt");
            FileWriter writer;
            if(outputFile.createNewFile()) {
                log.info("File is created!");
                writer = new FileWriter(outputFile);
            }else {
                writer = new FileWriter(outputFile,false);

            }


            for(Topic topic : topicList) {
                List<Result> results = new ArrayList<>();
                int topicNumber = topic.getNumber();
                String topicTitle = topic.getTitle();

                TopDocs hits = query.searchIndex(topicTitle);
                results = query.getResultsFromTopDocs(hits, topicNumber);
                for(Result result : results){

                    writer.write(result.toString());
                }

            }
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }


        log.info("<-- Main().");
    }

}


