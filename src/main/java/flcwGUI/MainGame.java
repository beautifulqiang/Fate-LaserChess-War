package flcwGUI;

import flcwGUI.LaserChessGamePlay.Board;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

import static flcwGUI.ButtonController.freeMode;

public class MainGame extends Application {
    static final BorderPane root_panel = new BorderPane();
    public static GameStyle game_style = GameStyle.classic;
    public static boolean reserved_map = false;
    public static String load_map;  // 如果是使用输入棋盘的方式，则需要提供地图文件名
    static Stage root_stage;
    //这里在12.21晚改了一下，多加了一个参数，因为我把棋盘类的构造函数给改了
    static Board board;  // 棋盘作为全局变量
    static GridPane game_grid = new GridPane();  // 棋盘，把棋子作为按钮放在上面

    public static void main(String[] args) {
        launch(args);
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

    @Override
    public void start(Stage primaryStage) {
        root_stage = primaryStage;
        gameModeSelect();
    }

    enum GameStyle {
        // 三种地图风格
        classic, elden, PvZ
    }
}
