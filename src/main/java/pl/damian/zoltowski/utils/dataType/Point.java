package pl.damian.zoltowski.utils.dataType;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Point {
    private int x;
    private int y;

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    @Override
    public String toString() { return "(" + this.x + ", " + this.y + ")";}
}
