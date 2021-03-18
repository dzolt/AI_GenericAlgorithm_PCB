package pl.damian.zoltowski.crossover;

import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.pcb.Path;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.util.ArrayList;
import java.util.Random;

public class MultiPointCrossover implements CrossOverAlgorithm {
    @Override
    public PCBIndividual cross(PCBIndividual firstParent, PCBIndividual secondParent) throws CloneNotSupportedException {
        PCBIndividual child = new PCBIndividual();
        Random r = new Random();
        child.setPopulation(new ArrayList<>());

        child.setPcbHeight(firstParent.getPcbHeight());
        child.setPcbWidth(firstParent.getPcbWidth());
        for(Tuple<Point, Point> p: firstParent.getPoints()){
            child.getPoints().add((Tuple)p.clone());
        }
        for(int i = 0; i < firstParent.getPopulation().size(); i++) {
            if ( r.nextDouble() < Constants.CROSS_OVER_PROBABILITY) {
                child.getPopulation().add((Path) firstParent.getPopulation().get(i).clone());
            } else {
                child.getPopulation().add((Path) secondParent.getPopulation().get(i).clone());
            }
            child.getPopulation().get(i).calculateLength();
        }
        return child;
    }
}
