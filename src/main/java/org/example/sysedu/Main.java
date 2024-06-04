package org.example.sysedu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.sysedu.controller.HomeController;
import org.example.sysedu.controller.LoadingScreenController;
import org.example.sysedu.data.LearnersDataGenerator;
import org.example.sysedu.exception.GlobalExceptionHandler;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Main.primaryStage = primaryStage;
        LearnersDataGenerator fakeDataGenerator = new LearnersDataGenerator();
        fakeDataGenerator.generateFakeLearnersData(100);

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            GlobalExceptionHandler.handleException(throwable);
        });

        // Đặt icon cho thanh taskbar

        // Tạo màn hình chính
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/page/LoadingScreen.fxml"));
            Font.loadFont(getClass().getResourceAsStream("/org/example/sysedu/font/Inter_VariableFont_slnt,wght.ttf"), 16);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            URL cssUrl = getClass().getResource("/org/example/sysedu/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("Không tìm thấy file CSS");
            }

            // Lấy controller và thiết lập stage
            LoadingScreenController controller = loader.getController();
            controller.setStage(primaryStage);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Loading");
            primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/org/example/sysedu/images/dark1.png")));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
