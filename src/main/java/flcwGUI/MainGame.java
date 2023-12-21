package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Objects;

import static flcwGUI.LaserChessGamePlay.Game.getTurn;
import static flcwGUI.LaserChessGamePlay.InputHandler.isChessColorMatching;
import static flcwGUI.LaserChessGamePlay.InputHandler.isLaserEmitter;

public class MainGame extends Application {
    private static Chess.Color turn = Chess.Color.BLUE;  // 用于记录是谁的回合
    private final Board board = new Board(1);  // 棋盘作为全局变量
    private final BorderPane root = new BorderPane();
    @FXML
    GridPane gameGrid = new GridPane();  // 棋盘，把棋子作为按钮放在上面
    private GameStyle GS = GameStyle.elden;
    private boolean pieceSelected = false; // 用于追踪是否已经选中了棋子
    private int selectedPieceRow = -1; // 用于存储选中的棋子的行
    private int selectedPieceCol = -1; // 用于存储选中的棋子的列

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chess Game");

        Scene scene = new Scene(root, 1280, 720);

        // 添加样式表到场景
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/flcwGUI/style.css")).toExternalForm());


        initializeBoard(8, 10); // 初始化棋盘
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void exitGame() {
        Platform.exit(); // 关闭游戏
    }

    private void initializeBoard(int rows, int cols) {
        gameGrid.getStyleClass().add("gameGrid");
        // 为了让棋盘在正中间，直接添加空白间隔
        gameGrid.setPadding(new Insets(0, 0, 0, 150));

        gameGrid.setHgap(5);
        gameGrid.setVgap(5);

        // 添加左旋转按钮
        Button rotateLeftButton = new Button("Rotate Left");
        rotateLeftButton.getStyleClass().add("rotate-button");
        rotateLeftButton.setOnAction(e -> rotateChessBoardLeft());

        // 添加右旋转按钮
        Button rotateRightButton = new Button("Rotate Right");
        rotateRightButton.getStyleClass().add("rotate-button");
        rotateRightButton.setOnAction(e -> rotateChessBoardRight());

        // 将按钮添加到 HBox 中
        HBox rotate_button_container = new HBox(10); // 设置垂直间隔
        rotate_button_container.getChildren().addAll(rotateLeftButton, rotateRightButton);
        rotate_button_container.getStyleClass().add("rotate-button-container");

        // 添加 HBox 到 BorderPane 的底部
        root.setBottom(rotate_button_container);

        // 添加退出按钮
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(event -> exitGame());

        root.setRight(exitButton);

        VBox controls = createControls();
        root.setLeft(controls);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button square = new Button();
                square.setMaxSize(65, 65);
                square.getStyleClass().add("chess");

                ImageView imageView = new ImageView();
                imageView.setFitWidth(65); // 设置宽度
                imageView.setFitHeight(65); // 设置高度

                // Chess_type
                if (board.chessboard[row][col] != null) {
                    // 根据棋子的类型和颜色获取图片
                    Image chessImage = getChessImage(board.chessboard[row][col]);

                    // 设置ImageView的图片
                    imageView.setImage(chessImage);

                    // 为图片旋转到合适方向
                    switch (board.chessboard[row][col].show_type()) {
                        case LaserEmitter, OneWayMirror, TwoWayMirror, Shield:
                            imageView.setRotate(-90 + board.chessboard[row][col].getrotate() * 90);
                            break;
                    }
                } else {
                    Image background_image = getBackgroundImage(board.backgroundBoard[row][col]);
                    imageView.setImage(background_image);
                }

                // 创建一个带有圆角的 Rectangle 作为 clip
                Rectangle clip = new Rectangle(65, 65);
                clip.setArcWidth(20); // 设置圆角的宽度
                clip.setArcHeight(20); // 设置圆角的高度
                imageView.setClip(clip);

                // 设置按钮的图形内容为ImageView
                square.setGraphic(imageView);

                int finalRow = row;
                int finalCol = col;
                square.setOnAction(e -> handleChessPieceClick(finalRow, finalCol));

                // 将按钮添加到 GridPane 中
                gameGrid.add(square, col, row);
            }
        }

        // 添加棋盘到 BorderPane 的中心
        root.setCenter(gameGrid);
    }

    private VBox createControls() {
        // 创建按钮
        Button switchModeButton = new Button("Switch Mode");
        switchModeButton.getStyleClass().add("switch-mode-button");
        switchModeButton.setOnAction(event -> switchGameMode());

        // 将按钮添加到垂直布局
        VBox controls = new VBox(10); // 设置垂直间隔
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().add(switchModeButton);

        return controls;
    }

    private void switchGameMode() {
    }

    private void rotateChessBoardLeft() {
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
    private void rotateChessBoardRight() {
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

    private void handleChessPieceClick(int row, int col) {
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
                move_update(op);
            }
            // 重置选中状态
            pieceSelected = false;
            selectedPieceRow = -1;
            selectedPieceCol = -1;
        }
    }

    private void move_update(Operate op) {
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
            System.out.println("Error occurred! Move fail!");
        }
    }

    private void renderSquare(int row, int col) {
        // 获取与指定行列相对应的按钮
        Button square = getSquareButton(row, col);
        Chess chess = board.chessboard[row][col];

        // 清空按钮的样式和图形内容
        if (square != null) {
            square.setStyle(null);
        }
        if (square != null) {
            square.setGraphic(null);
        }

        Image get_image;
        if (chess != null) {
            // 如果有棋子，加载相应的图片
            get_image = getChessImage(chess);
        } else {
            // 如果没有棋子，根据背景更新按钮的样式
            Background background = board.backgroundBoard[row][col];
            get_image = getBackgroundImage(background);
        }

        // 创建ImageView并设置图片
        ImageView imageView = new ImageView(get_image);
        imageView.setFitWidth(65); // 设置宽度
        imageView.setFitHeight(65); // 设置高度
        imageView.setPreserveRatio(true); // 保持宽高比

        // 设置按钮的尺寸
        square.setMaxSize(65, 65);

        // 旋转到合适方向
        if (chess != null) {
            switch (chess.show_type()) {
                case LaserEmitter, OneWayMirror, TwoWayMirror, Shield:
                    imageView.setRotate(-90 + chess.getrotate() * 90);
                    break;
            }
        }

        // 创建一个带有圆角的 Rectangle 作为 clip
        Rectangle clip = new Rectangle(65, 65);
        clip.setArcWidth(20); // 设置圆角的宽度
        clip.setArcHeight(20); // 设置圆角的高度
        imageView.setClip(clip);

        // 设置按钮的图形内容为ImageView
        square.setGraphic(imageView);
    }

    private Image getChessImage(Chess chess) {
        String imagePath = "";

        switch (GS) {
            case classic:
                imagePath = "/images/classic/";
                break;
            case elden:
                imagePath = "/images/elden/";
                break;
            case memes:
                imagePath = "/images/memes/";
                break;
            case PvZ:
                imagePath = "/images/PvZ/";
                break;
        }

        if (chess != null) {
            switch (chess.show_type()) {
                case King:
                    imagePath += "kings/";
                    if (chess.color == Chess.Color.BLUE) {
                        imagePath += "blue_king.png";
                    } else {
                        imagePath += "red_king.png";
                    }
                    break;
                case LaserEmitter:
                    imagePath += "emitters/";
                    if (chess.color == Chess.Color.BLUE) {
                        imagePath += "blue_emitter.png";
                    } else {
                        imagePath += "red_emitter.png";
                    }
                    break;
                case OneWayMirror:
                    imagePath += "one_way_mirrors/";
                    if (chess.color == Chess.Color.BLUE) {
                        imagePath += "blue_one.jpg";
                    } else {
                        imagePath += "red_one.jpg";
                    }
                    break;
                case TwoWayMirror:
                    imagePath += "two_way_mirrors/";
                    if (chess.color == Chess.Color.BLUE) {
                        imagePath += "blue_two.jpg";
                    } else {
                        imagePath += "red_two.jpg";
                    }
                    break;
                case Shield:
                    imagePath += "shields/";
                    if (chess.color == Chess.Color.BLUE) {
                        imagePath += "blue_shield.jpg";
                    } else {
                        imagePath += "red_shield.jpg";
                    }
                    break;
                default:
                    imagePath += "default.png";
            }
        } else {
            imagePath += "default.png";
        }


        return new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
    }

    private Image getBackgroundImage(Background bg) {
        String imagePath = "";

        switch (GS) {
            case classic:
                imagePath = "/images/classic/background/";
                break;
            case elden:
                imagePath = "/images/elden/background/";
                break;
            case memes:
                imagePath = "/images/memes/background/";
                break;
            case PvZ:
                imagePath = "/images/PvZ/background/";
                break;
        }

        if (bg != null) {  // 此时说明背景有限制颜色
            switch (bg.color) {
                case RED:
                    imagePath += "red_reserved_cell.png";
                    break;
                case BLUE:
                    imagePath += "blue_reserved_cell.png";
                    break;
            }
        } else {
            // 如果没有棋子，使用默认图片
            imagePath += "default.png";
        }

        return new Image(getClass().getResource(imagePath).toExternalForm());
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

    private enum GameStyle {
        classic, elden, memes, PvZ
    }
}
