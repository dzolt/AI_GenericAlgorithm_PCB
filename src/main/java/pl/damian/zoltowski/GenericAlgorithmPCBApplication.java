package pl.damian.zoltowski;

import pl.damian.zoltowski.crossover.CrossOverAlgorithm;
import pl.damian.zoltowski.crossover.MultiPointCrossover;
import pl.damian.zoltowski.mutation.MutationAlgorithm;
import pl.damian.zoltowski.mutation.SimpleMutation;
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
        Config config = new Config().readConfigFromFile("zad0.txt");
        System.out.println(config);
        List<PCBIndividual> population = new ArrayList<>();
        SelectionAlgorithm selectionAlgorithm = new RouletteSelection();
        PythonProcessBuilder pythonProcessBuilder = new PythonProcessBuilder();
        CrossOverAlgorithm crossing = new MultiPointCrossover();
        MutationAlgorithm mutation = new SimpleMutation();
        List<PCBIndividual> bestFromPopulations = new ArrayList<>();

        // create population consisting of POPULATION_SIZE PCBIndividuals
        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            PCBIndividual individual = new PCBIndividual(config);
            individual.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
            individual.calculateFitness();
            population.add(individual);
        }

        bestFromPopulations.add(population.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get());

        for (int i = 0; i < Constants.POPULATION_OPERATORS_STOP_CONDITION; i++) {
           List<PCBIndividual> newPopulation = new ArrayList<>();
           while(newPopulation.size() != population.size()) {
               PCBIndividual parent1 = selectionAlgorithm.select(population);
               PCBIndividual parent2 = selectionAlgorithm.select(population);;
               PCBIndividual child = crossing.cross(parent1, parent2);
               child = mutation.mutate(child);
               child.calculateFitness();
               newPopulation.add(child);
           }
           bestFromPopulations.add(newPopulation.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get());
           population = newPopulation;
            System.out.println("POPULATION " + i);
        }

        int index = 0;
        for(PCBIndividual best: bestFromPopulations) {
//            System.out.println(best);
            best.saveIndividualToFile("population" + index + ".json");
            pythonProcessBuilder.generatePCBImageToFile("population" + index + ".json", "population" + index + ".png");
            index++;
        }


//        System.out.println("BEST POPULATION: ");
//        PCBIndividual best = population.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get();
//        System.out.println(best + " ---------> SCORED: " + best.getFitness() + "-->>>> " + best.getInters());
//        best.saveIndividualToFile("best.json");
//        pythonProcessBuilder.generatePCBImageToFile("best.json", "result.png");

//        PCBIndividual parent1 = new PCBIndividual(config);
//        PCBIndividual parent2 = new PCBIndividual(config);
//        PCBIndividual child;
//        parent1.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
//        parent1.calculateFitness();
//        parent2.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
//        parent2.calculateFitness();
//        child = crossing.cross(parent1, parent2);
//        System.out.println("PARENT1: " + parent1);
//        System.out.println("PARENT2: " + parent2);
//        System.out.println("CHILD: " + child);
//        parent1.saveIndividualToFile("parent1.json");
//        parent2.saveIndividualToFile("parent2.json");
//        child.saveIndividualToFile("child.json");
//        pythonProcessBuilder.generatePCBImageToFile("parent1.json", "parent1.png");
//        pythonProcessBuilder.generatePCBImageToFile("parent2.json", "parent2.png");
//        pythonProcessBuilder.generatePCBImageToFile("child.json", "child.png");
//        PCBIndividual mutant = mutation.mutate(child);
//        mutant.saveIndividualToFile("mutant.json");
//        pythonProcessBuilder.generatePCBImageToFile("mutant.json", "mutant.png");
    }
}
