package pl.damian.zoltowski.mutation;

import pl.damian.zoltowski.pcb.Direction;
import pl.damian.zoltowski.pcb.PCBIndividual;
import pl.damian.zoltowski.pcb.Path;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.util.Random;

import static pl.damian.zoltowski.utils.Constants.MUTATION_PROBABILITY;

public class MutationA implements MutationAlgorithm {
    @Override
    public PCBIndividual mutate(PCBIndividual individual) throws CloneNotSupportedException {
        Random r = new Random();
        PCBIndividual mutant = (PCBIndividual) individual.clone();
        int index = 0;
        for (Path path : mutant.getPopulation()) {
            if (MUTATION_PROBABILITY > r.nextDouble()) {
                int segmentToMutateIndex = r.nextInt(path.getSegments().size());
                Tuple<Direction, Integer> selectedSegment = path.getSegments().get(segmentToMutateIndex);
                Direction newDirection;
                System.out.println("MUTATION OCCURED");

                if (selectedSegment.getFirst() == Direction.RIGHT || selectedSegment.getFirst() == Direction.LEFT) {
                    newDirection = r.nextDouble() > 0.5 ? Direction.UP : Direction.DOWN;
                } else {
                    newDirection = r.nextDouble() > 0.5 ? Direction.RIGHT : Direction.LEFT;
                }
                // fix left segment
                if (segmentToMutateIndex > 0) {
                    Tuple<Direction, Integer> previousSegment = path.getSegments().get(segmentToMutateIndex - 1);
                    if(previousSegment.getFirst() == newDirection) {
                        previousSegment.setSecond(previousSegment.getSecond() + Constants.MUTATION_MAX_MOVE_SEGMENT);
                    } else {
                        previousSegment.setSecond(previousSegment.getSecond() - Constants.MUTATION_MAX_MOVE_SEGMENT);
                    }
                }
                // fix right segment
                if(segmentToMutateIndex != path.getSegments().size() - 1) {
                    Tuple<Direction, Integer> followingSegment = path.getSegments().get(segmentToMutateIndex + 1);
                    if(followingSegment.getFirst() == newDirection) {
                        followingSegment.setSecond(followingSegment.getSecond() - Constants.MUTATION_MAX_MOVE_SEGMENT);
                    } else {
                        followingSegment.setSecond(followingSegment.getSecond() + Constants.MUTATION_MAX_MOVE_SEGMENT);
                    }
                }
                Tuple<Point, Point> startAndEnd = mutant.getPoints().get(index);
                path.setPoints(path.getPointsFromSegments(startAndEnd.getFirst(), startAndEnd.getSecond(), path.getSegments()));
            }
            index++;
        }
        mutant.calculateFitness();
        return mutant;
    }

}
