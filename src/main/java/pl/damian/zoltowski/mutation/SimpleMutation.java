package pl.damian.zoltowski.mutation;

import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.pcb.Path;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.util.Random;

import static pl.damian.zoltowski.utils.Constants.MUTATION_PROBABILITY;

public class SimpleMutation implements MutationAlgorithm{
    @Override
    public PCBIndividual mutate(PCBIndividual individual) {
        Random r = new Random();
        PCBIndividual mutant = new PCBIndividual(individual);
        Tuple<Integer, Integer> dims = new Tuple<>(individual.getPcbWidth(), individual.getPcbHeight());
        for(int i = 0; i < individual.getPopulation().size(); i++){
            if( r.nextDouble() < MUTATION_PROBABILITY) {
                Path mutatedPath = new Path().generateRandomPath(individual.getPoints().get(i), Constants.MAX_STEPS_INDIVIDUAL_GENERATION, dims);
                mutant.getPopulation().set(i, mutatedPath);
            }
        }
        mutant.calculateFitness();
        return mutant;
    }
}
