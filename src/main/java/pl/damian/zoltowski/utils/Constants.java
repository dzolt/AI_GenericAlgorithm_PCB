package pl.damian.zoltowski.utils;

public class Constants {
    /**
     * FILE SPLITTERS
     */
    public final static String DATA_SPLITTER = ";";

    /**
     * ALGORITHM CONSTANTS
     */
    public final static int MAX_STEPS_INDIVIDUAL_GENERATION = 1000;
    public final static int PROBABILITY_OF_DIRECTION_INCREASE = 3;
    public final static int POPULATION_SIZE = 10_000;
    public final static int POPULATION_OPERATORS_STOP_CONDITION = 100;
    public final static double CROSS_OVER_PROBABILITY = 0.7;
    public final static double MUTATION_PROBABILITY = 0.01;
    public final static int MUTATION_MAX_MOVE_SEGMENT = 1;

    /**
     * FITNESS CONSTANTS
     */
    public final static double INTERSECTION_PENALTY = 48.0;
    public final static double LENGTH_OUT_OF_BOARD_PENALTY = 16.7;
    public final static double SEGMENTS_OUT_OF_BOARD_PENALTY = 7.5;
    public final static double LENGTH_PENALTY = 2.5;



}
