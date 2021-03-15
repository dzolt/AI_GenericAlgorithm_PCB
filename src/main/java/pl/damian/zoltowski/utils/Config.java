package pl.damian.zoltowski.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damian.zoltowski.utils.dataType.Point;
import pl.damian.zoltowski.utils.dataType.Tuple;

import java.util.List;

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
}
