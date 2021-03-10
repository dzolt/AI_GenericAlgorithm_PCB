package pl.damian.zoltowski;

import lombok.Data;
import pl.damian.zoltowski.utils.Constants;
import pl.damian.zoltowski.utils.Tuple;

import java.util.ArrayList;
import java.util.List;


@Data
public class PCBIndividual {
    private int pcbWidth;
    private int pcbHeight;
    private List<Tuple<Point, Point>> points;
    private List<Path> population;
    private double fitness = 0;

    public PCBIndividual(Config config) {
        this.pcbWidth = config.getPcb_width();
        this.pcbHeight = config.getPcb_height();
        this.points = config.getPoints();
        this.population = new ArrayList<>();
    }

    public void initPopulation(int maxStepsToFindAPath) {
        //for each pair of points that is present on the board
        for(int i = 0; i < points.size(); i++) {
            population.add(new Path()
                    .generateRandomPath(points.get(i), maxStepsToFindAPath)
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
        int sumOfInteresctions = 0;
        for(Path path1: population){
            for (Path path2: population) {
                if(path1 != path2) {
                   for(Point point: path1.getPoints()) {
                       if(path2.getPoints().contains(point)) {
                           sumOfInteresctions++;
                       }
                   }
                }
            }
        }
        return sumOfInteresctions;
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
