package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

import static flcwGUI.ButtonController.handleDIYButtonClick;
import static flcwGUI.ButtonController.loadReservedMap;
import static flcwGUI.ImageRender.getChessImage;

public class MainGame extends Application {
    private static final BorderPane root = new BorderPane();
    public static GameStyle gameStyle = GameStyle.classic;
    public static boolean reserved_map = false;
    public static String load_map;  // 如果是使用输入棋盘的方式，则需要提供地图文件名
    //这里在12.21晚改了一下，多加了一个参数，因为我把棋盘类的构造函数给改了
    static Board board;  // 棋盘作为全局变量
    static GridPane gameGrid = new GridPane();  // 棋盘，把棋子作为按钮放在上面

    public static void main(String[] args) {
        launch(args);
    }

    public static void game_start(Stage primaryStage, int level) {
        primaryStage.setTitle("FLCW-游戏开始");

        Scene scene_game = new Scene(root, 1280, 720);

        // 添加样式表到场景
        scene_game.getStylesheets().add(Objects.requireNonNull(MainGame.class.getResource("/flcwGUI/style.css")).toExternalForm());

        switch (level) {
            case -1:
                break; //此时视为动态加载棋盘，不需要对棋盘再进行处理
            default:
                // 不需要进行DIY，将bool设置为false，根据level创建后端的棋盘对象
                board = new Board(level, false, false);
        }

        initializeBoard(8, 10); // 初始化棋盘
        primaryStage.setScene(scene_game);
        primaryStage.show();
    }

    public static void style_select_scene(Stage primaryStage) {
        // 创建 Label 和按钮
        Label label = new Label("地图风格：");
        Button style1Button = new Button("Classic");
        Button style2Button = new Button("Elden");
        Button style3Button = new Button("PvZ");
        Button returnButton = new Button("返回");

        // 创建 HBox，并添加 Label 和按钮
        HBox styleButton_container = new HBox();
        HBox returnButton_container = new HBox();
        styleButton_container.getChildren().addAll(label, style1Button, style2Button, style3Button);
        styleButton_container.setPadding(new Insets(310, 0, 0, 270));
        returnButton_container.getChildren().add(returnButton);
        returnButton_container.setPadding(new Insets(0, 0, 160, 620));

        // 创建 BorderPane 放置 HBox
        BorderPane style_panel = new BorderPane();
        style_panel.setCenter(styleButton_container);
        style_panel.setBottom(returnButton_container);

        // 设置场景
        Scene style_select_scene = new Scene(style_panel, 1280, 720);
        style_select_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        // 添加按钮点击事件处理
        style1Button.setOnAction(event -> ButtonController.handleStyleButtonClick("Classic", primaryStage));
        style2Button.setOnAction(event -> ButtonController.handleStyleButtonClick("Elden", primaryStage));
        style3Button.setOnAction(event -> ButtonController.handleStyleButtonClick("PvZ", primaryStage));
        returnButton.setOnAction(event -> game_mode_select(primaryStage));

        style_panel.getStyleClass().add("root-start");
        style1Button.getStyleClass().add("classic-button");
        style2Button.getStyleClass().add("elden-button");
        style3Button.getStyleClass().add("PvZ-button");
        returnButton.getStyleClass().add("return-button");

        // 设置场景到主舞台
        primaryStage.setScene(style_select_scene);
        primaryStage.setTitle("FLCW-地图风格选择");
        primaryStage.show();
    }

    public static void game_mode_select(Stage primaryStage) {
        Button default_mode = new Button("默认地图");
        Button DIY_mode = new Button("想要DIY吗");
        Button reserved_mode = new Button("已保存地图");

        default_mode.setOnAction(event -> style_select_scene(primaryStage));
        DIY_mode.setOnAction(event -> handleDIYButtonClick(primaryStage));
        reserved_mode.setOnAction(event -> loadReservedMap(primaryStage));

        HBox mode_button_container = new HBox();
        mode_button_container.getChildren().addAll(default_mode, DIY_mode, reserved_mode);
        mode_button_container.setPadding(new Insets(310, 0, 0, 270));

        StackPane mode_panel = new StackPane();
        mode_panel.getChildren().add(mode_button_container);

        Scene mode_select_scene = new Scene(mode_panel, 1280, 720);
        mode_select_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        mode_panel.getStyleClass().add("root-start");
        default_mode.getStyleClass().add("classic-button");
        DIY_mode.getStyleClass().add("elden-button");
        reserved_mode.getStyleClass().add("PvZ-button");

        primaryStage.setScene(mode_select_scene);
        primaryStage.setTitle("FLCW-选择游戏模式");
        primaryStage.show();
    }

    private static void initializeBoard(int rows, int cols) {
        String musicName = "";  // 配置音乐

        switch (gameStyle) {
            case classic -> {
                root.getStyleClass().add("root-classic");
                musicName = "classic.mp3";
            }
            case elden -> {
                root.getStyleClass().add("root-elden");
                musicName += "elden.mp3";
            }
            case PvZ -> {
                root.getStyleClass().add("root-PvZ");
                musicName += "PvZ.mp3";
            }
        }

        URL musicUrl = MainGame.class.getResource("/bgm/" + musicName);

        // 设置棋盘的位置，使其居中
        gameGrid.getStyleClass().add("gameGrid");
        gameGrid.setPadding(new Insets(30, 0, 0, 300));

        // 添加左旋转按钮
        Button rotateLeftButton = new Button();
        rotateLeftButton.setOnAction(event -> ButtonController.rotateChessBoardLeft());

        // 添加右旋转按钮
        Button rotateRightButton = new Button();
        rotateRightButton.setOnAction(event -> ButtonController.rotateChessBoardRight());

        // 添加保存并退出按钮
        Button save_quit_button = new Button("保存并退出");
        save_quit_button.setOnAction(event -> ButtonController.saveQuitClick());

        // 为按钮添加图片
        Image leftRotateImage = new Image(Objects.requireNonNull(MainGame.class.getResourceAsStream("/images/left_rotate.jpg")));
        Image rightRotateImage = new Image(Objects.requireNonNull(MainGame.class.getResourceAsStream("/images/right_rotate.jpg")));

        ImageView leftRotateImageView = new ImageView(leftRotateImage);
        ImageView rightRotateImageView = new ImageView(rightRotateImage);

        leftRotateImageView.setFitHeight(70);
        leftRotateImageView.setFitWidth(70);
        rightRotateImageView.setFitHeight(70);
        rightRotateImageView.setFitWidth(70);

        rotateLeftButton.setGraphic(leftRotateImageView);
        rotateRightButton.setGraphic(rightRotateImageView);


        // 将按钮添加到 HBox 中
        HBox rotate_button_container = new HBox(10); // 设置垂直间隔
        rotate_button_container.getChildren().addAll(rotateLeftButton, rotateRightButton);

        VBox exit_button_container = new VBox();
        exit_button_container.getChildren().add(save_quit_button);

        rotateLeftButton.getStyleClass().add("rotate-button");
        rotateRightButton.getStyleClass().add("rotate-button");
        rotate_button_container.getStyleClass().add("rotate-button-container");
        save_quit_button.getStyleClass().add("exit-button");

        // 添加 HBox 到 BorderPane 的底部
        root.setBottom(rotate_button_container);
        root.setRight(save_quit_button);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button square = new Button();
                square.setMaxSize(65, 65);
                switch (gameStyle) {
                    case classic -> square.getStyleClass().add("classic-chess");
                    case elden -> square.getStyleClass().add("elden-chess");
                    case PvZ -> square.getStyleClass().add("PvZ-chess");
                }

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
                clip.setArcWidth(15); // 设置圆角的宽度
                clip.setArcHeight(15); // 设置圆角的高度
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

        // 开始播放音乐
        MediaPlayer mediaPlayer;
        if (musicUrl != null) {
            Media sound = new Media(musicUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(sound);

            mediaPlayer.setVolume(0.4);
            mediaPlayer.play();
        } else {
            System.out.println("Resource not found: " + musicName);
        }
    }

    public static void level_select(Stage primaryStage) {
        Label level_select_prompt = new Label("选择关卡：");
        HBox level_select_container = new HBox(10);
        level_select_container.setPadding(new Insets(310, 0, 0, 230));
        level_select_container.getChildren().add(level_select_prompt);

        Button returnButton = new Button("返回");
        HBox returnButton_container = new HBox();
        returnButton_container.getChildren().add(returnButton);
        returnButton_container.setPadding(new Insets(0, 0, 160, 620));

        // 添加按钮点击事件处理
        returnButton.setOnAction(event -> game_mode_select(primaryStage));

        BorderPane level_pane = new BorderPane();
        level_pane.setCenter(level_select_container);
        level_pane.setBottom(returnButton_container);

        Scene level_select_scene = new Scene(level_pane, 1280, 720);
        level_select_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        // 默认棋盘只有三个
        for (int i = 1; i <= 3; i++) {
            Button level_button = new Button(String.valueOf(i));
            int finalI = i;
            level_button.setOnAction(event -> game_start(primaryStage, finalI));
            level_button.getStyleClass().add("classic-button");
            level_select_container.getChildren().add(level_button);
        }

        level_pane.getStyleClass().add("root-start");
        level_select_container.getStyleClass().add("rotate-button-container");
        returnButton.getStyleClass().add("return-button");

        primaryStage.setScene(level_select_scene);
        primaryStage.setTitle("FLCW-选择游戏模式");
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        game_mode_select(primaryStage);
    }

    enum GameStyle {
        // 三种地图风格
        classic, elden, PvZ
    }
}
