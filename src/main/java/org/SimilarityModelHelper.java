package org;

public class SimilarityModelHelper {

    public static final double LOG_E_OF_2 = Math.log(2.0D);

    public static final double LOG_2_OF_E = 1.0D / LOG_E_OF_2;

    public static double log(double d) {
        return (Math.log(d) * LOG_2_OF_E);
    }

    public static double log(double d1, double d2) {
        return (Math.log(d1/d2) * LOG_2_OF_E);
    }
}
