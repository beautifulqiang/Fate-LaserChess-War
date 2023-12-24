package flcwGUI;

import flcwGUI.LaserChessGamePlay.Laser;
import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

import static flcwGUI.ButtonController.turn;
import static flcwGUI.MainGame.*;

public class ImageRender {

    public static Image getChessImage(Chess chess) {
        // 提供四个风格的素材包
        String imagePath = switch (gameStyle) {
            case classic -> "/images/classic/";
            case elden -> "/images/elden/";
            case PvZ -> "/images/PvZ/";
        };

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
                        imagePath += "blue_one.png";
                    } else {
                        imagePath += "red_one.png";
                    }
                    break;
                case TwoWayMirror:
                    imagePath += "two_way_mirrors/";
                    if (chess.color == Chess.Color.BLUE) {
                        imagePath += "blue_two.png";
                    } else {
                        imagePath += "red_two.png";
                    }
                    break;
                case Shield:
                    imagePath += "shields/";
                    if (chess.color == Chess.Color.BLUE) {
                        imagePath += "blue_shield.png";
                    } else {
                        imagePath += "red_shield.png";
                    }
                    break;
                default:
                    imagePath += "default.png";
            }
        } else {
            imagePath += "default.png";
        }


        return new Image(Objects.requireNonNull(ImageRender.class.getResource(imagePath)).toExternalForm());
    }

    private static Image getLaserImage(Chess chess, Background bg, Boolean cross) {
        String imagePath = "/images/";

        switch (gameStyle) {
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

        return new Image(Objects.requireNonNull(ImageRender.class.getResource(imagePath)).toExternalForm());
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
        square.setMaxSize(65, 65);

//         旋转到合适方向
//        if (chess != null) {
//            switch (chess.show_type()) {
//                case LaserEmitter, OneWayMirror, TwoWayMirror, Shield:
//                    imageView.setRotate(-90 + chess.getrotate() * 90);
//                    break;
//            }
//        }

        // 创建一个带有圆角的 Rectangle 作为 clip
        Rectangle clip = new Rectangle(65, 65);
        clip.setArcWidth(15); // 设置圆角的宽度
        clip.setArcHeight(15); // 设置圆角的高度
        imageView.setClip(clip);

        // 设置按钮的图形内容为ImageView
        square.setGraphic(imageView);
    }


    public static Image getBackgroundImage(Background bg) {
        String imagePath = switch (gameStyle) {
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

        return new Image(ImageRender.class.getResource(imagePath).toExternalForm());
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
            get_image = ImageRender.getBackgroundImage(background);
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
        clip.setArcWidth(15); // 设置圆角的宽度
        clip.setArcHeight(15); // 设置圆角的高度
        imageView.setClip(clip);

        // 设置按钮的图形内容为ImageView
        square.setGraphic(imageView);
    }

    static void laser_out() {
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

            kill_chess_render(posToKill[0], posToKill[1]);
            board.killChess(posToKill[0], posToKill[1]);
            if (board.gameOver()) {
                System.out.println("The winner is" + board.getWinner());
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

    public static void kill_chess_render(int row, int col) {
        if (row < 0 || col < 0) return;
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), getSquareButton(row, col).getGraphic());
        scaleTransition.setToX(0.5);  // 缩放到宽度为0
        scaleTransition.setToY(0.5);  // 缩放到高度为0
//        scaleTransition.setOnFinished(event -> {
//           //杀死棋子。杀死大海！杀死大海！
//            board.killChess(row, col);
//            renderSquare(row, col);
//        });
//        scaleTransition.play();

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
}