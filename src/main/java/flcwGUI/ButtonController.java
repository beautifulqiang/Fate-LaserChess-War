package flcwGUI;

import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import javafx.application.Platform;
import javafx.fxml.FXML;

import static flcwGUI.ImageLoader.renderSquare;
import static flcwGUI.LaserChessGamePlay.Game.getTurn;
import static flcwGUI.LaserChessGamePlay.InputHandler.isChessColorMatching;
import static flcwGUI.LaserChessGamePlay.InputHandler.isLaserEmitter;
import static flcwGUI.MainGame.board;

public class ButtonController {
    private static Chess.Color turn = Chess.Color.BLUE;  // 用于记录是谁的回合
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
                renderSquare(selectedPieceRow, selectedPieceCol);
                System.out.println("棋子左旋");
                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
                pieceSelected = false;  // 执行完后重置
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
                renderSquare(selectedPieceRow, selectedPieceCol);
                System.out.println("棋子右旋");
                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
                pieceSelected = false;  // 执行完后重置
            }
        } else if (!pieceSelected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
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
            if (board.operateChess(op, getTurn().charAt(0))) {
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
        turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合

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
        } else {
            // 这个函数只处理Move的指令，如果不是Move则出错了
            System.out.println("Error occurred! Move fail!");
        }
    }

    static void exitGame() {
        Platform.exit(); // 关闭游戏
    }

    static void switchGameMode() {

    }
}
