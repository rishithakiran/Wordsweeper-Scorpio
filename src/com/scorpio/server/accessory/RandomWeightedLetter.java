package com.scorpio.server.accessory;

/**
 * Generates a letter with a random distribution that favors more common letters (e.g. E, T, A, etc.)
 * over less common ones, such as Z
 * @author Josh
 */
public class RandomWeightedLetter {
    // The letter that will be returned
    private final String v;

    // The cumulative weight of all elements. As we use percentages, this will be 100%
    private static double totalWeight = 100.0;

    // Table of all weights
    private static LetterTuple[] freqs = {
            new LetterTuple("E", 12.70),
            new LetterTuple("T", 9.06),
            new LetterTuple("A", 8.17),
            new LetterTuple("O", 7.51),
            new LetterTuple("I", 6.97),
            new LetterTuple("N", 6.75),
            new LetterTuple("S", 6.33),
            new LetterTuple("H", 6.09),
            new LetterTuple("R", 5.99),
            new LetterTuple("D", 4.25),
            new LetterTuple("L", 4.03),
            new LetterTuple("C", 2.78),
            new LetterTuple("U", 2.76),
            new LetterTuple("M", 2.41),
            new LetterTuple("W", 2.36),
            new LetterTuple("F", 2.23),
            new LetterTuple("G", 2.02),
            new LetterTuple("Y", 1.97),
            new LetterTuple("P", 1.93),
            new LetterTuple("B", 1.49),
            new LetterTuple("V", 0.98),
            new LetterTuple("K", 0.77),
            new LetterTuple("J", 0.15),
            new LetterTuple("X", 0.15),
            new LetterTuple("Qu", 0.10),
            new LetterTuple("Z", 0.07)
    };

    public String getValue(){
        return this.v;
    }

    /**
     * Generate a new RandomWeightedLetter instance
     */
    public RandomWeightedLetter(){
        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for (int i = 0; i < freqs.length; ++i)
        {
            random -= freqs[i].weight;
            if (random <= 0.0d)
            {
                randomIndex = i;
                break;
            }
        }
        this.v= freqs[randomIndex].letter;
    }

    // Private internal class to for the rows of our lookup tables
    private static class LetterTuple {
        public final String letter;
        public final double weight;
        public LetterTuple(String l, double w){
            this.letter = l;
            this.weight = w;
        }
    }
}
