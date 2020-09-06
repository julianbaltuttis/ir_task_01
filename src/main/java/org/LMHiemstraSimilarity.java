package org;

import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.LMSimilarity;

public class LMHiemstraSimilarity extends LMSimilarity {
    private final double lambda;

    public LMHiemstraSimilarity(CollectionModel collectionModel, double lambda) {
        super(collectionModel);
        if(Double.isFinite(lambda) == false || lambda < 0) {
            throw new IllegalArgumentException("illegal lambda value:" + lambda + ", must be non-negative finite value.");
        }
        this.lambda = lambda;
    }

    public LMHiemstraSimilarity(double lambda) {
        if(Double.isFinite(lambda) == false || lambda < 0) {
            throw new IllegalArgumentException("illegal lambda value:" + lambda + ", must be non-negative finite value.");
        }
        this.lambda = lambda;
    }

    public LMHiemstraSimilarity(CollectionModel collectionModel) {
        this(collectionModel, 0.15);
    }

    public LMHiemstraSimilarity() {
        this(0.15);

    }



    @Override
    public String getName() {
        return  "Hiemstra_LM " +lambda;
    }

    @Override
    protected double score(BasicStats stats, double freq, double docLen) {
        //set to one is just applicable for our task
        int keyFrequency = 1;
        double score =  SimilarityModelHelper.log(1 + ( (lambda * freq * stats.getNumberOfFieldTokens()) / ((1 - lambda) * stats.getTotalTermFreq() * docLen)) ) * keyFrequency;
        if (score > 0.0d) {
            return score;
        }
        return 0.0d;
    }
}
