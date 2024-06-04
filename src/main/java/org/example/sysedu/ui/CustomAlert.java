package org.example.sysedu.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomAlert {

    public static void showAlertWithCustomDesign() {
        Stage customAlert = new Stage();
        customAlert.initModality(Modality.APPLICATION_MODAL);
        customAlert.setTitle("Thành công");

        Label label = new Label("Bạn đã tạo tài khoản thành công!");

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> customAlert.close());

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(label, okButton);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox, 250, 150);
        customAlert.setScene(scene);

        // Set background color to white
        vBox.setStyle("-fx-background-color: white;");

        // Add custom icon
        // Replace "/path/to/your/success/icon.png" with the actual path to your icon file
        // Make sure the size of the icon is suitable
        StackPane iconPane = new StackPane();
        iconPane.setPrefSize(64, 64);
        Label iconLabel = new Label();
        iconLabel.setStyle("-fx-background-image: url('/org/example/sysedu/images/dark.png');");
        iconPane.getChildren().add(iconLabel);
        vBox.getChildren().add(0, iconPane);

        customAlert.showAndWait();
    }
}

