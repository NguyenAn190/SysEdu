package org.example.sysedu.controller.dialog.users;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.sysedu.controller.components.ManageUsersController;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.entity.Users;
import org.example.sysedu.enums.Role;
import org.example.sysedu.service.UsersService;
import org.example.sysedu.utils.GenerateID;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.TextFieldValidation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UpdateUsersDialogController {
    private ManageUsersController manageUsersController;
    UsersService usersService = new UsersService();
    public TextField usernameField;
    public Label errorUsernameLabel;
    public RadioButton adminRadioButton;
    public RadioButton userRadioButton;
    public Button restoreButton;
    public Button deleteButton;
    public Label errorFullNameLabel;
    public TextField fullNameField;
    public DialogPane dialogPane;
    @Setter
    private Stage dialogStage;
    @Getter
    private boolean confirmed = false;

    private ToggleGroup roleToggleGroup;
    public UpdateUsersDialogController() {
    }

    public void setManageUsersController(ManageUsersController manageUsersController) {
        this.manageUsersController = manageUsersController;
    }

    Users user = new Users();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(this::handleCloseRequest);
    }

    private void handleCloseRequest(WindowEvent windowEvent) {
    }

    private void handleDialogClose(DialogEvent dialogEvent) {
    }

    @FXML
    private void initialize() {
        roleToggleGroup = new ToggleGroup();
        userRadioButton.setToggleGroup(roleToggleGroup);
        adminRadioButton.setToggleGroup(roleToggleGroup);
    }

    public void initData(Users selectedLearner) {
        user = selectedLearner;
        if (selectedLearner != null) {

            if (user.getRole() == Role.ADMIN) {
                adminRadioButton.setSelected(true);
            } else {
                userRadioButton.setSelected(true);
            }
            fullNameField.setText(user.getFullName());
            usernameField.setText(user.getUsername());

            if (user.getIsDelete()) {
                restoreButton.setVisible(true);
                restoreButton.setManaged(true);
                deleteButton.setVisible(false);
                deleteButton.setManaged(false);
            } else {
                restoreButton.setVisible(false);
                restoreButton.setManaged(false);
                deleteButton.setVisible(true);
                deleteButton.setManaged(true);
            }
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        TextFieldValidation.validationUsername(usernameField, errorUsernameLabel);
        TextFieldValidation.validationNameField(fullNameField, errorFullNameLabel);
        String roleSelect = roleToggleGroup.getSelectedToggle().getUserData().toString();

        Role role;
        try {
            role = Role.valueOf(roleSelect);
        } catch (IllegalArgumentException e) {
            role = Role.USER;
        }

        System.out.println(usernameField.getText() + role);
        if (Objects.equals(errorUsernameLabel.getText(), "")
                && Objects.equals(errorFullNameLabel.getText(), "")) {
            try {
                List<Users> usersList = usersService.findAllByRole(Role.ADMIN);

                if (usersList.size() <= 1 && usersList.get(0).getUsername().equals(user.getUsername())) {
                    NotificationUtil.showToastError("Bạn không thể thay đổi thông tin tài khoản này!");
                } else {
                    usersService.update( Users.builder()
                            .id(user.getId())
                            .fullName(fullNameField.getText())
                            .username(usernameField.getText())
                            .password(user.getPassword())
                            .role(role)
                            .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                            .isDelete(user.getIsDelete())
                            .build());
                    manageUsersController.updateTable();
                    NotificationUtil.showToastSuccess("Cập nhật thông tin thành công!");
                }
            }catch (Exception ex) {
                NotificationUtil.showToastError("Lỗi không xác định!");
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
    private void handleDelete() {
        List<Users> usersList = usersService.findAllByRole(Role.ADMIN);

        if (usersList.size() <= 1 && usersList.get(0).getUsername().equals(user.getUsername())) {
            NotificationUtil.showToastError("Bạn không thể xóa tài khoản này!");
        } else {
            Platform.runLater(() -> manageUsersController.updateTable());
            usersService.updateStatusDeleteTrue(user.getId());
            NotificationUtil.showToastSuccess("Xóa tài khoản thành công!");
        }

        dialogStage.hide();
        closeDialogAfterDelay();
        confirmed = true;
    }

    private void closeDialogAfterDelay() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(() -> dialogStage.close());
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void handleRestore(ActionEvent actionEvent) {
        Platform.runLater(() -> manageUsersController.updateTable());
        usersService.updateStatusDeleteFalse(user.getId());
        NotificationUtil.showToastSuccess("Khôi phục tài khoản thành công!");

        dialogStage.hide();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(() -> dialogStage.close());
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}

