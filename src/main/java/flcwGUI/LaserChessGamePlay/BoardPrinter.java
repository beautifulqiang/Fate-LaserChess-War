package flcwGUI.LaserChessGamePlay;

import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.*;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

// 注意，本类并未在GUI开发中使用，本类更多的作为项目底层开发时的测试手段

public class BoardPrinter {

    public static void printBoard(Chess[][] chessboard, Background[][] backgroundBoard, boolean[][] path) {
        for (int i = 0; i < chessboard.length; i++) {
            System.out.print("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n|");
            for (int j = 0; j < chessboard[i].length; j++) {
                Chess chess = chessboard[i][j];
                String cellContent = getCellContent(chess);

                // 当没有棋子的时候，判断有没有背景
                if(cellContent == ""){
                    if(backgroundBoard[i][j] == null){
                    }
                    else{
                        //不知道可以不可以直接.color
                        cellContent = String.format("Block-%s", backgroundBoard[i][j].color);
                    }
                }

                // 输出格式化的格子
                System.out.print(String.format("%-22s", cellContent));
                //讨论激光
                if(path[i][j]==true){
                    System.out.print('*');
                }
                else {
                    System.out.print(' ');
                }

                System.out.print('|');
            }
            System.out.println(); // 换行
        }
        System.out.print("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }

    public static void printBoard(Chess[][] chessboard, Background[][] backgroundBoard, boolean[][] path, ChessLaserEmitter.Direction [][] vec_path) {
        for (int i = 0; i < chessboard.length; i++) {
            System.out.print("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n|");
            for (int j = 0; j < chessboard[i].length; j++) {
                Chess chess = chessboard[i][j];
                String cellContent = getCellContent(chess);

                // 当没有棋子的时候，判断有没有背景
                if(cellContent == ""){
                    if(backgroundBoard[i][j] == null){
                    }
                    else{
                        //不知道可以不可以直接.color
                        cellContent = String.format("Block-%s", backgroundBoard[i][j].color);
                    }
                }

                // 输出格式化的格子
                System.out.print(String.format("%-22s", cellContent));
                //讨论激光
                if(path[i][j]==true){
                    switch (vec_path[i][j]) {
                        case TOP -> System.out.println('↑');
                        case LEFT -> System.out.println('←');
                        case RIGHT -> System.out.println('→');
                        case BOTTOM -> System.out.println('↓');
                    }
                }
                else {
                    System.out.print(' ');
                }

                System.out.print('|');
            }
            System.out.println(); // 换行
        }
        System.out.print("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }

    private static String getCellContent(Chess chess) {
        if (chess == null) {
            return "";
        } else if (chess instanceof ChessKing) {
            return getChessKingInfo((ChessKing) chess);
        } else if (chess instanceof ChessLaserEmitter) {
            return getChessLaserEmitterInfo((ChessLaserEmitter) chess);
        } else if (chess instanceof ChessOneWayMirror) {
            return getChessOneWayMirrorInfo((ChessOneWayMirror) chess);
        } else if (chess instanceof ChessTwoWayMirror) {
            return getChessTwoWayMirrorInfo((ChessTwoWayMirror) chess);
        } else if (chess instanceof ChessShield) {
            return getChessShieldInfo((ChessShield) chess);
        } else {
            return "Unknown";
        }
    }

    private static String getChessShieldInfo(ChessShield chess) {
        return String.format("Shield-%s-%s", chess.color, chess.direction);
    }

    private static String getChessKingInfo(ChessKing chess) {
        return String.format("King-%s", chess.color);
    }

    private static String getChessLaserEmitterInfo(ChessLaserEmitter chess) {
        return String.format("Emitter-%s-%s", chess.color, chess.direction);
    }

    private static String getChessOneWayMirrorInfo(ChessOneWayMirror chess) {
        return String.format("OneM-%s-%s", chess.color, chess.direction);
    }

    private static String getChessTwoWayMirrorInfo(ChessTwoWayMirror chess) {
        return String.format("TwoM-%s-%s", chess.color, chess.direction);
    }
}
