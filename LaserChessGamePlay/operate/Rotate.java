package operate;

public class Rotate extends Operate {
    public enum RotateDirection {
        LEFT, RIGHT
    }

    public int x;
    public int y;
    public RotateDirection direction;

    public Rotate(int x, int y, RotateDirection direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public void print() {
        System.out.println("Rotate Operation:");
        System.out.println("Coordinates: (" + x + ", " + y + ")");
        System.out.println("Direction: " + direction);
    }

}