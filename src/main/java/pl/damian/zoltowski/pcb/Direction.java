package pl.damian.zoltowski.pcb;

public enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    UNKNOWN;

    public Direction getOpposite() {
        if(this == LEFT) {
            return RIGHT;
        } else if(this == RIGHT) {
            return LEFT;
        } else if(this == DOWN){
            return UP;
        } else {
            return DOWN;
        }
    }
}
