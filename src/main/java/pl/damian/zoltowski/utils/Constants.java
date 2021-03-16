package pl.damian.zoltowski.utils;

public class Constants {
    /**
     * FILE SPLITTERS
     */
    public final static String DATA_SPLITTER = ";";

    /**
     * ALGORITHM CONSTANTS
     */
    public final static int MAX_STEPS_INDIVIDUAL_GENERATION = 100;
    public final static int PROBABILITY_OF_DIRECTION_INCREASE = 3;
    public final static int POPULATION_SIZE = 2_000;
    public final static int POPULATION_OPERATORS_STOP_CONDITION = 10;
    public final static double CROSS_OVER_PROBABILITY = 0.7;
    public final static double MUTATION_PROBABILITY = 0.01;

    /**
     * FITNESS CONSTANTS
     */
    public final static double INTERSECTION_PENALTY = 35.0;
    public final static double LENGTH_OUT_OF_BOARD_PENALTY = 16.7;
    public final static double SEGMENTS_OUT_OF_BOARD_PENALTY = 28.5;
    public final static double LENGTH_PENALTY = 11.0;



}
