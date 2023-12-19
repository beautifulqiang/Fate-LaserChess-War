package flcwGUI.LaserChessGamePlay.chess;
public class ChessTwoWayMirror extends Chess {
    public enum Direction {
        LEFT_TOP, RIGHT_TOP
    }

    public Direction direction;

    public ChessTwoWayMirror(Direction direction, Color color) {
        this.direction = direction;
        this.color = color;
        this.ct = chess_type.TwoWayMirror;
    }

    public void rotate(char d){
        if(direction==Direction.LEFT_TOP){
            direction = Direction.RIGHT_TOP;
        }
        else{
            direction = Direction.LEFT_TOP;
        }
    }
}