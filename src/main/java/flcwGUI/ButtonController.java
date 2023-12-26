package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import flcwGUI.LaserChessGamePlay.SaveBoard;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static flcwGUI.ImageRender.*;
import static flcwGUI.LaserChessGamePlay.InputHandler.isChessColorMatching;
import static flcwGUI.LaserChessGamePlay.InputHandler.isLaserEmitter;
import static flcwGUI.MainGame.*;

public class ButtonController {
    public static Chess.Color turn = Chess.Color.BLUE;  // 用于记录是谁的回合

    public static String map_path = "";
    public static boolean pieceSelected = false; // 用于追踪是否已经选中了棋子
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
                System.out.println("棋子左旋");
                ImageView imageView = (ImageView) (ImageRender.getSquareButton(selectedPieceRow, selectedPieceCol)).getGraphic();
                rotateImage_l(imageView);
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

                ImageView imageView = (ImageView) (ImageRender.getSquareButton(selectedPieceRow, selectedPieceCol)).getGraphic();
                rotateImage_r(imageView);
                System.out.println("棋子右旋");
            }
        } else if (!pieceSelected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
    }

    public static void handleStyleButtonClick(String styleName) {
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

        if (reserved_map) {
            game_start(-1);
        } else {
            level_select();
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
        if (op instanceof Move) {
            int startX = ((Move) op).startX;
            int startY = ((Move) op).startY;
            int endX = ((Move) op).endX;
            int endY = ((Move) op).endY;

            // 更新 UI，这里可以根据棋子类型和颜色进行相应的渲染
            renderSquare(startX, startY);
            renderSquare(endX, endY);

            Platform.runLater(() -> {
                try {
                    Thread.sleep(1000); // 等待1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 激光发射
                ImageRender.laser_out();
                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
                System.out.println("----------------------");
                if (turn == Chess.Color.BLUE) {
                    System.out.println("Now is Blue's turn!!!");
                } else {
                    System.out.println("Now is Red's turn!!!");
                }
            });//为了避免移动的太慢，渲染和激光重叠，所以就让激光在移动后的一秒发射

        } else {
            // 这个函数只处理Move的指令，如果不是Move则出错了
            System.out.println("Error occurred! Move fail!");
        }
    }

    public static void handleDIYButtonClick() {
        System.out.println("Click diyButton!");

        // 游戏风格直接设计为classic
        gameStyle = MainGame.GameStyle.classic;

        // 实现对于DIY的DIY操作


    }

    public static void loadReservedMap() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("地图文件名");
        dialog.setHeaderText(null);
        dialog.setContentText("请输入地图文件名:");

        // 获取用户输入的文件名
        Optional<String> result = dialog.showAndWait();

        // 检查用户是否输入了文件名
        if (result.isPresent()) {
            // 获取用户输入的文件名并设置到全局变量
            load_map = result.get();

            // 检查文件是否存在
            if (isFileExists(load_map)) {
                // 文件存在，根据用户输入的文件名创建Board对象
                board = new Board(-1, false, true);
                // 根据用户输入的文件名创建Board对象
                System.out.println("棋盘读入成功！");
                reserved_map = true;
                style_select_scene();
            } else {
                // 文件不存在，可以在这里进行相应的处理
                System.out.println("文件不存在: " + load_map);
            }
        } else {
            game_mode_select();
        }
    }

    private static boolean isFileExists(String fileName) {
        // 获取资源文件的URL
        URL resource = MainGame.class.getResource("/saveBoard/" + fileName);

        // 如果资源文件存在，创建Path对象并检查文件是否存在
        if (resource != null) {
            try {
                Path path = Paths.get(resource.toURI());
                map_path = path.toString();
                return Files.exists(path) && Files.isRegularFile(path);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    public static void saveQuitClick() {
        SaveBoard.saveBoard(board.chessboard, board.backgroundBoard);
        Platform.exit();
    }
}
