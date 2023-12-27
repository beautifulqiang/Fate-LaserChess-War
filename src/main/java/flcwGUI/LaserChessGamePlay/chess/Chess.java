package flcwGUI.LaserChessGamePlay.chess;

import flcwGUI.LaserChessGamePlay.operate.Operate;

import java.util.List;

public class Chess {
    public Color color;
    chess_type ct = chess_type.NULL;

    public int getrotate() {
        return -1;
    }

    public void rotate(char d) {
    }//pass

    public chess_type show_type() {
        return ct;
    }

    public enum Color {
        RED, BLUE
    }

    public enum chess_type {
        King, LaserEmitter, OneWayMirror,
        Shield, TwoWayMirror, NULL
    }
}
