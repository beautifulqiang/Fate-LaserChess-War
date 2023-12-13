package operate;

public class Move extends Operate {
    public int startX;
    public int startY;
    public int endX;
    public int endY;

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public void print() {
        System.out.println("Move Operation:");
        System.out.println("Start Coordinates: (" + startX + ", " + startY + ")");
        System.out.println("End Coordinates: (" + endX + ", " + endY + ")");
    }
}