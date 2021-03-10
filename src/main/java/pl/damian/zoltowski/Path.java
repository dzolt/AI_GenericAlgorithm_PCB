package pl.damian.zoltowski;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damian.zoltowski.utils.Direction;
import pl.damian.zoltowski.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static pl.damian.zoltowski.utils.Constants.PROBABILITY_OF_DIRECTION_INCREASE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    private List<Tuple<Direction, Integer>> segments;
    private int length;
    private List<Point> points;


    public Path generateRandomPath(Tuple<Point, Point> positions, int maxSteps, Tuple<Integer, Integer> dims) {
        Point start = positions.getFirst();
        Point end = positions.getSecond();
        List<Tuple<Direction, Integer>> path = new ArrayList<>();
        List<Point> points = new ArrayList<> ();
        int length = 0;

        Point currentPosition = new Point(start.getX(), start.getY());
        Direction previousDirection = Direction.UNKNOWN;
        Direction currentDirection = Direction.UNKNOWN;
        Tuple<Point, List<Point>> helper;
        int iteration = 0;
        while (!currentPosition.equals(end)) {
            if (iteration >= maxSteps) {
                iteration = 0;
                path.clear();
                currentPosition.setX(start.getX());
                currentPosition.setY(start.getY());
                previousDirection = Direction.UNKNOWN;
                length = 0;
                points.clear();
                points.add(start);
            }

            currentDirection = determineDirection(currentPosition, end, previousDirection);
            int distance = determineDistance(currentPosition, end, currentDirection, dims);
            path.add(new Tuple<>(currentDirection, distance));
            helper = calculateNewPositionAndPoints(currentPosition, currentDirection, distance);
            currentPosition = helper.getFirst();
//            currentPosition = calculatePosition(currentPosition, currentDirection, distance);
//            points.add(new Point(currentPosition.getX(), currentPosition.getY()));
            points.addAll(helper.getSecond());
            previousDirection = currentDirection;
            iteration++;
            length += distance;
        }
        points.add(end);
        return new Path(path, length, points);
    }

    public List<Point> getPointsFromPath(Point start, Point end, List<Tuple<Direction, Integer>> path) {
        List<Point> points = new ArrayList<>();
        points.add(start);

        return points;
    }

    private Tuple<Point, List<Point>> calculateNewPositionAndPoints(Point currentPosition, Direction currentDirection, int distance) {
        List<Point> points = new ArrayList<>();
        if(currentDirection == Direction.UP) {
            for(int i = 0; i < distance; i++) {
                points.add(new Point(currentPosition.getX(), currentPosition.getY() + i));
            }
            currentPosition.setY(currentPosition.getY() + distance);
        } else if (currentDirection == Direction.DOWN) {
            for(int i = 0; i < distance; i++) {
                points.add(new Point(currentPosition.getX(), currentPosition.getY() - i));
            }
            currentPosition.setY(currentPosition.getY() - distance);
        } else if (currentDirection == Direction.LEFT) {
            for(int i = 0; i < distance; i++) {
                points.add(new Point(currentPosition.getX() - i, currentPosition.getY()));
            }
            currentPosition.setX(currentPosition.getX() - distance);
        } else {
            for(int i = 0; i < distance; i++) {
                points.add(new Point(currentPosition.getX() + i, currentPosition.getY()));
            }
            currentPosition.setX(currentPosition.getX() + distance);
        }

        return new Tuple(currentPosition, points);
    }

    private Point calculatePosition(Point currentPosition, Direction currentDirection, int distance) {
        if(currentDirection == Direction.UP) {
            currentPosition.setY(currentPosition.getY() + distance);
        } else if (currentDirection == Direction.DOWN) {
            currentPosition.setY(currentPosition.getY() - distance);
        } else if (currentDirection == Direction.LEFT) {
            currentPosition.setX(currentPosition.getX() - distance);
        } else {
            currentPosition.setX(currentPosition.getX() + distance);
        }

        return currentPosition;
    }

    private int determineDistance(Point currentPosition, Point end, Direction currentDirection, Tuple<Integer, Integer> dims) {
        //determine distance to travel per segment
        //think of implementing method based on PCB size so that you always achieve great results
        Random r = new Random();
        if(currentDirection == Direction.DOWN || currentDirection == Direction.UP) {
//            return r.nextInt(Math.random() * Math.abs(currentPosition.getY() - end.getY()) - 1) + 1;
//            return (int) (Math.random() * (Math.abs(currentPosition.getY() - end.getY()) - 1)) + 1;
            return (int) (Math.random() * (Math.abs(currentPosition.getY() - dims.second))) + 1;
        } else {
//            return r.nextInt(Math.abs(currentPosition.getX() - end.getX()) - 1) + 1;
//            return (int) (Math.random() * (Math.abs(currentPosition.getX() - end.getX()) - 1)) + 1;
            return (int) (Math.random() * (Math.abs(currentPosition.getX() - dims.first))) + 1;
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
