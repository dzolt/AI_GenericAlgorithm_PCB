package pl.damian.zoltowski;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damian.zoltowski.utils.Tuple;
import pl.damian.zoltowski.utils.Util;

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
