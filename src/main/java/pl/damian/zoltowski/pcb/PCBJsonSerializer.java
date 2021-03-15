package pl.damian.zoltowski.pcb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PCBJsonSerializer implements JsonSerializer<PCBIndividual> {


    @Override
    public JsonElement serialize(PCBIndividual src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonPCB = new JsonObject();
        JsonArray board = new JsonArray();
        JsonArray points = new JsonArray();
        JsonArray paths = new JsonArray();
        //serialize board dimensions
        board.add(src.getPcbWidth());
        board.add(src.getPcbHeight());

        //serialize points present on board
        for(Tuple<Point,Point> p: src.getPoints()) {
            JsonArray singleStartPoint = new JsonArray();
            JsonArray singleEndPoint = new JsonArray();
            singleStartPoint.add(p.getFirst().getX());
            singleStartPoint.add(p.getFirst().getY());
            singleEndPoint.add(p.getSecond().getX());
            singleEndPoint.add(p.getSecond().getY());
            points.add(singleStartPoint);
            points.add(singleEndPoint);
        }

        for(Path singlePath: src.getPopulation()) {
            JsonArray pointsInPath = new JsonArray();
            for(Point p: singlePath.getPoints()) {
                JsonArray point = new JsonArray();
                point.add(p.getX());
                point.add(p.getY());
                pointsInPath.add(point);
            }
            paths.add(pointsInPath);
        }

        jsonPCB.add("board", board);
        if (src.getFitness() == 0) {
            src.calculateFitness();
        }
        jsonPCB.addProperty("fitness", src.getFitness());
        jsonPCB.add("points", points);
        jsonPCB.add("paths", paths);
        return jsonPCB;
    }
}
