package pl.damian.zoltowski.pcb;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import lombok.Data;
import pl.damian.zoltowski.utils.Config;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class PCBIndividual implements Cloneable{
    private int pcbWidth;
    private int pcbHeight;
    private List<Tuple<Point, Point>> points;
    private List<Path> population;
    private double fitness = 0;
    private int inters = 0;

    public PCBIndividual(Config config) {
        this.pcbWidth = config.getPcb_width();
        this.pcbHeight = config.getPcb_height();
        this.points = config.getPoints();
        this.population = new ArrayList<>();
    }

    public PCBIndividual() {
        this.pcbWidth = -1;
        this.pcbHeight = -1;
        this.points = new ArrayList<>();
        this.population = new ArrayList<>();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        PCBIndividual clone = (PCBIndividual)super.clone();
        clone.points = new ArrayList<>();
        clone.population = new ArrayList<>();
        for(Tuple<Point, Point> p: this.points) {
            clone.points.add((Tuple)p.clone());
        }
        for(Path p: population) {
            clone.population.add((Path)p.clone());
        }
        return clone;
    }

    public void initPopulation(int maxStepsToFindAPath) throws CloneNotSupportedException {
        //for each pair of points that is present on the board
        Tuple<Integer, Integer> dims = new Tuple<>(pcbWidth, pcbHeight);
        for (int i = 0; i < points.size(); i++) {
            population.add(new Path()
                             .generateRandomPath(points.get(i), maxStepsToFindAPath, dims)
            );
        }
    }

    public void calculateFitness() {
        int sumOfLength = calculateLength();
        Tuple<Integer, Integer> outOfBoardData = calculateOutOfBoard();
        int segmentsOutOfBoard = outOfBoardData.getFirst();
        int lengthOfPathsOutOfBoard = outOfBoardData.getSecond();
        int calculateIntersections = calculateIntersections();

        this.fitness = sumOfLength * Constants.LENGTH_PENALTY +
                       segmentsOutOfBoard * Constants.SEGMENTS_OUT_OF_BOARD_PENALTY +
                       lengthOfPathsOutOfBoard * Constants.LENGTH_OUT_OF_BOARD_PENALTY +
                       calculateIntersections * Constants.INTERSECTION_PENALTY;
    }

    private int calculateIntersections() {
        int sumOfIntersections = 0;
        for (Path path1 : population) {
            for (Path path2 : population) {
                if (path1 != path2) {
                    for (Point point : path1.getPoints()) {
                        if (path2.getPoints().contains(point)) {
                            sumOfIntersections++;
                        }
                    }
                } else {
                    Map<Point, Integer> potentialOwnIntersections = new HashMap<>();
                    for (Point ownPoint : path1.getPoints()) {
                        if (potentialOwnIntersections.containsKey(ownPoint)) {
                            sumOfIntersections++;
                            potentialOwnIntersections.put(ownPoint, potentialOwnIntersections.get(ownPoint) + 1);
                        } else {
                            potentialOwnIntersections.put(ownPoint, 0);
                        }
                    }
                }
            }
        }
        this.inters = sumOfIntersections;
        return sumOfIntersections;
    }

    private int calculateSumOfPathsOutOfBoard() {
        int sumOfPaths = 0;
        for (Path path : population) {
            for (Point point : path.getPoints()) {
                if (isOutOfBoard(point)) {
                    sumOfPaths++;
                    continue;
                }
            }
        }
        return sumOfPaths;
    }

    public int calculateLength() {
        return population.stream().mapToInt(Path::getLength).sum();
    }

    private void jsonify(String fileName) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonSerializer<PCBIndividual> serializer = new PCBJsonSerializer();
        gsonBuilder.registerTypeAdapter(PCBIndividual.class, serializer);
        String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\damian\\zoltowski\\visualization\\data\\" + fileName;
        try (Writer writer = new FileWriter(filePath)) {
            gsonBuilder.create().toJson(this, writer);
        }
    }

    public void saveIndividualToFile(String fileName) {
        try {
            this.jsonify(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tuple<Integer, Integer> calculateOutOfBoard() {
        //first item is number of segments out of board and second item is sum of segments length out of board
        Tuple<Integer, Integer> outOfBoard = new Tuple<>(0, 0);
        Point currentPosition;


        for (int i = 0; i < points.size(); i++) {
            currentPosition = new Point(points.get(i).getFirst());
            Point end;
            boolean isEndOutOfBoard;
            boolean isStartOutOfBoard;
            for (Tuple<Direction, Integer> segment : population.get(i).getSegments()) {
                end = calculatePosition(currentPosition, segment.getFirst(), segment.getSecond());
                isEndOutOfBoard = isOutOfBoard(end);
                isStartOutOfBoard = isOutOfBoard(currentPosition);
                //if both points are out of board number of segments out of board is increased by one and the total
                //length is increased by the amount of distance that particular segment has

                //if start point is out of board whilst end point isn't then amount of segments is increased by one and
                //total length is increased by the distance between start point and board frame

                //if end point is out of board whilst start point isn't then amount of segments is increased by one and
                //total length is increased by the distance between end point and board frame
                if (isStartOutOfBoard && isEndOutOfBoard) {
                    outOfBoard.setFirst(outOfBoard.getFirst() + 1);
                    outOfBoard.setSecond(outOfBoard.getSecond() + segment.getSecond());
                } else if (!isEndOutOfBoard && isStartOutOfBoard) {
                    outOfBoard.setFirst(outOfBoard.getFirst() + 1);
                    outOfBoard.setSecond(outOfBoard.getSecond() + calculateDistanceToTheEdge(end, segment, "START"));
                } else if (!isStartOutOfBoard && isEndOutOfBoard) {
                    outOfBoard.setFirst(outOfBoard.getFirst() + 1);
                    outOfBoard.setSecond(outOfBoard.getSecond() + calculateDistanceToTheEdge(currentPosition, segment, "END"));
                }
            }
        }
        return outOfBoard;
    }

    private Integer calculateDistanceToTheEdge(Point position, Tuple<Direction, Integer> segment, String whichPointOutOfBoard) {
        Direction direction = segment.getFirst();
        Integer distance = segment.getSecond();
        if (whichPointOutOfBoard == "START") {
            if (direction == Direction.UP) {
                return distance - position.getY();
            } else if (direction == Direction.DOWN) {
                return distance - (pcbHeight - position.getY());
            } else if (direction == Direction.RIGHT) {
                return distance - position.getX();
            } else {
                return distance - (pcbWidth - position.getX());
            }
        } else {
            if (direction == Direction.UP) {
                return distance - (pcbHeight - position.getY());
            } else if (direction == Direction.DOWN) {
                return distance - position.getY();
            } else if (direction == Direction.RIGHT) {
                return distance - (pcbWidth - position.getX());
            } else {
                return distance - position.getX();
            }
        }
    }

    private Point calculatePosition(Point currentPosition, Direction currentDirection, int distance) {
        if (currentDirection == Direction.UP) {
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

    private boolean isOutOfBoard(Point point) {
        return point.getX() > pcbWidth || point.getX() < 0
               || point.getY() > pcbHeight || point.getY() < 0;
    }

//    public PCBIndividual clone() {
//        PCBIndividual clone = new PCBIndividual();
//        clone.pcbWidth = this.pcbWidth;
//        clone.pcbHeight = this.pcbHeight;
//        clone.points = ;
//        clone.population;
//        clone.fitness = this.fitness;
//        clone.inters = this.inters;
//    }
}
