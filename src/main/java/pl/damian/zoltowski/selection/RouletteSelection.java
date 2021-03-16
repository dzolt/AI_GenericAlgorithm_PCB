package pl.damian.zoltowski.selection;

import pl.damian.zoltowski.pcb.PCBIndividual;

import java.util.List;

public class RouletteSelection implements SelectionAlgorithm{
    @Override
    public PCBIndividual select(List<PCBIndividual> population) {
        double sumOfFitness = population.stream().mapToDouble(ind -> 1 / Math.pow(ind.getFitness(), 10)).sum();
        double randomValue = Math.random() * sumOfFitness;
        double currentSum = 0.0;

        for(PCBIndividual ind: population) {
            currentSum += 1 / Math.pow(ind.getFitness(), 10);
            if (currentSum > randomValue) {
                return ind;
            }
        }
        return null;
    }
}
