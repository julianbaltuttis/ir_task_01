package org;


import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.ScoreDoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Log4j
public class Query {
    public static final String INDEX_DIR = "indexDir";
    public static final int SEARCH_RESULTS = 10;

    private IndexReader indexReader;
    private IndexSearcher searcher;
    private File indexDirectory;
    private Directory dir;
    private Analyzer analyzer;

    public Query() throws IOException {
        this.indexDirectory = new File(INDEX_DIR);
        this.dir = FSDirectory.open(indexDirectory.toPath());
        this.analyzer = new StandardAnalyzer();
        this.indexReader = DirectoryReader.open(dir);
        this.searcher = new IndexSearcher(indexReader);
    }

    public List<Document> searchIndex (String query) {

        try {

            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"conclusion","text"}, analyzer);
            TopDocs topDocs = searcher.search(queryParser.parse(query),10);
            List<Document> documents = new ArrayList<>();
            for(ScoreDoc sorceDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(sorceDoc.doc));
            }
            return documents;

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
