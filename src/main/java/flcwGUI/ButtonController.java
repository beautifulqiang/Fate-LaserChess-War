package flcwGUI;

import flcwGUI.LaserChessGamePlay.AI;
import flcwGUI.LaserChessGamePlay.Board;
import flcwGUI.LaserChessGamePlay.SaveBoard;
import flcwGUI.LaserChessGamePlay.User;
import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.*;
import flcwGUI.LaserChessGamePlay.operate.Move;
import flcwGUI.LaserChessGamePlay.operate.Operate;
import flcwGUI.LaserChessGamePlay.operate.Rotate;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static flcwGUI.LaserChessGamePlay.InputHandler.isChessColorMatching;
import static flcwGUI.LaserChessGamePlay.InputHandler.isLaserEmitter;
import static flcwGUI.MainGame.*;
import static flcwGUI.Render.*;

public class ButtonController {
    public static boolean is_adv = false;  // 在
    public static Chess.Color turn = Chess.Color.BLUE;  // 用于记录是谁的回合
    public static String map_path = "";
    public static boolean piece_selected = false; // 用于追踪是否已经选中了棋子
    public static GameStyle game_style = GameStyle.classic;
    public static String load_map;  // 如果是使用输入棋盘的方式，则需要提供地图文件名
    public static int AI_level = -1;  // AI难度
    public static int map_kind = -1;  // 哪张地图
    static Board board;  // 棋盘作为全局变量
    static Board DIYboard;
    static boolean[] DIY_legal = new boolean[4];// 0 for blue KING, 1 for red KING,2 for blue Laser emitter,3 for red Laser emitter
    static int[] red_laser_emitter_pos = new int[2];
    static int[] red_king_pos = new int[2];
    static int[] blue_king_pos = new int[2];
    static ChessLaserEmitter.Direction red_laser_emitter_dir;
    static int[] blue_laser_emitter_pos = new int[2];
    static ChessLaserEmitter.Direction blue_laser_emitter_dir;
    static GridPane DIY_grid = new GridPane();
    static GridPane game_grid = new GridPane();  // 棋盘，把棋子作为按钮放在上面
    static User game_user;
    private static int selected_piece_row = -1; // 用于存储选中的棋子的行
    private static int selected_piece_col = -1; // 用于存储选中的棋子的列

    private static void rotateChessBoardLeft() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (piece_selected && isChessColorMatching(board, selected_piece_row, selected_piece_col, turn)) {
            // 处理左旋转逻辑
            if (board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.King ||
                    board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.LaserEmitter)
                System.out.println(board.chessboard[selected_piece_row][selected_piece_col].show_type() + " can't be rotated");
            else {
                board.chessboard[selected_piece_row][selected_piece_col].rotate('l');
                System.out.println("棋子左旋");
                ImageView imageView = (ImageView) (Render.getSquareButton(selected_piece_row, selected_piece_col)).getGraphic();
                rotateImage_l(imageView);
            }
        } else if (!piece_selected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
    }

    private static void rotateChessBoardRight() {
        // 只有在有棋子被选中，而且可以旋转的情况下进行旋转
        if (piece_selected && isChessColorMatching(board, selected_piece_row, selected_piece_col, turn)) {
            // 处理右旋转逻辑
            if (board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.King ||
                    board.chessboard[selected_piece_row][selected_piece_col].show_type() == Chess.chess_type.LaserEmitter)
                System.out.println(board.chessboard[selected_piece_row][selected_piece_col].show_type() + " can't be rotated");
            else {
                board.chessboard[selected_piece_row][selected_piece_col].rotate('r');

                System.out.println("棋子右旋");
                ImageView imageView = (ImageView) (Render.getSquareButton(selected_piece_row, selected_piece_col)).getGraphic();
                rotateImage_r(imageView);
            }
        } else if (!piece_selected) {
            System.out.println("未选中棋子，不执行操作");
        } else {
            System.out.println("不是你的回合");
        }
    }

    public static void handleStyleButtonClick(String styleName) {
        switch (styleName) {
            case "Classic":
                game_style = MainGame.GameStyle.classic;
                break;
            case "Elden":
                game_style = MainGame.GameStyle.elden;
                break;
            case "PvZ":
                game_style = MainGame.GameStyle.PvZ;
                break;
        }

        gameStart();
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
                Render.laserOut();
                turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
                System.out.println("----------------------");
                if (turn == Chess.Color.BLUE) {
                    System.out.println("Now is Blue's turn!!!");
                } else {
                    System.out.println("Now is Red's turn!!!");
                }

                if (is_adv && turn == Chess.Color.RED) {
                    handleAI();
                }
            });//为了避免移动的太慢，渲染和激光重叠，所以就让激光在移动后的一秒发射

        } else {
            // 这个函数只处理Move的指令，如果不是Move则出错了
            System.out.println("Error occurred! Move fail!");
        }
    }

    public static void handleDIYButtonClick() {
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

    static void handleDIYChessPieceClick(int row, int col, String color, String type, String direction) {
        // 处理棋子点击事件
        System.out.println("Clicked on square: " + row + ", " + col);
        if (color == null) return;
        if (type == null) return;
        if (direction == null) direction = "LEFT";

        if (row == blue_king_pos[0] && col == blue_king_pos[1]) DIY_legal[0] = false;
        if (row == red_king_pos[0] && col == red_king_pos[1]) DIY_legal[1] = false;
        if (row == blue_laser_emitter_pos[0] && col == blue_laser_emitter_pos[1]) DIY_legal[2] = false;
        if (row == red_laser_emitter_pos[0] && col == red_laser_emitter_pos[1]) DIY_legal[3] = false;

        boolean is_chess = false;
        Chess tmp_chess = null;
        Background tmp_bac = null;
        // 从下拉框中获取选定的值
        switch (type) {
            case "King":
                if (Objects.equals(color, "RED")) {
                    if (DIY_legal[1]) System.out.println("WARNNING:There should be 1 red KING!");
                    else {
                        tmp_chess = new ChessKing(Chess.Color.RED);
                        DIY_legal[1] = true;
                        red_king_pos[0] = row;
                        red_king_pos[1] = col;
                    }
                } else {
                    if (DIY_legal[0]) System.out.println("WARNNING:There should be 1 blue KING!");
                    else {
                        tmp_chess = new ChessKing(Chess.Color.BLUE);
                        DIY_legal[0] = true;
                        blue_king_pos[0] = row;
                        blue_king_pos[1] = col;
                    }
                }
                is_chess = true;
                break;
            case "Shield":
                ChessShield.Direction tmp_d = switch (direction) {
                    case "LEFT" -> ChessShield.Direction.LEFT;
                    case "TOP" -> ChessShield.Direction.TOP;
                    case "RIGHT" -> ChessShield.Direction.RIGHT;
                    case "BOTTOM" -> ChessShield.Direction.BOTTOM;
                    default -> ChessShield.Direction.LEFT;
                };
                if (Objects.equals(color, "RED")) {

                    tmp_chess = new ChessShield(tmp_d, Chess.Color.RED);
                } else tmp_chess = new ChessShield(tmp_d, Chess.Color.BLUE);
                is_chess = true;
                break;
            case "Laser_emitter":
                ChessLaserEmitter.Direction tmp_d1 = switch (direction) {
                    case "LEFT" -> ChessLaserEmitter.Direction.LEFT;
                    case "TOP" -> ChessLaserEmitter.Direction.TOP;
                    case "RIGHT" -> ChessLaserEmitter.Direction.RIGHT;
                    case "BOTTOM" -> ChessLaserEmitter.Direction.BOTTOM;
                    default -> ChessLaserEmitter.Direction.LEFT;
                };
                if (Objects.equals(color, "RED")) {
                    if (DIY_legal[3]) System.out.println("WARNNING:There should be 1 red Laser_emitter!");
                    else {
                        if ((row == 0 && tmp_d1 == ChessLaserEmitter.Direction.TOP) || (row == 7 && tmp_d1 == ChessLaserEmitter.Direction.BOTTOM)
                                || (col == 0 && tmp_d1 == ChessLaserEmitter.Direction.LEFT) || (col == 9 && tmp_d1 == ChessLaserEmitter.Direction.RIGHT)) {
                            System.out.println("WARNING:The position of red Laser_emitter is illegal!");
                            return;
                        }
                        if (DIY_legal[2]) {
                            int dx = 0, dy = 0;
                            switch (blue_laser_emitter_dir) {
                                case TOP:
                                    dx = -1;
                                    break;
                                case LEFT:
                                    dy = -1;
                                    break;
                                case RIGHT:
                                    dy = 1;
                                    break;
                                case BOTTOM:
                                    dx = 1;
                                    break;
                            }
                            if ((blue_laser_emitter_pos[0] + dx == row) && (blue_laser_emitter_pos[1] + dy) == col) {
                                System.out.println("WARNING:The position of red Laser_emitter is illegal!");
                                return;
                            }
                        }
                        DIY_legal[3] = true;
                        red_laser_emitter_dir = tmp_d1;
                        red_laser_emitter_pos[0] = row;
                        red_laser_emitter_pos[1] = col;
                        tmp_chess = new ChessLaserEmitter(tmp_d1, Chess.Color.RED);
                    }
                } else {
                    if (DIY_legal[2]) System.out.println("WARNNING:There should be 1 blue Laser_emitter!");
                    else {
                        if ((row == 0 && tmp_d1 == ChessLaserEmitter.Direction.TOP) || (row == 7 && tmp_d1 == ChessLaserEmitter.Direction.BOTTOM)
                                || (col == 0 && tmp_d1 == ChessLaserEmitter.Direction.LEFT) || (col == 9 && tmp_d1 == ChessLaserEmitter.Direction.RIGHT)) {
                            System.out.println("WARNING:The position of blue Laser_emitter is illegal!");
                            return;
                        }
                        if (DIY_legal[3]) {
                            int dx = 0, dy = 0;
                            switch (red_laser_emitter_dir) {
                                case TOP:
                                    dx = -1;
                                    break;
                                case LEFT:
                                    dy = -1;
                                    break;
                                case RIGHT:
                                    dy = 1;
                                    break;
                                case BOTTOM:
                                    dx = 1;
                                    break;
                            }
                            if ((red_laser_emitter_pos[0] + dx == row) && (red_laser_emitter_pos[1] + dy) == col) {
                                System.out.println("WARNING:The position of blue Laser_emitter is illegal!");
                                return;
                            }
                        }
                        DIY_legal[2] = true;
                        blue_laser_emitter_dir = tmp_d1;
                        blue_laser_emitter_pos[0] = row;
                        blue_laser_emitter_pos[1] = col;
                        tmp_chess = new ChessLaserEmitter(tmp_d1, Chess.Color.BLUE);
                    }
                }
                is_chess = true;
                break;
            case "One_way_mirror":
                ChessOneWayMirror.Direction tmp_d2 = switch (direction) {
                    case "LEFT_TOP" -> ChessOneWayMirror.Direction.LEFT_TOP;
                    case "RIGHT_TOP" -> ChessOneWayMirror.Direction.RIGHT_TOP;
                    case "RIGHT_BOTTOM" -> ChessOneWayMirror.Direction.RIGHT_BOTTOM;
                    case "LEFT_BOTTOM" -> ChessOneWayMirror.Direction.LEFT_BOTTOM;
                    default -> ChessOneWayMirror.Direction.LEFT_TOP;
                };
                if (Objects.equals(color, "RED")) {

                    tmp_chess = new ChessOneWayMirror(tmp_d2, Chess.Color.RED);
                } else tmp_chess = new ChessOneWayMirror(tmp_d2, Chess.Color.BLUE);
                is_chess = true;
                break;
            case "Two_way_mirror":
                ChessTwoWayMirror.Direction tmp_d3 = switch (direction) {
                    case "LEFT_TOP" -> ChessTwoWayMirror.Direction.LEFT_TOP;
                    case "RIGHT_TOP" -> ChessTwoWayMirror.Direction.RIGHT_TOP;
                    default -> ChessTwoWayMirror.Direction.LEFT_TOP;
                };
                if (Objects.equals(color, "RED")) {
                    tmp_chess = new ChessTwoWayMirror(tmp_d3, Chess.Color.RED);
                } else tmp_chess = new ChessTwoWayMirror(tmp_d3, Chess.Color.BLUE);
                is_chess = true;
                break;

            case "Background":
                if (Objects.equals(color, "RED")) {
                    tmp_bac = new Background(Background.Color.RED);
                } else {
                    tmp_bac = new Background(Background.Color.BLUE);
                }
                is_chess = false;
                break;
            case "Null_background":
                tmp_bac = null;
                is_chess = false;
                break;
            case "Null_chess":
                tmp_chess = null;
                is_chess = true;
                break;
        }
        if (is_chess) {
            if (DIYboard.backgroundBoard[row][col] != null && tmp_chess != null) {
                if ((DIYboard.backgroundBoard[row][col].color == Background.Color.RED && tmp_chess.color == Chess.Color.BLUE) ||
                        (DIYboard.backgroundBoard[row][col].color == Background.Color.BLUE && tmp_chess.color == Chess.Color.RED)) {
                    System.out.println("WARNING:The background color does not match the chess color!");
                    return;
                }
            }
            DIYboard.chessboard[row][col] = tmp_chess;
        } else {
            if (DIYboard.chessboard[row][col] != null && tmp_bac != null) {
                if ((DIYboard.chessboard[row][col].color == Chess.Color.RED && tmp_bac.color == Background.Color.BLUE) ||
                        (DIYboard.chessboard[row][col].color == Chess.Color.BLUE && tmp_bac.color == Background.Color.RED)) {
                    System.out.println("WARNING:The background color does not match the chess color!");
                    return;
                }
            }
            DIYboard.backgroundBoard[row][col] = tmp_bac;
        }
        renderSquareDIY(row, col);
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
                AIorPvP();
            } else {
                // 文件不存在，显示提示框
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FLCW-地图提取错误");
                alert.setHeaderText(null);
                alert.setContentText("文件不存在: " + load_map);
                alert.showAndWait();
            }
        } else {
            freeMode();
        }
    }

    private static boolean isFileExists(String fileName) {
        // 获取资源文件的URL
        URL resource = MainGame.class.getResource("/saveBoard/" + game_user.toString() + "/" + fileName + ".txt");

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
        SaveBoard.saveBoard(board.chessboard, board.backgroundBoard, game_user, false, "none");
        Platform.exit();
    }

    public static void adventrueMode() {
        is_adv = true;  // 确定为冒险模式
        map_kind = 1; // 进行游戏关卡为第一关
        AI_level = 1;
        styleSelect();
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

    public static void gameStart() {
        if (is_adv) {
            root_stage.setTitle("FLCW-PvE");
        } else {
            root_stage.setTitle("FLCW-PvP");
        }

        Scene scene_game = new Scene(root_panel, 1280, 720);

        // 添加样式表到场景
        scene_game.getStylesheets().add(Objects.requireNonNull(MainGame.class.getResource("/flcwGUI/style.css")).toExternalForm());

        if (map_kind != -1) {//此时视为动态加载棋盘，不需要对棋盘再进行处理
            board = new Board(map_kind, false, false);
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
        Image leftRotateImage = new Image(Objects.requireNonNull(MainGame.class.getResourceAsStream("/images/left_rotate.png")));
        Image rightRotateImage = new Image(Objects.requireNonNull(MainGame.class.getResourceAsStream("/images/right_rotate.png")));

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
                    Image background_image = Render.getBackgroundImage(board.backgroundBoard[row][col]);
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

    public static void styleSelect() {
        // 创建 Label 和按钮
        Label label = new Label("地图风格：");
        Button style1Button = new Button("Classic");
        Button style2Button = new Button("Elden");
        Button style3Button = new Button("PvZ");
        Button returnButton = new Button("返回");

        // 添加按钮点击事件处理
        style1Button.setOnAction(event -> ButtonController.handleStyleButtonClick("Classic"));
        style2Button.setOnAction(event -> ButtonController.handleStyleButtonClick("Elden"));
        style3Button.setOnAction(event -> ButtonController.handleStyleButtonClick("PvZ"));
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
            level_button.setOnAction(event -> {
                map_kind = finalI;
                AIorPvP();
            });
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

    private static void AIorPvP() {
        Label label = new Label("选择游玩方式：");
        Button AI_button = new Button("PvE对局");
        Button PvP_button = new Button("PvP对局");

        AI_button.setOnAction(event -> {
            is_adv = true;
            ButtonController.EZorHard();
        });

        PvP_button.setOnAction(event -> ButtonController.styleSelect());

        HBox options_container = new HBox(label, AI_button, PvP_button);
        options_container.setPadding(new Insets(310, 0, 0, 300));

        BorderPane mode_panel = new BorderPane(options_container);

        Scene user_options_scene = new Scene(mode_panel, 1280, 720);
        user_options_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        mode_panel.getStyleClass().add("root-start");
        AI_button.getStyleClass().add("classic-button");
        PvP_button.getStyleClass().add("elden-button");

        root_stage.setScene(user_options_scene);
        root_stage.setTitle("FLCW-选择游玩方式");
        root_stage.show();
    }

    private static void EZorHard() {
        Label mode_select = new Label("选择难度：");
        Button easy_mode = new Button("easy");
        Button medium_mode = new Button("medium");
        Button hard_mode = new Button("hard");

        easy_mode.setOnAction(event -> {
            AI_level = 1;
            ButtonController.styleSelect();
        });
        medium_mode.setOnAction(event -> {
            AI_level = 2;
            ButtonController.styleSelect();
        });
        hard_mode.setOnAction(event -> {
            AI_level = 3;
            ButtonController.styleSelect();
        });

        HBox mode_button_container = new HBox(mode_select, easy_mode, medium_mode, hard_mode);
        mode_button_container.setPadding(new Insets(310, 0, 0, 270));

        StackPane mode_panel = new StackPane(mode_button_container);

        Scene mode_select_scene = new Scene(mode_panel, 1280, 720);
        mode_select_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        mode_panel.getStyleClass().add("root-start");
        easy_mode.getStyleClass().add("classic-button");
        medium_mode.getStyleClass().add("elden-button");
        hard_mode.getStyleClass().add("PvZ-button");

        root_stage.setScene(mode_select_scene);
        root_stage.setTitle("FLCW-选择游戏难度");
        root_stage.show();
    }

    public static void userLogin() {
        // 创建用于输入用户名的对话框
        TextInputDialog username_dialog = new TextInputDialog();
        username_dialog.setTitle("用户登录");
        username_dialog.setHeaderText(null);
        username_dialog.setContentText("请输入用户名:");

        // 获取用户名输入的结果
        username_dialog.showAndWait().ifPresent(username -> {
            // 创建用于输入密码的对话框
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("用户登录");
            passwordDialog.setContentText("请输入密码:");

            if (User.userExists(username)) {
                // 获取密码输入的结果
                passwordDialog.showAndWait().ifPresent(password -> {
                    if (User.isValidNameOrPasswd(username) && User.isValidNameOrPasswd(password)
                            && User.login(username, password)) {
                        // 如果用户登录成功，则创建本次游戏的user对象
                        game_user = new User(username);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("FLCW-登录成功");
                        alert.setContentText("转接到模式选择");
                        alert.showAndWait();

                        // 进入游戏模式选择
                        gameModeSelect();
                    } else {
                        // 否则发出警报
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("FLCW-登录失败");
                        alert.setContentText("请检查账号与密码是否匹配！");
                        alert.showAndWait();
                    }
                });
            } else {
                // 此时用户不存在
                // 否则发出警报
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FLCW-登录失败");
                alert.setContentText("账户不存在！");
                alert.showAndWait();
            }
        });
    }

    public static void userRegister() {
        // 创建用于输入用户名的对话框
        TextInputDialog username_dialog = new TextInputDialog();
        username_dialog.setTitle("用户注册");
        username_dialog.setContentText("请输入用户名:");

        // 获取用户名输入的结果
        username_dialog.showAndWait().ifPresent(username -> {
            // 创建用于输入密码的对话框
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("用户注册");
            passwordDialog.setContentText("请输入密码:");

            if (User.isValidNameOrPasswd(username)) {
                passwordDialog.showAndWait().ifPresent(password -> {
                    if (User.isValidNameOrPasswd(password)) {
                        // 注册成功，后端创建用户，并直接登录
                        User.register(username, password);
                        game_user = new User(username);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("FLCW-注册成功");
                        alert.setContentText("直接为您登录");
                        alert.showAndWait();

                        // 进入游戏模式选择
                        gameModeSelect();
                    } else {
                        // 否则发出警报
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("FLCW-注册失败");
                        alert.setContentText("请检查密码是否合法！");
                        alert.showAndWait();
                    }
                });
            } else {
                // 用户名不合法
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FLCW-注册失败");
                alert.setContentText("请检查账户名是否合法！");
                alert.showAndWait();
            }
        });
    }

    public static void gameModeSelect() {
        Label mode_select = new Label("选择模式：");
        Button adventure_mode = new Button("剧情模式");
        Button free_mode = new Button("自由对战");

        adventure_mode.setOnAction(event -> ButtonController.adventrueMode());
        free_mode.setOnAction(event -> freeMode());

        HBox mode_button_container = new HBox(mode_select, adventure_mode, free_mode);
        mode_button_container.setPadding(new Insets(310, 0, 0, 270));

        StackPane mode_panel = new StackPane(mode_button_container);

        Scene mode_select_scene = new Scene(mode_panel, 1280, 720);
        mode_select_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        mode_panel.getStyleClass().add("root-start");
        adventure_mode.getStyleClass().add("classic-button");
        free_mode.getStyleClass().add("elden-button");

        root_stage.setScene(mode_select_scene);
        root_stage.setTitle("FLCW-选择游戏模式");
        root_stage.show();
    }

    public static void saveDIYBoard() {
        // 获取要保存的棋盘名称
        TextInputDialog board_name_dialog = new TextInputDialog();
        board_name_dialog.setTitle("保存棋盘");
        board_name_dialog.setContentText("要保存的棋盘名字：");

        // 获取棋盘名输入的结果
        board_name_dialog.showAndWait().ifPresent(board_name -> {
            if (User.isValidNameOrPasswd(board_name)) {
                // 合法则保存棋盘并退出
                SaveBoard.saveBoard(DIYboard.chessboard, DIYboard.backgroundBoard, game_user, true, board_name);
                freeMode();
            } else {
                Alert invalid_name = new Alert(Alert.AlertType.ERROR);
                invalid_name.setHeaderText("非法名称！");
                invalid_name.setContentText("请输入正确名称");
                invalid_name.showAndWait();
            }
        });
    }

    static void handleAI() {
        Operate AI_op = null;
        switch (AI_level) {
            case 1 -> AI_op = AI.worst(board, Chess.Color.RED);
            case 2 -> AI_op = AI.random(board, Chess.Color.RED);
            case 3 -> AI_op = AI.best(board, Chess.Color.RED);
            default -> AI.random(board, Chess.Color.RED);
        }

        piece_selected = true;

        if (AI_op instanceof Move) {
            selected_piece_row = ((Move) AI_op).startX;
            selected_piece_col = ((Move) AI_op).startY;
            // AI判断进行一次Move
            if (board.operateChess(AI_op, (turn == Chess.Color.BLUE ? 'B' : 'R'))) {
                System.out.println("Move to: " + ((Move) AI_op).endX + ", " + ((Move) AI_op).endY);
                moveUpdate(AI_op);
                piece_selected = false;
            }
        } else {
            selected_piece_row = ((Rotate) AI_op).x;
            selected_piece_col = ((Rotate) AI_op).y;
            // AI判断进行一次旋转
            if (((Rotate) AI_op).direction == Rotate.RotateDirection.LEFT) {
                rotateChessBoardLeft();
            } else {
                rotateChessBoardRight();
            }
        }
    }
}
