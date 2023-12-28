package flcwGUI.LaserChessGamePlay.chess;

public class Chess {
    public int getrotate() {
        return -1;
    }

    public enum Color {
        RED, BLUE
    }

    public Color color;
    chess_type ct = chess_type.NULL;

    public enum chess_type {
        King, LaserEmitter, OneWayMirror,
        Shield, TwoWayMirror, NULL
    }

    public void rotate(char d){}//pass

    public chess_type show_type() {
        return ct;
    }
}
