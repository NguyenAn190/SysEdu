package org.example.sysedu.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.sysedu.ui.CustomAlert;
import org.example.sysedu.ui.UIManager;
import org.example.sysedu.validation.FullNameValidation;
import org.example.sysedu.validation.PasswordValidation;
import org.example.sysedu.validation.UsernameValidation;

import java.io.IOException;

public class RegisterController {
    private Stage stage;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private CheckBox saveAccountCheckbox;
    @FXML
    private Label errorFullNameLabel = new Label();
    @FXML
    private Label errorUserNameLabel = new Label();
    @FXML
    private Label errorPasswordLabel = new Label();
    @FXML
    private Label errorConfirmPasswordLabel = new Label();
    public void setRegisterStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void loginAction(ActionEvent event) throws IOException {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // TODO: Xử lý đăng nhập ở đây
        String errorUserName = UsernameValidation.isValidateUsername(username);
        String errorPassword = PasswordValidation.isValidatePassword(password);
        String errorFullName = FullNameValidation.isValidateFullName(username);
        String errorConfirmPassword =  PasswordValidation.isValidateConfirmPassword(password, confirmPassword);

        if(!errorUserName.isEmpty() || !errorPassword.isEmpty() || !errorFullName.isEmpty() || !errorConfirmPassword.isEmpty()) {
            errorUserNameLabel.setText(errorUserName);
            errorPasswordLabel.setText(errorPassword);
            errorFullNameLabel.setText(errorFullName);
            errorConfirmPasswordLabel.setText(errorConfirmPassword);
        }else {
            CustomAlert.showAlertWithCustomDesign();
            Node source = (Node) event.getSource();
            Stage registerStage = (Stage) source.getScene().getWindow();
            UIManager.openLoginWindow(registerStage);
            registerStage.close();
        }
    }

    private void showAlertWithHeaderText() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully");
        alert.setHeaderText("Thành công:");
        alert.setContentText("Tạo tài khoản thành công!");

        alert.showAndWait();
    }
    @FXML
    private void registerAction(ActionEvent event) {
        // TODO: Hiển thị giao diện đăng ký
        System.out.println("Register button clicked");
    }

    @FXML
    private void forgotPasswordAction(ActionEvent event) {
        // TODO: Hiển thị giao diện quên mật khẩu
        System.out.println("Forgot Password button clicked");
    }

    @FXML
    private void forgotPasswordAction(MouseEvent event) {
        System.out.println("this is quen mạt khảu");
    }

    @FXML
    private void redirectToLoginAction(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage loginStage = (Stage) source.getScene().getWindow();
        UIManager.openLoginWindow(loginStage);

        loginStage.close();
    }
}
