package pl.damian.zoltowski.crossover;

import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.utils.Constants;

import java.util.Random;

public class MultiPointCrossover implements CrossOverAlgorithm {
    @Override
    public PCBIndividual cross(PCBIndividual firstParent, PCBIndividual secondParent) {
        PCBIndividual child = new PCBIndividual();
        Random r = new Random();

        child.setPcbHeight(firstParent.getPcbHeight());
        child.setPcbWidth(firstParent.getPcbWidth());
        child.setPoints(firstParent.getPoints());
        for(int i = 0; i < firstParent.getPopulation().size(); i++) {
            if ( r.nextDouble() < Constants.CROSS_OVER_PROBABILITY) {
                child.getPopulation().add(firstParent.getPopulation().get(i));
            } else {
                child.getPopulation().add(secondParent.getPopulation().get(i));
            }
        }
        return child;
    }
}
