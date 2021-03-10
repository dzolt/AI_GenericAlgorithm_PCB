package pl.damian.zoltowski;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.Direction;
import pl.damian.zoltowski.utils.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static pl.damian.zoltowski.utils.Constants.PROBABILITY_OF_DIRECTION_INCREASE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Individual {
    private List<Tuple<Direction, Integer>> segments;

    public Individual generateRandomIndividual(Tuple<Point, Point> positions, int maxSteps) {
        Point start = positions.getFirst();
        Point end = positions.getSecond();
        List<Tuple<Direction, Integer>> path = new ArrayList<>();

        Point currentPosition = start;
        Direction previousDirection = Direction.UNKNOWN;
        Direction currentDirection = Direction.UNKNOWN;

        int iteration = 0;
        while (currentPosition != end) {
            if (iteration >= maxSteps) {
                iteration = 0;
                path.clear();
                currentPosition = start;
            }

            currentDirection = determineDirection(currentPosition, end, previousDirection);
            int distance = determineDistance(currentPosition, end, currentDirection);
            path.add(new Tuple<>(currentDirection, distance));

            previousDirection = currentDirection;
            iteration++;
        }

        return new Individual(path);
    }

    private int determineDistance(Point currentPosition, Point end, Direction currentDirection) {
        //determine distance to travel per segment
        //think of implementing method based on PCB size so that you always achieve great results
        Random r = new Random();
        if(currentDirection == Direction.DOWN || currentDirection == Direction.UP) {
//            return r.nextInt(Math.random() * Math.abs(currentPosition.getY() - end.getY()) - 1) + 1;
            return (int) (Math.random() * (Math.abs(currentPosition.getY() - end.getY()) - 1)) + 1;
        } else {
//            return r.nextInt(Math.abs(currentPosition.getX() - end.getX()) - 1) + 1;
            return (int) (Math.random() * (Math.abs(currentPosition.getX() - end.getX()) - 1)) + 1;
        }
    }

    private Direction determineDirection(Point currentPosition, Point endPosition, Direction previousDirection) {
        Tuple<Direction, Integer> upProbability = new Tuple<>(Direction.UP, 1);
        Tuple<Direction, Integer> downProbability = new Tuple<>(Direction.DOWN, 1);
        Tuple<Direction, Integer> leftProbability = new Tuple<>(Direction.LEFT, 1);
        Tuple<Direction, Integer> rightProbability = new Tuple<>(Direction.RIGHT, 1);
        List<Tuple<Direction, Integer>> listOfProbabilities = new ArrayList<>(List.of(upProbability, downProbability, leftProbability, rightProbability));

        if (previousDirection != Direction.UNKNOWN) {
            if (endPosition.getX() - currentPosition.getX() > 0) {
                rightProbability.second += PROBABILITY_OF_DIRECTION_INCREASE;
            } else {
                leftProbability.second += PROBABILITY_OF_DIRECTION_INCREASE;
            }

            if (endPosition.getY() - currentPosition.getY() > 0) {
                upProbability.second += PROBABILITY_OF_DIRECTION_INCREASE;
            } else {
                downProbability.second += PROBABILITY_OF_DIRECTION_INCREASE;
            }

            if(previousDirection == Direction.UP) {
                downProbability.second = 0;
            } else if (previousDirection == Direction.DOWN) {
                upProbability.second = 0;
            } else if (previousDirection == Direction.LEFT) {
                rightProbability.second = 0;
            } else {
                leftProbability.second = 0;
            }
        }
        return chooseDirection(listOfProbabilities);
    }

    private Direction chooseDirection(List<Tuple<Direction, Integer>> listOfProbabilities) {
        Random r = new Random();
        listOfProbabilities.forEach(el -> el.second *= r.nextInt(15));
        Tuple<Direction, Integer> chosenDirection = listOfProbabilities.get(0);
        for(Tuple<Direction, Integer> prob: listOfProbabilities) {
            if(prob.second > chosenDirection.second) {
                chosenDirection = prob;
            }
        }

        return chosenDirection.getFirst();
    }


}
