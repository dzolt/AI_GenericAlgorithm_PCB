package pl.damian.zoltowski;

import pl.damian.zoltowski.utils.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GenericAlgorithmPCBApplication {

    public static void main(String[] args) {
        Config config = new Config().readConfigFromFile("zad0.txt");
        System.out.println(config);
        List<PCBIndividual> population = new ArrayList<>();
        for(int i = 0; i < Constants.POPULATION_SIZE; i++) {
            PCBIndividual individual = new PCBIndividual(config);
            individual.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
            individual.calculateFitness();
            population.add(individual);
        }
        int index = 0;
        for(PCBIndividual ind: population) {
            System.out.println(ind);
            System.out.println("POPULATION " + index + " ----> " + ind.getFitness() + "Scored");
            index++;
        }

        System.out.println("BEST POPULATION: ");
        PCBIndividual best = population.stream().min(Comparator.comparing(PCBIndividual::getFitness)).get();
        System.out.println(best + " ---------> SCORED: " + best.getFitness());

//        PCBIndividual individual = new PCBIndividual(config);
//        individual.initPopulation(Constants.MAX_STEPS_INDIVIDUAL_GENERATION);
//        individual.calculateFitness();
//        System.out.println(individual);
//        System.out.println(population.size());

    }
}
