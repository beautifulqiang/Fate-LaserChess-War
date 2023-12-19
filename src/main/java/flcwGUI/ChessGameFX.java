package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import flcwGUI.LaserChessGamePlay.InputHandler;
import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import flcwGUI.LaserChessGamePlay.chess.Chess;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import static flcwGUI.LaserChessGamePlay.Game.getTurn;
import static flcwGUI.LaserChessGamePlay.InputHandler.*;

public class ChessGameFX extends Application {

    private Board board = new Board(1);  // 获取棋盘

    private static Chess.Color turn = Chess.Color.BLUE;  // 用于记录是谁的回合

    private boolean pieceSelected = false; // 用于追踪是否已经选中了棋子
    private int selectedPieceRow = -1; // 用于存储选中的棋子的行
    private int selectedPieceCol = -1; // 用于存储选中的棋子的列

    @FXML
    GridPane gameGrid = new GridPane();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chess Game");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(850);

        initializeBoard(primaryStage, 8, 10); // 传入新的行列数

        primaryStage.show();
    }

    private void initializeBoard(Stage primaryStage, int rows, int cols) {
        gameGrid.setPadding(new Insets(5));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);

        // BorderPane 布局
        BorderPane borderPane = new BorderPane();

        // 添加左旋转按钮
        Button rotateLeftButton = new Button("Rotate Left");
        rotateLeftButton.setOnAction(e -> rotateChessBoardLeft());

        // 添加右旋转按钮
        Button rotateRightButton = new Button("Rotate Right");
        rotateRightButton.setOnAction(e -> rotateChessBoardRight());

        // 将按钮添加到 HBox 中
        HBox buttonContainer = new HBox(10); // 设置垂直间隔
        buttonContainer.getChildren().addAll(rotateLeftButton, rotateRightButton);
        buttonContainer.setAlignment(Pos.CENTER); // 使按钮水平居中

        // 添加 HBox 到 BorderPane 的底部
        borderPane.setBottom(buttonContainer);

        Scene scene = new Scene(borderPane);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button square = new Button();
                square.setMinSize(75, 75);
                // Chess_type
                if (board.chessboard[row][col] != null) {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(75); // 设置宽度
                    imageView.setFitHeight(75); // 设置高度

                    // 根据棋子的类型和颜色获取图片
                    Image chessImage = getChessImage(board.chessboard[row][col]);

                    // 设置ImageView的图片
                    imageView.setImage(chessImage);

                    // 设置按钮的图形内容为ImageView
                    square.setGraphic(imageView);
                } else {
                    if (board.backgroundBoard[row][col] != null) {
                        if (board.backgroundBoard[row][col].color == Background.Color.RED) {
                            square.setStyle("-fx-background-color: #ea3e5b;");
                        } else {
                            square.setStyle("-fx-background-color: #7070fa;");
                        }
                    } else {
                        square.setStyle("-fx-background-color: white;");
                    }
                }

                int finalRow = row;
                int finalCol = col;
                square.setOnAction(e -> handleChessPieceClick(primaryStage, finalRow, finalCol));

                // 将按钮添加到 GridPane 中
                gameGrid.add(square, col, row);
            }
        }

        // 添加棋盘到 BorderPane 的中心
        borderPane.setCenter(gameGrid);

        primaryStage.setScene(scene);
    }

    private Image getChessImage(Chess chess) {
        String imagePath = "/images/";

        if (chess != null) {
            switch (chess.show_type()) {
                case King:
                    imagePath += "Kobe.jpg";
                    break;
                case LaserEmitter:
                    imagePath += "Redtea.jpg";
                    break;
//                case OneWayMirror:
//                    imagePath += "one_way_mirror.png";
//                    break;
//                case TwoWayMirror:
//                    imagePath += "two_way_mirror.png";
//                    break;
//                case Shield:
//                    imagePath += "shield.png";
//                    break;
                default:
                    imagePath += "Kobe.jpg";
            }
        } else {
            // 如果没有棋子，使用默认图片
            imagePath += "Kobe.jpg";
        }

//        imagePath = "/images/Kobe.jpg";

        return new Image(getClass().getResource(imagePath).toExternalForm());
    }

    @FXML
    private void rotateChessBoardLeft() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (pieceSelected) {
            // 处理左旋转逻辑

            System.out.println("棋子左旋");
            pieceSelected = false;  // 执行完后重置
        } else {
            System.out.println("未选中棋子，不执行操作");
        }
    }

    @FXML
    private void rotateChessBoardRight() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (pieceSelected) {
            // 处理左旋转逻辑

            System.out.println("棋子右旋");
            pieceSelected = false;  // 执行完后重置
        } else {
            System.out.println("未选中棋子，不执行操作");
        }
    }

    private void handleChessPieceClick(Stage primaryStage, int row, int col) {
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
                update_UI(op);
            }
            // 重置选中状态
            pieceSelected = false;
            selectedPieceRow = -1;
            selectedPieceCol = -1;
        }
    }

    private void update_UI(Operate op) {
        if (op instanceof Move) {
            int startX = ((Move) op).startX;
            int startY = ((Move) op).startY;
            int endX = ((Move) op).endX;
            int endY = ((Move) op).endY;

            // 更新 UI，这里可以根据棋子类型和颜色进行相应的渲染
            renderSquare(startX, startY);
            renderSquare(endX, endY);
        } else {
            // 实现旋转操作的渲染
        }
    }

    private void renderSquare(int row, int col) {
        // 获取与指定行列相对应的按钮
        Button square = getSquareButton(row, col);
        Chess chess = board.chessboard[row][col];

        // 清空按钮的样式和图形内容
        square.setStyle(null);
        square.setGraphic(null);

        if (chess != null) {
            // 如果有棋子，加载相应的图片
            Image chessImage = getChessImage(chess);

            // 创建ImageView并设置图片
            ImageView imageView = new ImageView(chessImage);
            imageView.setFitWidth(75); // 设置宽度
            imageView.setFitHeight(75); // 设置高度
            imageView.setPreserveRatio(true); // 保持宽高比

            // 设置按钮的最小尺寸
            square.setMinSize(75, 75);

            // 设置按钮的边距为0
            square.setStyle("-fx-padding: 0;");

            // 设置按钮的图形内容为ImageView
            square.setGraphic(imageView);
        } else {
            // 如果没有棋子，根据背景的颜色更新按钮的样式
            Background background = board.backgroundBoard[row][col];
            if (background != null) {
                if (background.color == Background.Color.RED) {
                    square.setStyle("-fx-background-color: #ea3e5b;");
                } else {
                    square.setStyle("-fx-background-color: #7070fa;");
                }
            } else {
                // 如果没有背景，将按钮的样式设置为白色
                square.setStyle("-fx-background-color: white;");
            }
        }
    }


    private Button getSquareButton(int row, int col) {
        // 获取所有子节点
        ObservableList<Node> children = gameGrid.getChildren();

        // 遍历子节点列表
        for (Node node : children) {
            // 检查节点是否为按钮类型
            if (node instanceof Button) {
                // 获取当前按钮所在的行和列
                int currentRow = GridPane.getRowIndex(node);
                int currentCol = GridPane.getColumnIndex(node);

                // 如果当前按钮的行列与目标行列匹配，则返回该按钮
                if (currentRow == row && currentCol == col) {
                    return (Button) node;
                }
            }
        }

        // 如果没有找到匹配的按钮，则返回 null
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
