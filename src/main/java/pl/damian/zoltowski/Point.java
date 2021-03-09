package pl.damian.zoltowski;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Point {
    private int x;
    private int y;

    @Override
    public String toString() { return "(" + this.x + ", " + this.y + ")";}
}
