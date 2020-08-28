package org;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Result {

    private int topicNumber;
    private String doc_id;
    private int rank;
    private float score;
    private String tag;

    @Override
    public String toString() {
        return topicNumber+" Q0 " + doc_id +" "+ rank +" "+ score + " " +tag+ "\n";
    }
}
