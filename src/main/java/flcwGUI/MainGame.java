package flcwGUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MainGame extends Application {
    static final BorderPane root_panel = new BorderPane();  // 根Panel
    static Stage root_stage; // 根舞台

    // 主应用入口
    public static void main(String[] args) {
        launch(args);
    }

    // main函数只调用用户登录的函数
    public static void loginOrRegister() {
        Label label = new Label("欢迎！");
        Button login_button = new Button("登录");
        Button register_button = new Button("注册");

        login_button.setOnAction(event -> ButtonController.userLogin());
        register_button.setOnAction(event -> ButtonController.userRegister());

        HBox options_container = new HBox(label, login_button, register_button);
        options_container.setPadding(new Insets(310, 0, 0, 300));

        BorderPane mode_panel = new BorderPane(options_container);

        Scene user_options_scene = new Scene(mode_panel, 1280, 720);
        user_options_scene.getStylesheets().add(Objects.requireNonNull(ButtonController.class.getResource("/flcwGUI/style.css")).toExternalForm());

        mode_panel.getStyleClass().add("root-start");
        login_button.getStyleClass().add("classic-button");
        register_button.getStyleClass().add("elden-button");

        root_stage.setScene(user_options_scene);
        root_stage.setTitle("FLCW-登录");
        root_stage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        root_stage = primaryStage;
        loginOrRegister();
    }

    enum GameStyle {
        // 三种地图风格
        classic, elden, PvZ
    }
}
