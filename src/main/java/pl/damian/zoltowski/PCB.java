package pl.damian.zoltowski;

import pl.damian.zoltowski.utils.Tuple;

import java.util.ArrayList;
import java.util.List;

public class PCB {
    private int pcbWidth;
    private int pcbHeight;
    private List<Tuple<Point, Point>> points;
    private List<Individual> population;

    public PCB(Config config) {
        this.pcbWidth = config.getPcb_width();
        this.pcbHeight = config.getPcb_height();
        this.points = config.getPoints();
        this.population = new ArrayList<>();
    }

    public void initPopulation(int x) {

    }

}
