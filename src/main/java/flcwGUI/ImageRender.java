package flcwGUI;

import flcwGUI.LaserChessGamePlay.Laser;
import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import flcwGUI.LaserChessGamePlay.chess.ChessLaserEmitter;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

import static flcwGUI.ButtonController.turn;
import static flcwGUI.MainGame.*;
import static flcwGUI.MainGame.GameStyle.elden;

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

    private static Image getLaserImage() {
        String imagePath = "/images/";

        switch (gameStyle) {
            case classic -> imagePath += "classic/bullets/";
            case elden -> imagePath += "elden/bullets/";
            case PvZ -> imagePath += "PvZ/bullets/";
        }

        switch (turn) {
            case RED -> imagePath += "red_bullet.png";
            case BLUE -> imagePath += "blue_bullet.png";
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
        Image get_image = getLaserImage();

        // 创建ImageView并设置图片
        ImageView imageView = new ImageView(get_image);
        imageView.setFitWidth(65); // 设置宽度
        imageView.setFitHeight(65); // 设置高度
        imageView.setPreserveRatio(true); // 保持宽高比
        imageView.setRotate(-90 + d.ordinal() * 90);
        // 设置按钮的尺寸
        square.setMaxSize(65, 65);

        // 旋转到合适方向
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
                    renderSquare(i, j,vec_path[i][j]);
                }
            }
        }

        //杀死棋子。杀死大海！杀死大海！
        board.killChess(posToKill[0], posToKill[1]);
        renderSquare(posToKill[0], posToKill[1]);

        Platform.runLater(() -> {
            try {
                Thread.sleep(1000); // 等待1秒
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