package pl.damian.zoltowski.utils;

import pl.damian.zoltowski.Config;
import pl.damian.zoltowski.Point;

import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static pl.damian.zoltowski.utils.Constants.DATA_SPLITTER;

public class Util {

    public static Config readConfigFromFile(String fileName) {
        Tuple<Integer, Integer> dimensions;
        List<Tuple<Point, Point>> points = new ArrayList<>();
        List<String> lines;

        try {
            URL resource = Util.class.getClassLoader().getResource("data/" + fileName);
            if (resource == null) {
                throw new NoSuchFileException(fileName);
            }
            URI uri = resource.toURI();
            lines = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
            if (lines.size() < 2) {
                throw new InvalidDataInFileException("Make sure that file contains PCB dimensions and at least one Point to connect");
            }
            dimensions = readDimensionsFromLine(lines.get(0));
            for(String line: lines.subList(1, lines.size())) {
                points.add(readPointsFromLine(line));
            }
            return new Config(dimensions.first, dimensions.second, points);
        } catch (Exception | InvalidDataInFileException | InvalidDimensionDataException | InvalidPointsEntryDataException e) {
            e.printStackTrace();
        }
        return new Config();
    }

    public static Tuple<Integer, Integer> readDimensionsFromLine(String line) throws InvalidDimensionDataException {
        String[] dims = line.split(DATA_SPLITTER);
        if(dims.length != 2) {
            throw new InvalidDimensionDataException("Invalid dimension parameters specified in file! Expected 2, got: " + dims.length);
        }
        Integer x = Integer.parseInt(dims[0]);
        Integer y = Integer.parseInt(dims[1]);
        return new Tuple<>(x, y);
    }

    public static Tuple<Point, Point> readPointsFromLine(String line) throws InvalidPointsEntryDataException {
        String[] pointsCoordinates = line.split(DATA_SPLITTER);
        if(pointsCoordinates.length != 4) {
            throw new InvalidPointsEntryDataException("Invalid number of points in line. Expected 2, got: " + pointsCoordinates.length);
        }
        int x1 = Integer.parseInt(pointsCoordinates[0]);
        int y1 = Integer.parseInt(pointsCoordinates[1]);
        int x2 = Integer.parseInt(pointsCoordinates[2]);
        int y2 = Integer.parseInt(pointsCoordinates[3]);
        Point startingPoint = new Point(x1, y1);
        Point endingPoint = new Point(x2, y2);

        return new Tuple<>(startingPoint, endingPoint);
    }
}
