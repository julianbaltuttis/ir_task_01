package org;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.DFRSimilarity;
import org.apache.lucene.search.similarities.SimilarityBase;

public class DLH13Similarity extends SimilarityBase {
    private double k = 0.5;

    public DLH13Similarity() {
        super();
    }

    @Override
    protected double score(BasicStats stats, double freq, double docLen) {
        double f = SimilarityModelHelper.relativeFrequency(freq, docLen);
        //TODO Add Score formula.
        double score = 1;
        return score;
    }

    @Override
    public String toString() {
        return "DLH13";
    }
}
