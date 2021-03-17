package pl.damian.zoltowski;

import pl.damian.zoltowski.crossover.CrossOverAlgorithm;
import pl.damian.zoltowski.crossover.MultiPointCrossover;
import pl.damian.zoltowski.mutation.MutationA;
import pl.damian.zoltowski.mutation.MutationAlgorithm;
import pl.damian.zoltowski.mutation.SimpleMutation;
import pl.damian.zoltowski.pcb.Direction;
import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.pcb.Path;
import pl.damian.zoltowski.selection.RouletteSelection;
import pl.damian.zoltowski.selection.SelectionAlgorithm;
import pl.damian.zoltowski.utils.Config;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.dataType.Tuple;
import pl.damian.zoltowski.visualization.PythonProcessBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GenericAlgorithmPCBApplication {

    public static void main(String[] args) throws CloneNotSupportedException {
        Config config = new Config().readConfigFromFile("zad0.txt");
        System.out.println(config);
        List<PCBIndividual> population = new ArrayList<>();
        SelectionAlgorithm selectionAlgorithm = new RouletteSelection();
        PythonProcessBuilder pythonProcessBuilder = new PythonProcessBuilder();
        CrossOverAlgorithm crossing = new MultiPointCrossover();
        MutationAlgorithm mutation = new MutationA();
        List<PCBIndividual> bestFromPopulations = new ArrayList<>();

//         create population consisting of POPULATION_SIZE PCBIndividuals
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


//            for(int i = 0; i < 10; i++) {
//                PCBIndividual ind = new PCBIndividual(config);
//                ind.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
//                ind.calculateFitness();
//                System.out.println(ind);
//                System.out.println("POP " + i + " FITNESS " + ind.getFitness());
//                ind.saveIndividualToFile("ind" + i + ".json");
//                pythonProcessBuilder.generatePCBImageToFile("ind" + i + ".json", "ind" + i + ".png");
//                ind = mutation.mutate(ind);
//                System.out.println("MUTATED");
//                System.out.println(ind);
//                System.out.println("MUTATED POP " + i + " FITNESS " + ind.getFitness());
//                ind.saveIndividualToFile("mut" + i + ".json");
//                pythonProcessBuilder.generatePCBImageToFile("mut" + i + ".json", "mut" + i + ".png");
//            }
    }
}
