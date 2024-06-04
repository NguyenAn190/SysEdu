package org.example.sysedu.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.sysedu.dto.usersDTO.UserSession;
import org.example.sysedu.entity.Users;
import org.example.sysedu.service.UsersService;
import org.example.sysedu.ui.UIManager;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.utils.PasswordManager;
import org.example.sysedu.validation.PasswordValidation;
import org.example.sysedu.validation.UsernameValidation;

import java.io.IOException;
import java.util.Optional;
import java.time.LocalDateTime;

public class LoginController {
    UsersService usersService = new UsersService();
    Stage stage = new Stage();
    public TextField usernameField;
    public PasswordField passwordField;
    public CheckBox saveAccountCheckbox;
    public Label errorUserNameLabel = new Label();
    public Label errorPasswordLabel = new Label();

    public void setLoginStage(Stage stage) {
        this.stage = stage;
    }


    /**
     * Phương thức sử lí đăng nhập
     * @param event sự kiện click vào nút đăng nhập
     * @throws IOException ném ngoại lệ nếu có lỗi
     */
    @FXML
    private void loginAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // TODO: Xử lý đăng nhập ở đây
        String errorUserName = UsernameValidation.isValidateUsername(username);
        String errorPassword = PasswordValidation.isValidatePassword(password);

        if(!errorUserName.isEmpty() || !errorPassword.isEmpty()) {
            errorUserNameLabel.setText(errorUserName);
            errorPasswordLabel.setText(errorPassword);
        }else {
            Optional<Users> user = usersService.findByUsername(username);
            if(user.isPresent()) {
                if(PasswordManager.checkPassword(password, user.get().getPassword())) {
                    LocalDateTime expirationTime = LocalDateTime.now().plusWeeks(1);

                    // Lưu thông tin người dùng vào UserSession và tệp tạm
                    UserSession userSession = UserSession.getInstance();
                    userSession.setUsername(user.get().getUsername());
                    userSession.setExpirationTime(expirationTime);
                    userSession.saveSessionToFile();

                    Node source = (Node) event.getSource();
                    Stage loginStage = (Stage) source.getScene().getWindow();
                    UIManager.openHomeWindow(loginStage);

                    loginStage.close();
                } else {
                    errorUserNameLabel.setText("");
                    errorPasswordLabel.setText("Tên đăng nhập hoặc mật khẩu không hợp lệ!");
                }
            }else {
                errorUserNameLabel.setText("");
                errorPasswordLabel.setText("Tên đăng nhập hoặc mật khẩu không hợp lệ!");
            }
        }
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
    private void redirectToRegisterAction(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage loginStage = (Stage) source.getScene().getWindow();


        loginStage.close();
    }
}
