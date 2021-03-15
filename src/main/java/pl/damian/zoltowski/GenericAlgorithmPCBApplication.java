package pl.damian.zoltowski;

import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.selection.RouletteSelection;
import pl.damian.zoltowski.selection.SelectionAlgorithm;
import pl.damian.zoltowski.utils.Config;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.visualization.PythonProcessBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GenericAlgorithmPCBApplication {

    public static void main(String[] args) {
        Config config = new Config().readConfigFromFile("zad1.txt");
        System.out.println(config);
        List<PCBIndividual> population = new ArrayList<>();
        SelectionAlgorithm selectionAlgorithm = new RouletteSelection();
        PythonProcessBuilder pythonProcessBuilder = new PythonProcessBuilder();

        // create population consisting of POPULATION_SIZE PCBIndividuals
        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            PCBIndividual individual = new PCBIndividual(config);
            individual.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
            individual.calculateFitness();
            population.add(individual);
        }

        for (int i = 0; i < Constants.POPULATION_OPERATORS_STOP_CONDITION; i++) {

            System.out.println(selectionAlgorithm.select(population).getFitness());
        }

        System.out.println("BEST POPULATION: ");
        PCBIndividual best = population.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get();
        System.out.println(best + " ---------> SCORED: " + best.getFitness() + "-->>>> " + best.getInters());
        best.saveIndividualToFile("best.json");
        pythonProcessBuilder.generatePCBImageToFile("best.json", "result.png");
    }
}
