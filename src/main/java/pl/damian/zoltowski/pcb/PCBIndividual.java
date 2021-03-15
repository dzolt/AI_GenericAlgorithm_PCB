package pl.damian.zoltowski.pcb;

import lombok.Data;
import pl.damian.zoltowski.utils.Config;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class PCBIndividual {
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

    public void initPopulation(int maxStepsToFindAPath) {
        //for each pair of points that is present on the board
        Tuple<Integer, Integer> dims = new Tuple<>(pcbWidth, pcbHeight);
        for(int i = 0; i < points.size(); i++) {
            population.add(new Path()
                    .generateRandomPath(points.get(i), maxStepsToFindAPath, dims)
            );
        }
    }

    public void calculateFitness() {
        int sumOfLength = calculateLength(population);
        int sumOfPathsOutOfBoard = calculateSumOfPathsOutOfBoard(population);
        int calculateIntersections = calculateIntersections(population);

        this.fitness = sumOfLength * Constants.LENGTH_PENALTY +
               sumOfPathsOutOfBoard * Constants.OUT_OF_BOARD_PENALTY +
               calculateIntersections * Constants.INTERSECTION_PENALTY;
    }

    private int calculateIntersections(List<Path> population) {
        int sumOfIntersections = 0;
        for(Path path1: population){
            for (Path path2: population) {
                if(path1 != path2) {
                   for(Point point: path1.getPoints()) {
                       if(path2.getPoints().contains(point)) {
                           sumOfIntersections++;
                       }
                   }
                } else {
                    Map<Point, Integer> potentialOwnIntersections = new HashMap<>();
                    for(Point ownPoint: path1.getPoints()) {
                        if(potentialOwnIntersections.containsKey(ownPoint)) {
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

    private int calculateSumOfPathsOutOfBoard(List<Path> population) {
        int sumOfPaths = 0;
        for(Path path: population) {
            for(Point point: path.getPoints()) {
                if(point.getX() > pcbWidth || point.getX() < 0 || point.getY() > pcbHeight || point.getY() < 0) {
                    sumOfPaths++;
                    continue;
                }
            }
        }
        return sumOfPaths;
    }

    private int calculateLength(List<Path> population) {
        return population.stream().mapToInt(Path::getLength).sum();
    }

}
