package org;

import lombok.extern.log4j.Log4j;
import org.apache.lucene.index.CompositeReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
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


        log.info("<-- Main().");
    }

}


