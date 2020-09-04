package org;

import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.LMSimilarity;

public class LMHiemstraSimilarity extends LMSimilarity {
    private final float lambda;

    public LMHiemstraSimilarity(CollectionModel collectionModel, float lambda) {
        super(collectionModel);
        if(Float.isFinite(lambda) == false || lambda < 0) {
            throw new IllegalArgumentException("illegal lambda value:" + lambda + ", must be non-negative finite value.");
        }
        this.lambda = lambda;
    }

    public LMHiemstraSimilarity(float lambda) {
        if(Float.isFinite(lambda) == false || lambda < 0) {
            throw new IllegalArgumentException("illegal lambda value:" + lambda + ", must be non-negative finite value.");
        }
        this.lambda = lambda;
    }

    public LMHiemstraSimilarity(CollectionModel collectionModel) {
        this(collectionModel, 0.15f);
    }

    public LMHiemstraSimilarity() {
        this(0.15f);

    }



    @Override
    public String getName() {
        return  "Hiemstra_LM " +lambda;
    }

    @Override
    protected double score(BasicStats stats, double freq, double docLen) {
        double termFreqDivNumOfTokens = ((LMStats)stats).getCollectionProbability();
        double score =  stats.getBoost() * (SimilarityModelHelper.log(((1 - lambda) * (freq / docLen)) / (lambda * termFreqDivNumOfTokens) + 1)
                        + SimilarityModelHelper.log(lambda * termFreqDivNumOfTokens));
        if (score > 0.0d) {
            return score;
        }
        return 0.0d;
    }
}
