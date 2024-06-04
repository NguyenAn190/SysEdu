package org.example.sysedu.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class NotificationUtil {

    public static void showToastSuccess(String message) {
        Platform.runLater(() -> {
            Stage toastStage = new Stage();
            toastStage.initStyle(StageStyle.TRANSPARENT);

            // Tạo ImageView cho biểu tượng thành công
            ImageView imageView = new ImageView(new Image("D:\\FPT Polytechnic\\SysEdu\\SysEdu\\src\\main\\resources\\org\\example\\sysedu\\gif\\icons8-success.gif"));
            imageView.setFitWidth(35);
            imageView.setFitHeight(35);

            // Tạo Label cho thông báo
            Label label = new Label(message);
            label.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 10px; " +
                    "-fx-border-width: 0 0 0 5px; " +
                    "-fx-border-color: green;");

            // Tạo HBox chứa ImageView và Label
            HBox hbox = new HBox(imageView, label);
            hbox.setSpacing(20);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-padding: 10px 25px 10px 30px;");

            StackPane root = new StackPane(hbox);
            root.setStyle("-fx-background-color: transparent;");
            root.setAlignment(Pos.BOTTOM_CENTER);

            Scene scene = new Scene(root, 500, 100);
            scene.setFill(Color.TRANSPARENT);

            toastStage.setScene(scene);

            Screen screen = Screen.getPrimary();
            javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

            toastStage.setX(bounds.getMaxX() - scene.getWidth() - 20);
            toastStage.setY(bounds.getMinY() + 20);
            toastStage.show();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> toastStage.close()));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    public static void showToastError(String message) {
        Platform.runLater(() -> {
            Stage toastStage = new Stage();
            toastStage.initStyle(StageStyle.TRANSPARENT);

            // Tạo ImageView cho biểu tượng thành công
            ImageView imageView = new ImageView(new Image("D:\\FPT Polytechnic\\SysEdu\\SysEdu\\src\\main\\resources\\org\\example\\sysedu\\gif\\icons8-error.gif"));
            imageView.setFitWidth(35);
            imageView.setFitHeight(35);

            // Tạo Label cho thông báo
            Label label = new Label(message);
            label.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 10px; " +
                    "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 0 0 0 5px; " +
                    "-fx-border-color: red;");

            // Tạo HBox chứa ImageView và Label
            HBox hbox = new HBox(imageView, label);
            hbox.setSpacing(20);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-padding: 10px 25px 10px 30px;");

            StackPane root = new StackPane(hbox);
            root.setStyle("-fx-background-color: transparent;");
            root.setAlignment(Pos.BOTTOM_CENTER);

            Scene scene = new Scene(root, 500, 100);
            scene.setFill(Color.TRANSPARENT);

            toastStage.setScene(scene);
            // Lấy kích thước của màn hình
            Screen screen = Screen.getPrimary();
            javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

            // Tính toán vị trí của thông báo ở góc trên bên phải
            toastStage.setX(bounds.getMaxX() - scene.getWidth() - 20); // 20 là khoảng cách từ mép phải
            toastStage.setY(bounds.getMinY() + 20); // 20 là khoảng cách từ mép trên
            toastStage.show();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> toastStage.close()));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    public static Stage loading(Stage loadingStage) {
        loadingStage.initStyle(StageStyle.TRANSPARENT);

        // Tạo ImageView cho logo
        ImageView imageView = new ImageView(new Image("file:D:\\FPT Polytechnic\\SysEdu\\SysEdu\\src\\main\\resources\\org\\example\\sysedu\\gif\\icons8-success.gif"));
        imageView.setFitWidth(200); // Điều chỉnh kích thước của logo nếu cần
        imageView.setFitHeight(200);

        // Tạo StackPane chứa ImageView
        StackPane root = new StackPane(imageView);
        root.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-padding: 20px;");
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 900, 1440);
        scene.setFill(Color.TRANSPARENT);

        loadingStage.setScene(scene);

        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

        loadingStage.setX((bounds.getWidth() - scene.getWidth()) / 2);
        loadingStage.setY((bounds.getHeight() - scene.getHeight()) / 2);

        return loadingStage;
    }


}
