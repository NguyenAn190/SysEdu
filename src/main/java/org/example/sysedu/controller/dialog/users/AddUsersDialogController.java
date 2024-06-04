package org.example.sysedu.controller.dialog.users;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.sysedu.controller.components.ManageUsersController;
import org.example.sysedu.entity.Users;
import org.example.sysedu.enums.Role;
import org.example.sysedu.service.UsersService;
import org.example.sysedu.utils.GenerateID;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.utils.PasswordManager;
import org.example.sysedu.validation.TextFieldValidation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class AddUsersDialogController {

    public RadioButton adminRadioButton;
    public RadioButton userRadioButton;
    UsersService usersService = new UsersService();
    private ManageUsersController manageUsersController;
    public TextField usernameField;
    public DialogPane dialogPane;
    public Label errorUsernameLabel;
    public PasswordField passwordField;
    public Label errorPasswordLabel;
    public TextField fullNameField;
    public TextArea noteField;
    public Label errorFullNameLabel;
    private ToggleGroup roleToggleGroup;
    @Setter
    private Stage dialogStage;
    @Getter
    private boolean confirmed = false;

    public AddUsersDialogController() {
    }

    public void setManageUsersController(ManageUsersController manageUsersController) {
        this.manageUsersController = manageUsersController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(this::handleCloseRequest);
    }

    private void handleCloseRequest(WindowEvent windowEvent) {
    }

    @FXML
    private void initialize() {
        roleToggleGroup = new ToggleGroup();
        userRadioButton.setToggleGroup(roleToggleGroup);
        adminRadioButton.setToggleGroup(roleToggleGroup);
        userRadioButton.setSelected(true);
    }

    private void handleDialogClose(DialogEvent dialogEvent) {
    }

    @FXML
    private void handleCreate() {
        TextFieldValidation.validationUsername(usernameField, errorUsernameLabel);
        TextFieldValidation.validationPasswords(passwordField, errorPasswordLabel);
        TextFieldValidation.validationNameField(fullNameField, errorFullNameLabel);
        String roleSelect = roleToggleGroup.getSelectedToggle().getUserData().toString();

        Role role = Objects.equals(roleSelect, Role.USER.name()) ? Role.USER : Role.ADMIN;

        if (errorUsernameLabel.getText().isEmpty()
                && errorPasswordLabel.getText().isEmpty()
                && errorFullNameLabel.getText().isEmpty()) {
            try {
                usersService.save(Users.builder()
                        .id(GenerateID.generateID("S", usersService.findMaxId()))
                        .fullName(fullNameField.getText())
                        .username(usernameField.getText())
                        .password(PasswordManager.hashPassword(passwordField.getText()))
                        .role(role)
                        .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                        .isDelete(false)
                        .build());
                manageUsersController.updateTable();
                NotificationUtil.showToastSuccess("Thêm tài khoản thành công!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                NotificationUtil.showToastError("Lỗi chưa xác định");

            }

            dialogStage.hide();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
                Platform.runLater(() -> dialogStage.close());
            }));
            timeline.setCycleCount(1);
            timeline.play();
        }

        confirmed = true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


}

