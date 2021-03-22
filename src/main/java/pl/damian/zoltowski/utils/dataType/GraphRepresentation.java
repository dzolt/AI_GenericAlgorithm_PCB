package pl.damian.zoltowski.utils.dataType;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.pcb.PCBJsonSerializer;
import pl.damian.zoltowski.utils.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Data
public class GraphRepresentation {
    public int pcbWidth;
    public int pcbHeight;
    public int populationSize;
    public int populationGenres;
    public double crossOverProbability;
    public double mutationProbability;
    public int mutationMaxMoveSegment;
    public double intersectionPenalty;
    public double lengthOutOfBoardPenalty;
    public double segmentsOutOfBoardPenalty;
    public double lengthPenalty;
    public List<Double> maximums;
    public List<Double> minimums;
    public List<Double> avgs;
    public String selectionOP;

    public GraphRepresentation() {
        this.populationSize = Constants.POPULATION_SIZE;
        this.populationGenres = Constants.POPULATION_OPERATORS_STOP_CONDITION;
        this.crossOverProbability = Constants.CROSS_OVER_PROBABILITY;
        this.mutationProbability = Constants.MUTATION_PROBABILITY;
        this.mutationMaxMoveSegment = Constants.MUTATION_MAX_MOVE_SEGMENT;
        this.intersectionPenalty = Constants.INTERSECTION_PENALTY;
        this.lengthOutOfBoardPenalty = Constants.LENGTH_OUT_OF_BOARD_PENALTY;
        this.segmentsOutOfBoardPenalty = Constants.SEGMENTS_OUT_OF_BOARD_PENALTY;
        this.lengthPenalty = Constants.LENGTH_PENALTY;
        this.maximums = new ArrayList<>();
        this.minimums = new ArrayList<>();
        this.avgs = new ArrayList<>();
        this.pcbHeight = -1;
        this.pcbWidth = -1;
        this.selectionOP = "";
    }

    public GraphRepresentation(int pcbWidth, int pcbHeight, String selectionOP, List<Double> maximums, List<Double> minimums, List<Double> avgs) {
        this.populationSize = Constants.POPULATION_SIZE;
        this.populationGenres = Constants.POPULATION_OPERATORS_STOP_CONDITION;
        this.crossOverProbability = Constants.CROSS_OVER_PROBABILITY;
        this.mutationProbability = Constants.MUTATION_PROBABILITY;
        this.mutationMaxMoveSegment = Constants.MUTATION_MAX_MOVE_SEGMENT;
        this.intersectionPenalty = Constants.INTERSECTION_PENALTY;
        this.lengthOutOfBoardPenalty = Constants.LENGTH_OUT_OF_BOARD_PENALTY;
        this.segmentsOutOfBoardPenalty = Constants.SEGMENTS_OUT_OF_BOARD_PENALTY;
        this.lengthPenalty = Constants.LENGTH_PENALTY;
        this.maximums = maximums;
        this.minimums = minimums;
        this.avgs = avgs;
        this.pcbWidth = pcbWidth;
        this.pcbHeight = pcbHeight;
        this.selectionOP = selectionOP;
    }

    private void jsonify(String fileName) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\data\\chart\\" + fileName;
        try (Writer writer = new FileWriter(filePath)) {
            gsonBuilder.create().toJson(this, writer);
        }
    }

    public void saveIndividualToFile(String fileName) {
        try {
            this.jsonify(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
