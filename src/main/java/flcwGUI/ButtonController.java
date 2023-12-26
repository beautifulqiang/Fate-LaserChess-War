package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import flcwGUI.LaserChessGamePlay.SaveBoard;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static flcwGUI.ImageRender.*;
import static flcwGUI.LaserChessGamePlay.InputHandler.isChessColorMatching;
import static flcwGUI.LaserChessGamePlay.InputHandler.isLaserEmitter;
import static flcwGUI.MainGame.*;

public class ButtonController {
    public static Chess.Color turn = Chess.Color.BLUE;  // 用于记录是谁的回合

    public static String map_path = "";
    public static boolean piece_selected = false; // 用于追踪是否已经选中了棋子
    private static int selected_piece_row = -1; // 用于存储选中的棋子的行
    private static int selected_piece_col = -1; // 用于存储选中的棋子的列

    public static void rotateChessBoardLeft() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (piece_selected && isChessColorMatching(board, selected_piece_row, selected_piece_col, turn)) {
            // 处理左旋转逻辑
            if (board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.King ||
                    board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.LaserEmitter)
                System.out.println(board.chessboard[selected_piece_row][selected_piece_col].show_type() + " can't be rotated");
            else {
                board.chessboard[selected_piece_row][selected_piece_col].rotate('l');
                System.out.println("棋子左旋");
                ImageView imageView = (ImageView) (ImageRender.getSquareButton(selected_piece_row, selected_piece_col)).getGraphic();
                rotateImage_l(imageView);
            }
        } else if (!piece_selected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
    }

    @FXML
    static void rotateChessBoardRight() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (piece_selected && isChessColorMatching(board, selected_piece_row, selected_piece_col, turn)) {
            // 处理右旋转逻辑
            if (board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.King ||
                    board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.LaserEmitter)
                System.out.println(board.chessboard[selected_piece_row][selected_piece_col].show_type() + " can't be rotated");
            else {
                board.chessboard[selected_piece_row][selected_piece_col].rotate('r');

                ImageView imageView = (ImageView) (ImageRender.getSquareButton(selected_piece_row, selected_piece_col)).getGraphic();
                rotateImage_r(imageView);
                System.out.println("棋子右旋");
            }
        } else if (!piece_selected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
    }

    public static void handleStyleButtonClick(String styleName, int level) {
        switch (styleName) {
            case "Classic":
                MainGame.game_style = MainGame.GameStyle.classic;
                break;
            case "Elden":
                MainGame.game_style = MainGame.GameStyle.elden;
                break;
            case "PvZ":
                MainGame.game_style = MainGame.GameStyle.PvZ;
                break;
        }

        if (reserved_map) {
            gameStart(-1);
        } else {
            gameStart(level);
        }
    }

    static void handleChessPieceClick(int row, int col) {
        // 处理棋子点击事件
        System.out.println("Clicked on square: " + row + ", " + col);

        if (!piece_selected && isChessColorMatching(board, row, col, turn) && !isLaserEmitter(board, row, col)) {
            // 第一次点击，选中棋子
            piece_selected = true;
            selected_piece_row = row;
            selected_piece_col = col;
            System.out.println("Piece selected: " + row + ", " + col);
        } else if (piece_selected) {
            Operate op = new Move(selected_piece_row, selected_piece_col, row, col);
            if (board.operateChess(op, (turn == Chess.Color.BLUE ? 'B' : 'R'))) {
                System.out.println("Move to: " + row + ", " + col);
                moveUpdate(op);
            }
            // 重置选中状态
            piece_selected = false;
            selected_piece_row = -1;
            selected_piece_col = -1;
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
                ImageRender.laserOut();
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
        //        System.out.println("Click diyButton!");
        root_stage.setTitle("FLCW-DIY棋局");

        Scene DIY_scene = new Scene(root_panel, 1280, 720);

        DIY_scene.getStylesheets().add(Objects.requireNonNull(MainGame.class.getResource("/flcwGUI/style.css")).toExternalForm());

        board = new Board();

        // 游戏风格直接设计为classic
        game_style = MainGame.GameStyle.classic;

        root_stage.setScene(DIY_scene);
        DIYBoardInitialize();
        root_stage.show();
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
                styleSelect(-1);
            } else {
                // 文件不存在，可以在这里进行相应的处理
                System.out.println("文件不存在: " + load_map);
            }
        } else {
            freeMode();
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

    public static void adventrueMode() {
    }

    public static void freeMode() {
        Button default_mode = new Button("默认地图");
        Button DIY_mode = new Button("想要DIY吗");
        Button reserved_mode = new Button("已保存地图");
        Button return_button = new Button("返回");

        default_mode.setOnAction(event -> levelSelect());
        DIY_mode.setOnAction(event -> handleDIYButtonClick());
        reserved_mode.setOnAction(event -> loadReservedMap());
        return_button.setOnAction(event -> gameModeSelect());

        HBox options_container = new HBox(default_mode, DIY_mode, reserved_mode);
        options_container.setPadding(new Insets(310, 0, 0, 270));

        HBox return_button_container = new HBox(return_button);
        return_button_container.setPadding(new Insets(0, 0, 160, 600));

        BorderPane mode_panel = new BorderPane();
        mode_panel.setCenter(options_container);
        mode_panel.setBottom(return_button_container);

        Scene map_options = new Scene(mode_panel, 1280, 720);
        map_options.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        mode_panel.getStyleClass().add("root-start");
        default_mode.getStyleClass().add("classic-button");
        DIY_mode.getStyleClass().add("elden-button");
        reserved_mode.getStyleClass().add("PvZ-button");
        return_button.getStyleClass().add("return-button");

        root_stage.setScene(map_options);
        root_stage.setTitle("FLCW-选择地图类型");
        root_stage.show();
    }

    public static void gameStart(int level) {
        root_stage.setTitle("FLCW-游戏开始");

        Scene scene_game = new Scene(root_panel, 1280, 720);

        // 添加样式表到场景
        scene_game.getStylesheets().add(Objects.requireNonNull(MainGame.class.getResource("/flcwGUI/style.css")).toExternalForm());

        if (level != -1) {//此时视为动态加载棋盘，不需要对棋盘再进行处理
            board = new Board(level, false, false);
        }

        root_stage.setScene(scene_game);
        initializeBoard(8, 10); // 初始化棋盘
        root_stage.show();
    }

    private static void initializeBoard(int rows, int cols) {
        String musicName = "";  // 配置音乐

        switch (game_style) {
            case classic -> {
                root_panel.getStyleClass().add("root-classic");
                musicName = "classic.mp3";
            }
            case elden -> {
                root_panel.getStyleClass().add("root-elden");
                musicName += "elden.mp3";
            }
            case PvZ -> {
                root_panel.getStyleClass().add("root-PvZ");
                musicName += "PvZ.mp3";
            }
        }

        URL musicUrl = MainGame.class.getResource("/bgm/" + musicName);

        // 设置棋盘的位置，使其居中
        game_grid.getStyleClass().add("gameGrid");
        game_grid.setPadding(new Insets(30, 0, 0, 300));

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
        root_panel.setBottom(rotate_button_container);
        root_panel.setRight(save_quit_button);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button square = new Button();
                square.setMaxSize(65, 65);
                switch (game_style) {
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
                square.setOnAction(event -> ButtonController.handleChessPieceClick(finalRow, finalCol));

                // 将按钮添加到 GridPane 中
                game_grid.add(square, col, row);
            }
        }

        // 添加棋盘到 BorderPane 的中心
        root_panel.setCenter(game_grid);

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

    public static void styleSelect(int level) {
        // 创建 Label 和按钮
        Label label = new Label("地图风格：");
        Button style1Button = new Button("Classic");
        Button style2Button = new Button("Elden");
        Button style3Button = new Button("PvZ");
        Button returnButton = new Button("返回");

        // 添加按钮点击事件处理
        style1Button.setOnAction(event -> ButtonController.handleStyleButtonClick("Classic", level));
        style2Button.setOnAction(event -> ButtonController.handleStyleButtonClick("Elden", level));
        style3Button.setOnAction(event -> ButtonController.handleStyleButtonClick("PvZ", level));
        returnButton.setOnAction(event -> levelSelect());

        // 创建 HBox，并添加 Label 和按钮
        HBox styleButton_container = new HBox(label, style1Button, style2Button, style3Button);
        HBox returnButton_container = new HBox(returnButton);

        styleButton_container.setPadding(new Insets(310, 0, 0, 270));
        returnButton_container.setPadding(new Insets(0, 0, 160, 620));

        // 创建 BorderPane 放置 HBox
        BorderPane style_panel = new BorderPane();
        style_panel.setCenter(styleButton_container);
        style_panel.setBottom(returnButton_container);

        // 设置场景
        Scene style_select_scene = new Scene(style_panel, 1280, 720);
        style_select_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        style_panel.getStyleClass().add("root-start");
        style1Button.getStyleClass().add("classic-button");
        style2Button.getStyleClass().add("elden-button");
        style3Button.getStyleClass().add("PvZ-button");
        returnButton.getStyleClass().add("return-button");

        // 设置场景到主舞台
        root_stage.setScene(style_select_scene);
        root_stage.setTitle("FLCW-地图风格选择");
        root_stage.show();
    }

    public static void levelSelect() {
        Label level_select_prompt = new Label("选择关卡：");
        HBox level_select_container = new HBox(10);
        level_select_container.setPadding(new Insets(0, 0, 0, 230));
        level_select_container.getChildren().add(level_select_prompt);

        Button return_button = new Button("返回");
        HBox return_button_container = new HBox(return_button);
        return_button_container.setPadding(new Insets(0, 0, 160, 600));


        // 添加按钮点击事件处理
        return_button.setOnAction(event -> freeMode());

        BorderPane level_pane = new BorderPane();
        level_pane.setCenter(level_select_container);
        level_pane.setBottom(return_button_container);

        Scene level_select_scene = new Scene(level_pane, 1280, 720);
        level_select_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        // 默认棋盘只有三个
        for (int i = 1; i <= 3; i++) {
            Button level_button = new Button(String.valueOf(i));
            int finalI = i;
            level_button.setOnAction(event -> styleSelect(finalI));
            level_button.getStyleClass().add("classic-button");
            level_select_container.getChildren().add(level_button);
        }

        level_pane.getStyleClass().add("root-start");
        level_select_container.getStyleClass().add("rotate-button-container");
        return_button.getStyleClass().add("return-button");

        root_stage.setScene(level_select_scene);
        root_stage.setTitle("FLCW-选择游戏模式");
        root_stage.show();
    }
}
