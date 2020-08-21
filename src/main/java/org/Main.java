package org;

import lombok.extern.log4j.Log4j;
import org.json.simple.JSONArray;
// Somebody else made a change and I forgot to pull before I made my changes. Oh oh!

@Log4j
public class Main {

    public static void main(String[] args) {
        log.info("--> Main().");
        System.out.println("Hello World!");
        Indexer search = new Indexer("/parliamentary.json");
        JSONArray objects = search.parseJSONFile();
        search.addDocuments(objects);
        log.info("<-- Main().");
    }

}


