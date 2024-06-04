package org.example.sysedu.controller.dialog.learners;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import org.example.sysedu.utils.GenerateID;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.DatePickerValidation;
import org.example.sysedu.validation.TextFieldValidation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddLearnersDialogController {
    LearnersService learnersService = new LearnersService();
    private ManageLearnersController manageLearnersController;

    public TextField phoneNumberField;
    public TextField emailField;
    public TextArea noteField;
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

    public AddLearnersDialogController() {
    }

    public void setManageLearnersController(ManageLearnersController manageLearnersController) {
        this.manageLearnersController = manageLearnersController;
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(this::handleCloseRequest);
    }

    private void handleCloseRequest(WindowEvent windowEvent) {
    }

    @FXML
    private void initialize() {
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        maleRadioButton.setSelected(true);

        TextField editor = birthDatePicker.getEditor();

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
            try {
                LocalDate date = LocalDate.parse(newValue, formatter);
                birthDatePicker.setValue(date);
            } catch (DateTimeParseException e) {
                birthDatePicker.setValue(null);
            }
        });
    }

    private void handleDialogClose(DialogEvent dialogEvent) {
    }

    @FXML
    private void handleCreate() {
        TextFieldValidation.validateFullNameField(fullNameField, errorFullNameLabel);
        DatePickerValidation.validateBirthdayDatePicker(birthDatePicker, errorBirthDateLabel);
        TextFieldValidation.validateEmailField(emailField, errorEmailLabel);
        TextFieldValidation.validatePhoneNumberField(phoneNumberField, errorPhoneNumberLabel);

        if (errorFullNameLabel.getText().isEmpty()
                && errorBirthDateLabel.getText().isEmpty()
                && errorEmailLabel.getText().isEmpty()
                && errorPhoneNumberLabel.getText().isEmpty()) {
            try{
                learnersService.save(Learners.builder()
                        .id(GenerateID.generateID("S", learnersService.findMaxId()))
                        .fullName(fullNameField.getText())
                        .gender(genderToggleGroup.getSelectedToggle().isSelected())
                        .birthDate(birthDatePicker.getValue())
                        .phone(phoneNumberField.getText())
                        .email(emailField.getText())
                        .note(noteField.getText())
                        .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                        .isDelete(false)
                        .build());
                manageLearnersController.updateTable();
                NotificationUtil.showToastSuccess("Thêm thông tin thành công!");
            }catch (Exception e) {
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

