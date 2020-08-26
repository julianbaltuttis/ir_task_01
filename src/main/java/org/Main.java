package org;

import lombok.extern.log4j.Log4j;

import java.io.IOException;

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
        try {
            QueryProcessor query = new QueryProcessor();
            query.getQuery();

        }catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("<-- Main().");
    }

}


