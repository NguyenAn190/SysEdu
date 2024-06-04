package org.example.sysedu.controller.dialog.learners;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.example.sysedu.controller.components.ManageLearnersController;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.service.LearnersService;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.DatePickerValidation;
import org.example.sysedu.validation.TextFieldValidation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class UpdateLearnersDialogController {
    private ManageLearnersController manageLearnersController;
    LearnersService learnersService = new LearnersService();
    public Button restoreButton;
    public Button deleteButton;
    public TextField phoneNumberField;
    public TextField emailField;
    public TextField noteField;
    public DatePicker birthDatePicker;
    public Label errorFullNameLabel;
    public Label errorBirthDateLabel;
    public Label errorPhoneNumberLabel;
    public Label errorEmailLabel;
    @FXML
    private TextField fullNameField;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private ToggleGroup genderToggleGroup;
    @Setter
    private Stage dialogStage;
    @Getter
    private boolean confirmed = false;
    public DialogPane dialogPane;

    public UpdateLearnersDialogController() {
    }

    public void setManageLearnersController(ManageLearnersController manageLearnersController) {
        this.manageLearnersController = manageLearnersController;
    }

    Learners learners = new Learners();

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
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);

        // Lấy TextField bên trong DatePicker
        TextField editor = birthDatePicker.getEditor();

        // Định dạng ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Chuyển đổi giá trị LocalDate sang chuỗi
        birthDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, formatter);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        });


        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(newValue, formatter);
                    birthDatePicker.setValue(date);
                } catch (DateTimeParseException e) {
                    // Nếu có lỗi khi phân tích cú pháp, khôi phục giá trị cũ
                    editor.setText(oldValue);
                }
            } else {
                birthDatePicker.setValue(null);
            }
        });
    }

    public void initData(Learners selectedLearner) {
        learners = selectedLearner;
        if (selectedLearner != null) {
            fullNameField.setText(learners.getFullName());
            if (learners.getGender()) {
                maleRadioButton.setSelected(true);
            } else {
                femaleRadioButton.setSelected(true);
            }
            birthDatePicker.setValue(learners.getBirthDate());
            phoneNumberField.setText(learners.getPhone());
            emailField.setText(learners.getEmail());
            noteField.setText(learners.getNote());

            if (learners.getIsDelete()) {
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
        TextFieldValidation.validateFullNameField(fullNameField, errorFullNameLabel);
        DatePickerValidation.validateBirthdayDatePicker(birthDatePicker, errorBirthDateLabel);
        TextFieldValidation.validateEmailUpdateField(emailField, errorEmailLabel);
        TextFieldValidation.validatePhoneNumberUpdateField(phoneNumberField, errorPhoneNumberLabel);

        if (Objects.equals(errorFullNameLabel.getText(), "")
                && Objects.equals(errorBirthDateLabel.getText(), "")
                && Objects.equals(errorEmailLabel.getText(), "")
                && Objects.equals(errorPhoneNumberLabel.getText(), "")) {
            try {
                learnersService.update(Learners.builder()
                        .id(learners.getId())
                        .fullName(fullNameField.getText())
                        .gender(genderToggleGroup.getSelectedToggle().isSelected())
                        .birthDate(birthDatePicker.getValue())
                        .phone(phoneNumberField.getText())
                        .email(emailField.getText())
                        .note(noteField.getText())
                        .dateCreated(learners.getDateCreated())
                        .isDelete(learners.getIsDelete())
                        .build());
                manageLearnersController.updateTable();
                NotificationUtil.showToastSuccess("Cập nhật thông tin thành công!");
            }catch (Exception ex) {
                System.out.println( ex.getMessage());
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
        Platform.runLater(() -> manageLearnersController.updateTable());
        learnersService.updateStatusDeleteTrue(learners.getId());
        NotificationUtil.showToastSuccess("Xóa người học thành công!");

        dialogStage.hide();


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(() -> dialogStage.close());
        }));
        timeline.setCycleCount(1);
        timeline.play();
        confirmed = true;
    }

    public void handleRestore(ActionEvent actionEvent) {
        Platform.runLater(() -> manageLearnersController.updateTable());
        learnersService.updateStatusDeleteFalse(learners.getId());
        NotificationUtil.showToastSuccess("Khôi phục người học thành công!");

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

