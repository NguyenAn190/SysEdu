package org.example.sysedu.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.sysedu.controller.HomeController;
import org.example.sysedu.controller.LoginController;
import org.example.sysedu.controller.RegisterController;

import java.io.IOException;
import java.net.URL;

public class UIManager {
    public static void openLoginWindow(Stage registerStage) {

        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/org/example/sysedu/fxml/page/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            URL cssUrl = UIManager.class.getResource("/org/example/sysedu/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("Không tìm thấy file CSS");
            }

            Image image = new Image("D:\\FPT Polytechnic\\SysEdu\\SysEdu\\src\\main\\resources\\org\\example\\sysedu\\images\\dark1.png");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Đăng nhập");
            stage.getIcons().add(image);
            LoginController loginController = loader.getController();
            loginController.setLoginStage(registerStage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openHomeWindow(Stage loginStage) {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/org/example/sysedu/fxml/page/HomeView.fxml")); // Sửa đường dẫn file FXML thành HomeView.fxml
            Parent root = loader.load();
            Scene scene = new Scene(root);

            URL cssUrl = UIManager.class.getResource("/org/example/sysedu/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("Không tìm thấy file CSS");
            }

            Image image = new Image("D:\\FPT Polytechnic\\SysEdu\\SysEdu\\src\\main\\resources\\org\\example\\sysedu\\images\\dark1.png");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Trang chủ");
            stage.getIcons().add(image);
            HomeController homeController = loader.getController();
            homeController.setLoginStage(loginStage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
