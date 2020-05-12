package org;

import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

@Log4j
public class Main {

    public static void main(String[] args) {
        log.info("--> Main().");
        System.out.println("Hello World!");

        log.info("<-- Main().");
    }

}


