package org;


import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j
public class QueryProcessor {
    public static final String INDEX_DIR = "/home/vestric/ir_task/indexDir";
    public static final int SEARCH_RESULTS = 1000;
    @Setter private String tag;

    private IndexReader indexReader;
    private IndexSearcher searcher;
    private File indexDirectory;
    private Directory dir;
    private Analyzer analyzer;

    public QueryProcessor(String tag) throws IOException {
        this.tag = tag;
        this.indexDirectory = new File(INDEX_DIR);
        this.dir = FSDirectory.open(indexDirectory.toPath());
        this.analyzer = new StandardAnalyzer();
        this.indexReader = DirectoryReader.open(dir);
        this.searcher = new IndexSearcher(indexReader);
    }

    public TopDocs searchIndex (String query) {

        try {
            
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"conclusion","text"}, analyzer);


            TopDocs topDocs = searcher.search(queryParser.parse(query),SEARCH_RESULTS);
            List<Document> documents = new ArrayList<>();

            return topDocs;

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<Document> getDocumentsFromTopDocs(TopDocs topDocs) {
        try{
            List<Document> documents = new ArrayList<>();
            for(ScoreDoc result : topDocs.scoreDocs) {
                documents.add(searcher.doc(result.doc));
            }
            return documents;

            } catch (IOException e) {
                log.error(e.getMessage());
            }
        return null;
    }

    public List<Result> getResultsFromTopDocs(TopDocs topDocs, int topicNumber) {
        try{
            List<Result> results = new ArrayList<>();
            int rank = 1;
            for(ScoreDoc hit : topDocs.scoreDocs) {
                Document doc = searcher.doc(hit.doc);
                float score = hit.score;
                Result result = new Result(topicNumber,doc.getField("id").stringValue(),rank,score,tag);
                results.add(result);
                rank++;
            }
            return results;

        }catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
