package pl.damian.zoltowski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import pl.damian.zoltowski.crossover.CrossOverAlgorithm;
import pl.damian.zoltowski.crossover.MultiPointCrossover;
import pl.damian.zoltowski.mutation.MutationA;
import pl.damian.zoltowski.mutation.MutationAlgorithm;
import pl.damian.zoltowski.mutation.SimpleMutation;
import pl.damian.zoltowski.pcb.Direction;
import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.pcb.PCBJsonSerializer;
import pl.damian.zoltowski.pcb.Path;
import pl.damian.zoltowski.selection.RouletteSelection;
import pl.damian.zoltowski.selection.SelectionAlgorithm;
import pl.damian.zoltowski.selection.TournamentSelection;
import pl.damian.zoltowski.utils.Config;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.dataType.GraphRepresentation;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.dataType.Tuple;
import pl.damian.zoltowski.visualization.PythonProcessBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GenericAlgorithmPCBApplication {

    public static void main(String[] args) throws CloneNotSupportedException {
        Config config = new Config().readConfigFromFile("zad3.txt");
        PythonProcessBuilder pythonProcessBuilder = new PythonProcessBuilder();
        List<PCBIndividual> population = new ArrayList<>();
        SelectionAlgorithm selectionAlgorithm = new RouletteSelection();
//        SelectionAlgorithm selectionAlgorithm = new TournamentSelection(5);
        CrossOverAlgorithm crossing = new MultiPointCrossover();
        MutationAlgorithm mutation = new MutationA();
        List<PCBIndividual> bestFromPopulations = new ArrayList<>();
        GraphRepresentation graphRepresentation = new GraphRepresentation();
        graphRepresentation.pcbWidth = config.getPcb_width();
        graphRepresentation.pcbHeight = config.getPcb_height();
        if (selectionAlgorithm.getClass() == TournamentSelection.class) {
            graphRepresentation.selectionOP = "Turniej: " + ((TournamentSelection) selectionAlgorithm).getK();
        } else {
            graphRepresentation.selectionOP = "Ruletka";
        }

//         create population consisting of POPULATION_SIZE PCBIndividuals
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            PCBIndividual individual = new PCBIndividual(config);
            individual.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
            individual.calculateFitness();
            population.add(individual);
        }

        bestFromPopulations.add(population.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get());

        for (int i = 0; i < Constants.POPULATION_OPERATORS_STOP_CONDITION; i++) {
            List<PCBIndividual> newPopulation = new ArrayList<>();
            while (newPopulation.size() != population.size()) {
                PCBIndividual parent1 = selectionAlgorithm.select(population);
                PCBIndividual parent2 = selectionAlgorithm.select(population);

                PCBIndividual child = crossing.cross(parent1, parent2);
                child = mutation.mutate(child);
                child.calculateFitness();
                newPopulation.add(child);
            }
            bestFromPopulations.add(newPopulation.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get());
            population = newPopulation;
            System.out.println("POPULATION " + i);
            graphRepresentation.maximums.add(newPopulation.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get().getFitness());
            graphRepresentation.minimums.add(newPopulation.stream().max(Comparator.comparing(PCBIndividual::getFitness)).get().getFitness());
            graphRepresentation.avgs.add(newPopulation.stream().mapToDouble(PCBIndividual::getFitness).average().orElse(0.0));
        }
        float timeElapsed = (System.currentTimeMillis() - startTime) / 1000F;
        int index = 0;
        for (PCBIndividual best : bestFromPopulations) {
            best.saveIndividualToFile("population" + index + ".json");
            pythonProcessBuilder.generatePCBImageToFile("population" + index + ".json", "population" + index + ".png");
            index++;
        }

        graphRepresentation.saveIndividualToFile("graph.json");
        pythonProcessBuilder.generatePCBChartImageToFile("graph.json", "graph.png");

        System.out.println("BEST: " + bestFromPopulations.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get().getFitness());
        System.out.println("AVERAGE: " + bestFromPopulations.stream().mapToDouble(PCBIndividual::getFitness).average().getAsDouble());
        System.out.println("WORST: " + bestFromPopulations.stream().max(Comparator.comparing(PCBIndividual::getFitness)).get().getFitness());

        System.out.println("TIME ELAPSED: " + timeElapsed + " seconds");
    }
}
