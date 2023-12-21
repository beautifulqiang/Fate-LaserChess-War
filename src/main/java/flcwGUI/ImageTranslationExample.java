package flcwGUI;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.*;

public class ImageTranslationExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建两个按钮
        Button sourceButton = new Button("Source Button");
        Button targetButton = new Button("Target Button");

        // 创建图像视图并设置图像
        Image image = new Image(getClass().getResource("/images/elden/kings/red_king.png").toExternalForm());

        ImageView imageView = new ImageView(image);

        // 设置图像视图和按钮的大小
        double imageSize = 100; // 替换成你想要的大小
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        sourceButton.setMinSize(imageSize, imageSize);
        targetButton.setMinSize(imageSize, imageSize);

        // 将图像添加到源按钮
        sourceButton.setGraphic(imageView);

        // 创建栈面板，并添加两个按钮
        StackPane root = new StackPane();
        root.getChildren().addAll(sourceButton, targetButton);

        // 创建位移动画
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), imageView);
        transition.setToX(targetButton.getLayoutX() - sourceButton.getLayoutX()); // 设置X轴目标位置

        // 设置按钮点击事件，启动位移动画
        sourceButton.setOnAction(event -> {
            transition.play();
        });

        // 设置场景
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Image Translation Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
