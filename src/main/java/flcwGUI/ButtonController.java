package flcwGUI;

import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.*;

import static flcwGUI.ImageRender.renderSquare;
import static flcwGUI.LaserChessGamePlay.InputHandler.isChessColorMatching;
import static flcwGUI.LaserChessGamePlay.InputHandler.isLaserEmitter;
import static flcwGUI.MainGame.board;

public class ButtonController {
    public static Chess.Color turn = Chess.Color.BLUE;  // 用于记录是谁的回合
    private static boolean pieceSelected = false; // 用于追踪是否已经选中了棋子
    private static int selectedPieceRow = -1; // 用于存储选中的棋子的行
    private static int selectedPieceCol = -1; // 用于存储选中的棋子的列



    public static void rotateChessBoardLeft() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (pieceSelected && isChessColorMatching(board, selectedPieceRow, selectedPieceCol, turn)) {
            // 处理左旋转逻辑
            if (board.chessboard[selectedPieceRow][selectedPieceCol].show_type() == Chess.chess_type.King ||
                    board.chessboard[selectedPieceRow][selectedPieceCol].show_type() == Chess.chess_type.LaserEmitter)
                System.out.println(board.chessboard[selectedPieceRow][selectedPieceCol].show_type() + " can't be rotated");
            else {
                board.chessboard[selectedPieceRow][selectedPieceCol].rotate('l');

//                rotateButton_90(ImageRender.getSquareButton(selectedPieceRow,selectedPieceCol));
//                renderSquare(selectedPieceRow, selectedPieceCol);
                System.out.println("棋子左旋");
                ImageView imageView = (ImageView) (ImageRender.getSquareButton(selectedPieceRow,selectedPieceCol)).getGraphic();
                rotateImage_l(imageView);
//                Platform.runLater(() -> {
//                    try {
//                        Thread.sleep(2000); // 等待2秒
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
//                // 激光发射
//                ImageRender.laser_out();
//
//                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
//                pieceSelected = false;  // 执行完后重置
            }
        } else if (!pieceSelected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
    }

    @FXML
    static void rotateChessBoardRight() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (pieceSelected && isChessColorMatching(board, selectedPieceRow, selectedPieceCol, turn)) {
            // 处理右旋转逻辑
            if (board.chessboard[selectedPieceRow][selectedPieceCol].show_type() == Chess.chess_type.King ||
                    board.chessboard[selectedPieceRow][selectedPieceCol].show_type() == Chess.chess_type.LaserEmitter)
                System.out.println(board.chessboard[selectedPieceRow][selectedPieceCol].show_type() + " can't be rotated");
            else {
                board.chessboard[selectedPieceRow][selectedPieceCol].rotate('r');
//                rotateButton90(ImageRender.getSquareButton(selectedPieceRow,selectedPieceCol));
//                renderSquare(selectedPieceRow, selectedPieceCol);

                ImageView imageView = (ImageView) (ImageRender.getSquareButton(selectedPieceRow,selectedPieceCol)).getGraphic();
                rotateImage_r(imageView);
                System.out.println("棋子右旋");

//                Platform.runLater(() -> {
//                    try {
//                        Thread.sleep(2000); // 等待2秒
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
//                // 激光发射
//                ImageRender.laser_out();
//                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
//                pieceSelected = false;  // 执行完后重置
            }
        } else if (!pieceSelected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
    }

    public static void handleStyleButtonClick(String styleName, Stage primaryStage) {
        switch (styleName) {
            case "Classic":
                MainGame.gameStyle = MainGame.GameStyle.classic;
                break;
            case "Elden":
                MainGame.gameStyle = MainGame.GameStyle.elden;
                break;
            case "PvZ":
                MainGame.gameStyle = MainGame.GameStyle.PvZ;
                break;
        }

        MainGame.game_start(primaryStage);
    }

    static void handleChessPieceClick(int row, int col) {
        // 处理棋子点击事件
        System.out.println("Clicked on square: " + row + ", " + col);

        if (!pieceSelected && isChessColorMatching(board, row, col, turn) && !isLaserEmitter(board, row, col)) {
            // 第一次点击，选中棋子
            pieceSelected = true;
            selectedPieceRow = row;
            selectedPieceCol = col;
            System.out.println("Piece selected: " + row + ", " + col);
        } else if (pieceSelected) {
            Operate op = new Move(selectedPieceRow, selectedPieceCol, row, col);
            if (board.operateChess(op, (turn == Chess.Color.BLUE ? 'B' : 'R'))) {
                System.out.println("Move to: " + row + ", " + col);
                moveUpdate(op);
            }
            // 重置选中状态
            pieceSelected = false;
            selectedPieceRow = -1;
            selectedPieceCol = -1;
        }
    }

    private static void moveUpdate(Operate op) {
        // 调试使用的测试信息
        System.out.println("----------------------");
        if (turn == Chess.Color.BLUE) {
            System.out.println("Now is Blue's turn!!!");
        } else {
            System.out.println("Now is Red's turn!!!");
        }

        if (op instanceof Move) {
            int startX = ((Move) op).startX;
            int startY = ((Move) op).startY;
            int endX = ((Move) op).endX;
            int endY = ((Move) op).endY;

            // 更新 UI，这里可以根据棋子类型和颜色进行相应的渲染
            renderSquare(startX, startY);
            renderSquare(endX, endY);

            // 激光发射
            ImageRender.laser_out();

            turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合

        } else {
            // 这个函数只处理Move的指令，如果不是Move则出错了
            System.out.println("Error occurred! Move fail!");
        }
    }
//    public static void rotateButton90(Button button) {
//        // 创建 RotateTransition，并设置持续时间和旋转角度
//        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), button);
//        rotateTransition.setByAngle(90);
//
//        // 播放动画
//        rotateTransition.play();
//    }
//    public static void rotateButton_90(Button button) {
//        // 创建 RotateTransition，并设置持续时间和旋转角度
//        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), button);
//        rotateTransition.setByAngle(-90);
//
//        // 播放动画
//        rotateTransition.play();
//    }
      public static void rotateImage_l(ImageView imageView) {
          // 创建 RotateTransition，并设置持续时间和旋转角度
          RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.2), imageView);
          rotateTransition.setByAngle(-90); // 旋转
          // 播放动画
          rotateTransition.play();
          rotateTransition.setOnFinished(event -> {
              ImageRender.laser_out();
                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
                pieceSelected = false;  // 执行完后重置
          });
      }
    public static void rotateImage_r(ImageView imageView) {
        // 创建 RotateTransition，并设置持续时间和旋转角度
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.2), imageView);
        rotateTransition.setByAngle(90); // 旋转

        // 播放动画
        rotateTransition.play();
        rotateTransition.setOnFinished(event -> {
            ImageRender.laser_out();
                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
                pieceSelected = false;  // 执行完后重置
        });
    }
    static void exitGame() {
        Platform.exit(); // 关闭游戏
    }

    static void switchGameMode() {

    }
}
