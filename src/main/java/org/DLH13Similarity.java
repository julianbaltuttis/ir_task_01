package org;

import org.apache.lucene.search.CollectionStatistics;
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
        /* keyFrequency is the number of term occurances in the query
        1 is just applicable for our case, because just one topic has one term that occurs two times.
         */
        int keyFrequency = 1;

        double score =  stats.getBoost() * (keyFrequency *
                (freq * SimilarityModelHelper.log((freq * stats.getAvgFieldLength() / docLen) *
                        (stats.getNumberOfDocuments() / stats.getTotalTermFreq()) )
                + 0.5d * SimilarityModelHelper.log(2d * Math.PI * freq * (1d - f)))
        /(freq + k));
        return score;
    }

    @Override
    public String toString() {
        return "DLH13";
    }
}
