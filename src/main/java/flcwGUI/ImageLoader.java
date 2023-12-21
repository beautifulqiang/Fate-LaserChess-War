package flcwGUI;

import flcwGUI.LaserChessGamePlay.background.Background;
import flcwGUI.LaserChessGamePlay.chess.Chess;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.collections.ObservableList;
import java.util.Objects;

import static flcwGUI.MainGame.*;

public class ImageLoader {

    private static final String IMAGE_ROOT = "/images/";

    public static Image getChessImage(Chess chess) {
        String imagePath = "";

        // 提供四个风格的素材包
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


        return new Image(Objects.requireNonNull(ImageLoader.class.getResource(imagePath)).toExternalForm());
    }

    public static Image getBackgroundImage(Background bg) {
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

        return new Image(ImageLoader.class.getResource(imagePath).toExternalForm());
    }

    static void renderSquare(int row, int col) {
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
            get_image = ImageLoader.getBackgroundImage(background);
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

    private static Button getSquareButton(int row, int col) {
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