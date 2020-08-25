package org;

import lombok.extern.log4j.Log4j;

@Log4j
public class Main {

    public static void main(String[] args) {
        log.info("--> Main().");
        System.out.println("Hello World!");

        Indexer index = new Indexer("indexDir","codebase/parliamentary.json", true);
        index.createIndex();

        index.setOverwrite(false);
        index.setJsonFilePath("codebase/debateorg.json");
        index.createIndex();

        index.setJsonFilePath("codebase/debatepedia.json");
        index.createIndex();

        index.setJsonFilePath("codebase/debatewise.json");
        index.createIndex();

        index.setJsonFilePath("codebase/idebate.json");
        index.createIndex();

        log.info("<-- Main().");
    }

}


