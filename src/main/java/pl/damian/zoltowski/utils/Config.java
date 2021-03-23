package pl.damian.zoltowski.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Config {
    private int pcb_width;
    private int pcb_height;
    private List<Tuple<Point, Point>> points;

    public Config readConfigFromFile(String fileName) {
        return Util.readConfigFromFile(fileName);
    }

    public void generateRandomConfigToFile(int pcbWidth, int pcbHeight, String fileName) {
        String FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\data\\";
        try(PrintWriter out = new PrintWriter(FILE_PATH + fileName)) {
            Random r = new Random();
            String dimensions = pcbWidth + ";" + pcbHeight;
            out.println(dimensions);
            int maxPointsOnBoard = (pcbWidth - 1) * (pcbHeight - 1);
            int minPointsOnBoard = 2;
            List<Point> pointsOnBoard = new ArrayList<Point>();

            //generate number from 2 to maxPointsOnBoard
            // divide by 2 to generate more "human" results
            int pointsToGenerate = (r.nextInt(maxPointsOnBoard - minPointsOnBoard) + minPointsOnBoard) / 2;
            if(pointsToGenerate % 2 != 0) {
                pointsToGenerate++;
            }
            if(pointsToGenerate > maxPointsOnBoard) {
                if(pointsToGenerate % 2 != 0) {
                    pointsToGenerate--;
                } else {
                    pointsToGenerate -= 2;
                }
            }
            String pointLine = "";
            System.out.println("POINTS TO GENERATE: " + pointsToGenerate);
            for(int i = 1; i <= pointsToGenerate; i++) {
                int randomX = r.nextInt((pcbWidth - 1) - 1) + 1;
                int randomY = r.nextInt((pcbHeight - 1) - 1) + 1;
                boolean pointFoundFlag;
                Point newPoint = new Point(randomX, randomY);
                if(checkIfContains(pointsOnBoard, newPoint)) {
                    i--;
                    pointFoundFlag = false;
                } else {
                    pointsOnBoard.add(newPoint);
                    pointLine += randomX + ";" + randomY;
                    pointFoundFlag = true;
                }
                if(i % 2 == 0 && pointFoundFlag) {
                    out.println(pointLine);
                    pointLine = "";
                } else if(pointFoundFlag) {
                    pointLine += ";";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfContains(List<Point> listOfPoints, Point point){
        for(Point p: listOfPoints) {
            if(p.isSame(point)) {
                return true;
            }
        }
        return false;
    }
}
