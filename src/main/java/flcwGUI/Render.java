package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import flcwGUI.LaserChessGamePlay.Laser;
import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;

import static flcwGUI.ButtonController.*;
import static flcwGUI.MainGame.root_panel;

public class Render {

    public static Image getChessImage(Chess chess) {
        // 提供四个风格的素材包
        String image_path = switch (game_style) {
            case classic -> "/images/classic/";
            case elden -> "/images/elden/";
            case PvZ -> "/images/PvZ/";
        };

        if (chess != null) {
            switch (chess.show_type()) {
                case King:
                    image_path += "kings/";
                    if (chess.color == Chess.Color.BLUE) {
                        image_path += "blue_king.png";
                    } else {
                        image_path += "red_king.png";
                    }
                    break;
                case LaserEmitter:
                    image_path += "emitters/";
                    if (chess.color == Chess.Color.BLUE) {
                        image_path += "blue_emitter.png";
                    } else {
                        image_path += "red_emitter.png";
                    }
                    break;
                case OneWayMirror:
                    image_path += "one_way_mirrors/";
                    if (chess.color == Chess.Color.BLUE) {
                        image_path += "blue_one.png";
                    } else {
                        image_path += "red_one.png";
                    }
                    break;
                case TwoWayMirror:
                    image_path += "two_way_mirrors/";
                    if (chess.color == Chess.Color.BLUE) {
                        image_path += "blue_two.png";
                    } else {
                        image_path += "red_two.png";
                    }
                    break;
                case Shield:
                    image_path += "shields/";
                    if (chess.color == Chess.Color.BLUE) {
                        image_path += "blue_shield.png";
                    } else {
                        image_path += "red_shield.png";
                    }
                    break;
                default:
                    image_path += "default.png";
            }
        } else {
            image_path += "default.png";
        }


        return new Image(Objects.requireNonNull(Render.class.getResource(image_path)).toExternalForm());
    }

    private static Image getLaserImage(Chess chess, Background bg, Boolean cross) {
        String imagePath = "/images/";

        switch (game_style) {
            case classic -> imagePath += "classic/bullets/";
            case elden -> imagePath += "elden/bullets/";
            case PvZ -> imagePath += "PvZ/bullets/";
        }
        //判断激光颜色
        switch (turn) {
            case RED:
                if (chess == null) {
                    if (bg != null) {  // 此时说明背景有限制颜色
                        switch (bg.color) {
                            case RED:
                                if (!cross) imagePath += "red_red.png";
                                else imagePath += "red_red_cross.png";
                                break;
                            case BLUE:
                                if (!cross) imagePath += "blue_red.png";
                                else imagePath += "blue_red_cross.png";
                                break;
                        }
                    } else {
                        // 如果没有棋子，使用默认图片
                        if (!cross) imagePath += "red_bullet.png";
                        else imagePath += "red_bullet_cross.png";
                    }
                    break;
                } else {
                    //判断棋子类型
                    switch (chess.show_type()) {
                        case OneWayMirror:
                            //判断棋子颜色
                            if (chess.color == Chess.Color.RED) {
                                imagePath += "red_one_red.png";
                            } else {
                                imagePath += "blue_one_red.png";
                            }
                            break;
                        case TwoWayMirror:
                            if (chess.color == Chess.Color.RED) {
                                if (!cross) imagePath += "red_two_red.png";
                                else imagePath += "red_two_red_red.png";
                            } else {
                                if (!cross) imagePath += "blue_two_red.png";
                                else imagePath += "blue_two_red_red.png";
                            }
                            break;
                    }
                    break;
                }

            case BLUE:
                if (chess == null) {
                    if (bg != null) {  // 此时说明背景有限制颜色
                        switch (bg.color) {
                            case RED:
                                if (!cross) imagePath += "red_blue.png";
                                else imagePath += "red_blue_cross.png";
                                break;
                            case BLUE:
                                if (!cross) imagePath += "blue_blue.png";
                                else imagePath += "blue_blue_cross.png";
                                break;
                        }
                    } else {
                        // 如果没有棋子，使用默认图片
                        if (!cross) imagePath += "blue_bullet.png";
                        else imagePath += "blue_bullet_cross.png";
                    }
                    break;
                } else {
                    switch (chess.show_type()) {
                        case OneWayMirror:
                            //判断棋子颜色
                            if (chess.color == Chess.Color.RED) {
                                imagePath += "red_one_blue.png";
                            } else {
                                imagePath += "blue_one_blue.png";
                            }
                            break;
                        case TwoWayMirror:
                            if (chess.color == Chess.Color.RED) {
                                if (!cross) imagePath += "red_two_blue.png";
                                else imagePath += "red_two_blue_blue.png";
                            } else {
                                if (!cross) imagePath += "blue_two_blue.png";
                                else imagePath += "blue_two_blue_blue.png";
                            }
                            break;

                    }
                }
                //判断棋子类型
                break;
        }

        return new Image(Objects.requireNonNull(Render.class.getResource(imagePath)).toExternalForm());
    }

    private static void renderSquare(int row, int col, ChessLaserEmitter.Direction d) {
        if (row < 0 || col < 0) return;
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


        Image get_image = getLaserImage(chess, board.backgroundBoard[row][col], (d == ChessLaserEmitter.Direction.Cross));

        // 创建ImageView并设置图片
        ImageView imageView = new ImageView(get_image);
        imageView.setFitWidth(65); // 设置宽度
        imageView.setFitHeight(65); // 设置高度
        imageView.setPreserveRatio(true); // 保持宽高比

        if (chess == null) {
            imageView.setRotate(-90 + d.ordinal() * 90);
        } else {
            switch (chess.show_type()) {
                case OneWayMirror:
                    imageView.setRotate(-90 + chess.getrotate() * 90);
                    break;
                case TwoWayMirror:
                    if (chess.getrotate() == 1 && (d.ordinal() == 1 || d.ordinal() == 2)) imageView.setRotate(0);
                    if (chess.getrotate() == 1 && (d.ordinal() == 0 || d.ordinal() == 3)) imageView.setRotate(180);
                    if (chess.getrotate() == 0 && (d.ordinal() == 0 || d.ordinal() == 1)) imageView.setRotate(-90);
                    if (chess.getrotate() == 0 && (d.ordinal() == 2 || d.ordinal() == 3)) imageView.setRotate(90);
                    if (chess.getrotate() == 0 && d.ordinal() == 4) imageView.setRotate(-90);
                    if (chess.getrotate() == 1 && d.ordinal() == 4) imageView.setRotate(0);
                    break;
            }

        }
        // 设置按钮的尺寸
        if (square != null) {
            square.setMaxSize(65, 65);
        }

        // 创建一个带有圆角的 Rectangle 作为 clip
        Rectangle clip = new Rectangle(65, 65);
        clip.setArcWidth(15); // 设置圆角的宽度
        clip.setArcHeight(15); // 设置圆角的高度
        imageView.setClip(clip);

        // 设置按钮的图形内容为ImageView
        if (square != null) {
            square.setGraphic(imageView);
        }
    }

    public static Image getBackgroundImage(Background bg) {
        String imagePath = switch (game_style) {
            case classic -> "/images/classic/background/";
            case elden -> "/images/elden/background/";
            case PvZ -> "/images/PvZ/background/";
        };

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

        return new Image(Objects.requireNonNull(Render.class.getResource(imagePath)).toExternalForm());
    }

    static void renderSquare(int row, int col) {
        if (row < 0 || col < 0) return;
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
            get_image = Render.getBackgroundImage(background);
        }

        // 创建ImageView并设置图片
        ImageView imageView = new ImageView(get_image);
        imageView.setFitWidth(65); // 设置宽度
        imageView.setFitHeight(65); // 设置高度
        imageView.setPreserveRatio(true); // 保持宽高比

        // 设置按钮的尺寸
        if (square != null) {
            square.setMaxSize(65, 65);
        }

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
        clip.setArcWidth(15); // 设置圆角的宽度
        clip.setArcHeight(15); // 设置圆角的高度
        imageView.setClip(clip);

        // 设置按钮的图形内容为ImageView
        if (square != null) {
            square.setGraphic(imageView);
        }
    }

    static void laserOut() {
        int[] pos = new int[2];
        ChessLaserEmitter.Direction[] d = new ChessLaserEmitter.Direction[1];
        int[] b_size = board.getBoardSize();
        if (turn == Chess.Color.RED)
            board.getLaserEmitter(pos, d, 'R');
        else board.getLaserEmitter(pos, d, 'B');
        Laser laser = new Laser(pos[0], pos[1], d[0], b_size[0], b_size[1]);
        //取得要杀死的棋子的坐标
        int[] posToKill = laser.disseminate(board.chessboard);
        boolean[][] path = laser.getPath();
        ChessLaserEmitter.Direction[][] vec_path = laser.getVec_path();
        //渲染激光路径
        for (int i = 0; i < b_size[0]; i++) {
            for (int j = 0; j < b_size[1]; j++) {
                if (path[i][j]) {
                    renderSquare(i, j, vec_path[i][j]);
                }
            }
        }

        Platform.runLater(() -> {
            try {
                Thread.sleep(500); // 等待0.5秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            killChessRender(posToKill[0], posToKill[1]);
            board.killChess(posToKill[0], posToKill[1]);
            if (board.gameOver()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");

                String winner = board.getWinner();
                alert.setContentText("The winner is " + winner);

                alert.showAndWait().ifPresent(response -> {
                    // 用户点击确认按钮后的操作
                    if (response == ButtonType.OK) {
                        if (is_adv) {
                            Alert next_level = new Alert(Alert.AlertType.INFORMATION);
                            next_level.setTitle("恭喜！");
                            next_level.setHeaderText("恭喜胜利");
                            next_level.setContentText("点击进入下一关");
                            next_level.showAndWait();
                            // 冒险模式下，我们会进入下一关
                            game_level++;
                            gameStart();
                        } else {
                            // 不是冒险模式，则回到模式选择界面
                            gameModeSelect();
                        }
                    }
                });
            }
        });


        Platform.runLater(() -> {
            try {
                Thread.sleep(500); // 等待0.5秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int m = 0; m < b_size[0]; m++) {
                for (int n = 0; n < b_size[1]; n++) {
                    if (path[m][n]) {
                        renderSquare(m, n);
                    }
                }
            }
        });


        Platform.runLater(() -> {
            try {
                Thread.sleep(1000); // 等待1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

    public static void killChessRender(int row, int col) {
        if (row < 0 || col < 0) return;
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), Objects.requireNonNull(getSquareButton(row, col)).getGraphic());
        scaleTransition.setToX(0.5);  // 缩放到宽度为0
        scaleTransition.setToY(0.5);  // 缩放到高度为0

        Platform.runLater(() -> {
            scaleTransition.setOnFinished(event -> {
                //杀死棋子。杀死大海！杀死大海！
                board.killChess(row, col);
                renderSquare(row, col);
            });
            scaleTransition.play();
        });
    }

    public static Button getSquareButton(int row, int col) {
        // 获取所有子节点
        ObservableList<Node> children = game_grid.getChildren();

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

    public static void rotateImage_l(ImageView imageView) {
        // 创建 RotateTransition，并设置持续时间和旋转角度
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.2), imageView);
        rotateTransition.setByAngle(-90); // 旋转
        // 播放动画
        rotateTransition.play();

        rotateTransition.setOnFinished(event -> {
            Render.laserOut();

            turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
            piece_selected = false;  // 执行完后重置

            if (is_adv && turn == Chess.Color.RED) {
                handleAI();
            }
        });
    }

    public static void rotateImage_r(ImageView imageView) {
        // 创建 RotateTransition，并设置持续时间和旋转角度
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.2), imageView);
        rotateTransition.setByAngle(90); // 旋转

        // 播放动画
        rotateTransition.play();
        rotateTransition.setOnFinished(event -> {
            Render.laserOut();
            turn = (turn == Chess.Color.BLUE ? Chess.Color.RED : Chess.Color.BLUE); //更新回合
            piece_selected = false;  // 执行完后重置

            if (is_adv && turn == Chess.Color.RED) {
                handleAI();
            }
        });

    }

    public static void DIYBoardInitialize() {
        DIYboard = new Board();
        String music_name = "classic.mp3";
        root_panel.getStyleClass().add("root-classic");

        URL music_url = MainGame.class.getResource("/bgm/" + music_name);

//        GridPane DIY_grid = new GridPane();  // 用于DIY的棋盘

        // 设置棋盘的位置，使其居中
        DIY_grid.getStyleClass().add("gameGrid");
        DIY_grid.setPadding(new Insets(30, 0, 0, 70));

        // 添加保存并退出按钮
        Button save_quit_button = new Button("保存并退出");
        save_quit_button.setOnAction(event -> ButtonController.saveDIYBoard());
        save_quit_button.getStyleClass().add("exit-button");

        // 添加颜色选择的下拉框
        ComboBox<String> colorComboBox = new ComboBox<>();
        colorComboBox.getItems().addAll("RED", "BLUE");
        colorComboBox.setPromptText("选择颜色");

        // 添加类型选择的下拉框
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("King", "Shield", "Laser_emitter", "One_way_mirror",
                "Two_way_mirror", "Background", "Null_chess", "Null_background");
        typeComboBox.setPromptText("选择类型");

        // 添加方向选择的下拉框
        ComboBox<String> directionComboBox = new ComboBox<>();
        directionComboBox.setPromptText("选择方向");

        colorComboBox.getStyleClass().add("combo-box");
        typeComboBox.getStyleClass().add("combo-box");
        directionComboBox.getStyleClass().add("combo-box");

        // 处理类型选择变化以更新方向选项
        typeComboBox.setOnAction(event -> {
            String selectedType = typeComboBox.getValue();
            updateDirectionComboBox(directionComboBox, selectedType);
        });

        // 将组件添加到 exit_button_container VBox 中
        VBox exit_button_container = new VBox(save_quit_button);
        root_panel.setRight(exit_button_container);

        VBox chess_select_container = new VBox(200, colorComboBox, typeComboBox, directionComboBox);
        chess_select_container.setPadding(new Insets(50, 0, 0, 0));
        root_panel.setLeft(chess_select_container);


        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                Button square = new Button();
                square.setMaxSize(65, 65);
                square.getStyleClass().add("classic-chess");

                ImageView image_view = new ImageView();
                image_view.setFitWidth(65); // 设置宽度
                image_view.setFitHeight(65); // 设置高度

                // 全部的按钮一开始都初始化为背景图
                Image background_image = Render.getBackgroundImage(board.backgroundBoard[row][col]);
                image_view.setImage(background_image);

                // 创建一个带有圆角的 Rectangle 作为 clip
                Rectangle clip = new Rectangle(65, 65);
                clip.setArcWidth(15); // 设置圆角的宽度
                clip.setArcHeight(15); // 设置圆角的高度
                image_view.setClip(clip);

                // 设置按钮的图形内容为ImageView
                square.setGraphic(image_view);

                int final_row = row;
                int final_col = col;

                // 将按钮的事件处理器中获取下拉框的值的部分移到这里
                square.setOnAction(event -> {
                    String selectedColor = colorComboBox.getValue();
                    String selectedType = typeComboBox.getValue();
                    String selectedDirection = directionComboBox.getValue();
                    ButtonController.handleDIYChessPieceClick(final_row, final_col, selectedColor, selectedType, selectedDirection);
                });

                // 将按钮添加到 GridPane 中
                DIY_grid.add(square, col, row);
            }
        }


        // 添加棋盘到 BorderPane 的中心
        root_panel.setCenter(DIY_grid);

        // 开始播放音乐
        MediaPlayer media_player;
        if (music_url != null) {
            Media sound = new Media(music_url.toExternalForm());
            media_player = new MediaPlayer(sound);

            media_player.setVolume(0.4);
            media_player.play();
        } else {
            System.out.println("Resource not found: " + music_name);
        }
    }

    private static void updateDirectionComboBox(ComboBox<String> directionComboBox, String selectedType) {
        directionComboBox.getItems().clear(); // 清除现有选项

        switch (selectedType) {
            case "Shield":
            case "Laser_emitter":
                directionComboBox.getItems().addAll("LEFT", "TOP", "RIGHT", "BOTTOM");
                break;
            case "One_way_mirror":
                directionComboBox.getItems().addAll("LEFT_TOP", "RIGHT_TOP", "RIGHT_BOTTOM", "LEFT_BOTTOM");
                break;
            case "Two_way_mirror":
                directionComboBox.getItems().addAll("LEFT_TOP", "RIGHT_TOP");
                break;
            // 根据需要添加其他类型的情况
        }
    }

    public static Button getSquareButtonDIY(int row, int col) {
        // 获取所有子节点
        ObservableList<Node> children = DIY_grid.getChildren();

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

    static void renderSquareDIY(int row, int col) {
        if (row < 0 || col < 0) return;
        // 获取与指定行列相对应的按钮
        Button square = getSquareButtonDIY(row, col);
        Chess chess = DIYboard.chessboard[row][col];

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
            Background background = DIYboard.backgroundBoard[row][col];
            get_image = Render.getBackgroundImage(background);
        }

        // 创建ImageView并设置图片
        ImageView imageView = new ImageView(get_image);
        imageView.setFitWidth(65); // 设置宽度
        imageView.setFitHeight(65); // 设置高度
        imageView.setPreserveRatio(true); // 保持宽高比

        // 设置按钮的尺寸
        if (square != null) {
            square.setMaxSize(65, 65);
        }

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
        clip.setArcWidth(15); // 设置圆角的宽度
        clip.setArcHeight(15); // 设置圆角的高度
        imageView.setClip(clip);

        // 设置按钮的图形内容为ImageView
        if (square != null) {
            square.setGraphic(imageView);
        }
    }
}