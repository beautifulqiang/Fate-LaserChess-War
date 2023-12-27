package flcwGUI.LaserChessGamePlay;

import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class SaveBoard {
    public static void saveBoard(Chess[][] chessboard, Background[][] backgroundBoard) {
        // 获取资源文件夹的路径
        ClassLoader classLoader = SaveBoard.class.getClassLoader();
        String folderPath = Objects.requireNonNull(classLoader.getResource("saveBoard/")).getPath();

        // 构建文件路径
        String filePath = folderPath + "InterruptSave.txt";
        File file = new File(filePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            int x = chessboard.length;
            int y = chessboard[0].length;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {

                    //有棋子
                    if (chessboard[i][j] != null) {
                        Chess chess = chessboard[i][j];
                        //写入该棋子颜色
                        if (chess.color == Chess.Color.BLUE) {
                            writer.println("B");
                        } else {
                            writer.println("R");
                        }

                        //写入该棋子类型，方向
                        if (chess instanceof ChessKing) {
                            writer.println("king");
                        } else if (chess instanceof ChessLaserEmitter) {
                            writer.println("le");
                            if (((ChessLaserEmitter) chess).direction == ChessLaserEmitter.Direction.BOTTOM) {
                                writer.println("b");
                            } else if (((ChessLaserEmitter) chess).direction == ChessLaserEmitter.Direction.TOP) {
                                writer.println("t");
                            } else if (((ChessLaserEmitter) chess).direction == ChessLaserEmitter.Direction.LEFT) {
                                writer.println("l");
                            } else if (((ChessLaserEmitter) chess).direction == ChessLaserEmitter.Direction.RIGHT) {
                                writer.println("r");
                            }
                        } else if (chess instanceof ChessOneWayMirror) {
                            writer.println("one");
                            if (((ChessOneWayMirror) chess).direction == ChessOneWayMirror.Direction.LEFT_BOTTOM) {
                                writer.println("lb");
                            } else if (((ChessOneWayMirror) chess).direction == ChessOneWayMirror.Direction.LEFT_TOP) {
                                writer.println("lt");
                            } else if (((ChessOneWayMirror) chess).direction == ChessOneWayMirror.Direction.RIGHT_BOTTOM) {
                                writer.println("rb");
                            } else if (((ChessOneWayMirror) chess).direction == ChessOneWayMirror.Direction.RIGHT_TOP) {
                                writer.println("rt");
                            }

                        } else if (chess instanceof ChessTwoWayMirror) {
                            writer.println("two");
                            if (((ChessTwoWayMirror) chess).direction == ChessTwoWayMirror.Direction.LEFT_TOP) {
                                writer.println("lt");
                            } else if (((ChessTwoWayMirror) chess).direction == ChessTwoWayMirror.Direction.RIGHT_TOP) {
                                writer.println("rt");
                            }
                        } else if (chess instanceof ChessShield) {
                            writer.println("shield");
                            if (((ChessShield) chess).direction == ChessShield.Direction.LEFT) {
                                writer.println("l");
                            } else if (((ChessShield) chess).direction == ChessShield.Direction.RIGHT) {
                                writer.println("r");
                            } else if (((ChessShield) chess).direction == ChessShield.Direction.TOP) {
                                writer.println("t");
                            } else if (((ChessShield) chess).direction == ChessShield.Direction.BOTTOM) {
                                writer.println("b");
                            }
                        }

                        //写入该棋子坐标
                        writer.println(i + "," + j);
                    }

                    //有背景
                    if (backgroundBoard[i][j] != null) {
                        Background bg = backgroundBoard[i][j];
                        //写入该棋子颜色
                        if (bg.color == Background.Color.BLUE) {
                            writer.println("B");
                        } else {
                            writer.println("R");
                        }

                        //写入类型
                        writer.println("bg");
                        //写入坐标
                        writer.println(i + "," + j);
                    }
                }
            }
            writer.println("B"); //这里只是为了占位，因为在DIYBoard类中的实现是先读入一个颜色再读入类型（END）
            writer.println("END");
        } catch (IOException e) {
            System.out.println("Error while saving the board configuration.");
            e.printStackTrace();
        }
    }
}
