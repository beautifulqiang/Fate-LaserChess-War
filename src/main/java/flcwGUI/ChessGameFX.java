package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import flcwGUI.LaserChessGamePlay.InputHandler;
import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import flcwGUI.LaserChessGamePlay.chess.Chess;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChessGameFX extends Application {

    private Board board = new Board(1);  // 获取棋盘

    private static Chess.Color turn = null;  // 用于记录是谁的回合

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chess Game");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);

        initializeBoard(primaryStage, 8, 10); // 传入新的行列数

        primaryStage.show();
    }

    private void initializeBoard(Stage primaryStage, int rows, int cols) {
        GridPane gameGrid = new GridPane();
        gameGrid.setPadding(new Insets(5));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button square = new Button();
                square.setMinSize(75, 75);
                // Chess_type
                if (board.chessboard[row][col] != null) {
                    switch (board.chessboard[row][col].show_type()) {
                        case King:
                            square.setStyle("-fx-background-color: yellow;");
                            break;
                        case LaserEmitter:
                            square.setStyle("-fx-background-color: darkgray;");
                            break;
                        case OneWayMirror:
                            square.setStyle("-fx-background-color: orange;");
                            break;
                        case TwoWayMirror:
                            square.setStyle("-fx-background-color: pink;");
                            break;
                        case Shield:
                            square.setStyle("-fx-background-color: green;");
                            break;
                        default:
                            square.setStyle("-fx-background-color: #8e8d8d;");
                    }
                } else {
                    if (board.backgroundBoard[row][col] != null) {
                        if (board.backgroundBoard[row][col].color == Background.Color.RED) {
                            square.setStyle("-fx-background-color: red;");
                        } else {
                            square.setStyle("-fx-background-color: blue;");
                        }
                    } else {
                        square.setStyle("-fx-background-color: white;");
                    }
                }

                int finalRow = row;
                int finalCol = col;
                square.setOnAction(e -> handleChessPieceClick(primaryStage, finalRow, finalCol));

                gameGrid.add(square, col, row);
            }
        }

        Scene scene = new Scene(gameGrid);
        primaryStage.setScene(scene);
    }

    private void handleChessPieceClick(Stage primaryStage, int row, int col) {
        // 处理棋子点击事件
        Operate operation = InputHandler.getOperationFromInput(board, turn);
        System.out.println("Clicked on square: " + row + ", " + col);
        // 这里可以添加处理点击事件的代码
    }

    public static void main(String[] args) {
        launch(args);
    }
}
