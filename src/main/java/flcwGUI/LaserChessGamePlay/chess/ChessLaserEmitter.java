package flcwGUI.LaserChessGamePlay.chess;
public class ChessLaserEmitter extends Chess {
    public enum Direction {
        LEFT, TOP, RIGHT, BOTTOM
    }
    public Direction direction;

    public ChessLaserEmitter(Direction direction, Color color) {
        this.direction = direction;
        this.color = color;
        this.ct = chess_type.LaserEmitter;
    }
}