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
        //PCBIndividual mutant = (PCBIndividual) individual.clone();
        int index = 0;
        for (Path path : individual.getPopulation()) {
            if (MUTATION_PROBABILITY > r.nextDouble() && path.getSegments().size() > 1) {
                int segmentToMutateIndex = r.nextInt(path.getSegments().size());
                Tuple<Direction, Integer> selectedSegment = path.getSegments().get(segmentToMutateIndex);
                Direction newDirection;
                boolean wasPreviousSegmentSameDirection = false;

                if (selectedSegment.getFirst() == Direction.RIGHT || selectedSegment.getFirst() == Direction.LEFT) {
                    newDirection = r.nextDouble() > 0.5 ? Direction.UP : Direction.DOWN;
                } else {
                    newDirection = r.nextDouble() > 0.5 ? Direction.RIGHT : Direction.LEFT;
                }

                if(segmentToMutateIndex == 0) {
                    Tuple<Direction, Integer> followingSegment = path.getSegments().get(segmentToMutateIndex + 1);
                    path.getSegments().add(segmentToMutateIndex, new Tuple(newDirection, Constants.MUTATION_MAX_MOVE_SEGMENT));
                    if(selectedSegment.getFirst() == followingSegment.getFirst()) {
                        path.getSegments().add(segmentToMutateIndex + 2, new Tuple<>(newDirection.getOpposite(), Constants.MUTATION_MAX_MOVE_SEGMENT));
                    }else {
                        if(followingSegment.getFirst() == newDirection) {
                            followingSegment.setSecond(followingSegment.getSecond() - Constants.MUTATION_MAX_MOVE_SEGMENT);
                            if(followingSegment.getSecond() == 0) {
                                path.getSegments().remove(followingSegment);
                                segmentToMutateIndex--;
                            }
                        } else {
                            followingSegment.setSecond(followingSegment.getSecond() + Constants.MUTATION_MAX_MOVE_SEGMENT);
                        }
                    }
                }
                if(segmentToMutateIndex == path.getSegments().size() - 1){
                    Tuple<Direction, Integer> previousSegment = path.getSegments().get(segmentToMutateIndex - 1);
                    path.getSegments().add(new Tuple(newDirection.getOpposite(), Constants.MUTATION_MAX_MOVE_SEGMENT));
                    if(selectedSegment.getFirst() == previousSegment.getFirst()) {
                        path.getSegments().add(segmentToMutateIndex, new Tuple(newDirection, Constants.MUTATION_MAX_MOVE_SEGMENT));
                    } else {
                        if(previousSegment.getFirst() == newDirection){
                            previousSegment.setSecond(previousSegment.getSecond() + Constants.MUTATION_MAX_MOVE_SEGMENT);
                        } else {
                            previousSegment.setSecond(previousSegment.getSecond() - Constants.MUTATION_MAX_MOVE_SEGMENT);
                            if(previousSegment.getSecond() == 0) {
                                path.getSegments().remove(previousSegment);
                                segmentToMutateIndex--;
                            }
                        }
                    }
                }

                // fix left segment
                if (segmentToMutateIndex > 0) {
                    Tuple<Direction, Integer> previousSegment = path.getSegments().get(segmentToMutateIndex - 1);
                    //check if the previous segment is the same direction as selected eg. UP 2 UP 2
                    if (selectedSegment.getFirst() == previousSegment.getFirst()) {
                        path.getSegments().add(segmentToMutateIndex, new Tuple<>(newDirection, Constants.MUTATION_MAX_MOVE_SEGMENT));
                        wasPreviousSegmentSameDirection = true;
                    } else {
                        if (previousSegment.getFirst() == newDirection) {
                            previousSegment.setSecond(previousSegment.getSecond() + Constants.MUTATION_MAX_MOVE_SEGMENT);
                        } else {
                            previousSegment.setSecond(previousSegment.getSecond() - Constants.MUTATION_MAX_MOVE_SEGMENT);
                            if(previousSegment.getSecond() == 0) {
                                path.getSegments().remove(previousSegment);
                                segmentToMutateIndex--;
                            }
                        }
                    }
                }
                // fix right segment
                if (segmentToMutateIndex != path.getSegments().size() - 1 && path.getSegments().get(path.getSegments().size() - 1) != selectedSegment ) {
                    int followingSegmentIndex = wasPreviousSegmentSameDirection ? segmentToMutateIndex + 2 : segmentToMutateIndex + 1;
                    Tuple<Direction, Integer> followingSegment = path.getSegments().get(followingSegmentIndex);

                    // check if following segment is the same direction as selected eg. UP 2 UP 2
                    if (selectedSegment.getFirst() == followingSegment.getFirst()) {
                            path.getSegments().add(followingSegmentIndex, new Tuple<>(newDirection.getOpposite(), Constants.MUTATION_MAX_MOVE_SEGMENT));
                    } else {
                        if (followingSegment.getFirst() == newDirection) {
                            followingSegment.setSecond(followingSegment.getSecond() - Constants.MUTATION_MAX_MOVE_SEGMENT);
                            if(followingSegment.getSecond() == 0) {
                                path.getSegments().remove(followingSegment);
                                segmentToMutateIndex--;
                            }
                        } else {
                            followingSegment.setSecond(followingSegment.getSecond() + Constants.MUTATION_MAX_MOVE_SEGMENT);
                        }
                    }
                }
                Tuple<Point, Point> startAndEnd = individual.getPoints().get(index);
                path.setPoints(path.getPointsFromSegments((Point) startAndEnd.getFirst().clone(), (Point) startAndEnd.getSecond().clone(), path.getSegments()));
            }
            index++;
        }
        individual.calculateFitness();
        return individual;
    }
}
