package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import static flcwGUI.ImageRender.getChessImage;

public class MainGame extends Application {
    //这里在12.21晚改了一下，多加了一个参数，因为我把棋盘类的构造函数给改了
    final static Board board = new Board(1, false);  // 棋盘作为全局变量
    public static GameStyle gameStyle = GameStyle.elden;
    @FXML
    static GridPane gameGrid = new GridPane();  // 棋盘，把棋子作为按钮放在上面
    private final BorderPane root = new BorderPane();

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

    private void initializeBoard(int rows, int cols) {
        switch (gameStyle) {
            case classic -> root.getStyleClass().add("root-classic");
            case elden -> root.getStyleClass().add("root-elden");
            case PvZ -> root.getStyleClass().add("root-PvZ");
        }

        // 设置棋盘的位置，使其居中
        gameGrid.getStyleClass().add("gameGrid");
        gameGrid.setPadding(new Insets(30, 0, 0, 145));

        // 添加左旋转按钮
        Button rotateLeftButton = new Button("Rotate Left");
        rotateLeftButton.setOnAction(e -> ButtonController.rotateChessBoardLeft());

        // 添加右旋转按钮
        Button rotateRightButton = new Button("Rotate Right");
        rotateRightButton.setOnAction(e -> ButtonController.rotateChessBoardRight());

        // 将按钮添加到 HBox 中
        HBox rotate_button_container = new HBox(10); // 设置垂直间隔
        rotate_button_container.getChildren().addAll(rotateLeftButton, rotateRightButton);

        rotateLeftButton.getStyleClass().add("rotate-button");
        rotateRightButton.getStyleClass().add("rotate-button");
        rotate_button_container.getStyleClass().add("rotate-button-container");

        // 添加 HBox 到 BorderPane 的底部
        root.setBottom(rotate_button_container);

        // 添加退出按钮
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(event -> ButtonController.exitGame());

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
                    Image background_image = ImageRender.getBackgroundImage(board.backgroundBoard[row][col]);
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
                square.setOnAction(e -> ButtonController.handleChessPieceClick(finalRow, finalCol));

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
        switchModeButton.setOnAction(event -> ButtonController.switchGameMode());

        // 将按钮添加到垂直布局
        VBox controls = new VBox(10); // 设置垂直间隔
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().add(switchModeButton);

        return controls;
    }

    enum GameStyle {
        // 三种地图风格
        classic, elden, PvZ
    }
}
