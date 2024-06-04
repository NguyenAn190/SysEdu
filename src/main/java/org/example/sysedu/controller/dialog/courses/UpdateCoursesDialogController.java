package org.example.sysedu.controller.dialog.courses;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.example.sysedu.controller.components.ManageCoursesController;
import org.example.sysedu.entity.Courses;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.ComboboxValidation;
import org.example.sysedu.validation.DatePickerValidation;
import org.example.sysedu.validation.NumberValidation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class UpdateCoursesDialogController {
    public Button restoreButton;
    public Button deleteButton;
    public TextField courseNameField;
    public Label errorCourseNameLabel;
    Courses courses = new Courses();
    public Label errorTimeLabel;
    CourseService courseService = new CourseService();
    TopicsService topicsService = new TopicsService();

    private ManageCoursesController manageCoursesController;
    public TextField timeField;
    public TextArea descriptionField;
    public DialogPane dialogPane;
    public DatePicker declarationDatePicker;
    public Label errorDeclarationDateLabel;
    public ComboBox topicComboBox;
    public Label errorTopicComboBox;
    public Label errorTuitionFeeLabel;

    public void setManageCoursesController(ManageCoursesController manageCoursesController) {
        this.manageCoursesController = manageCoursesController;
    }
    @Setter
    private Stage dialogStage;
    @Getter
    private boolean confirmed = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(this::handleCloseRequest);
    }
    private void handleCloseRequest(WindowEvent windowEvent) {}



    @FXML
    private void initialize() {

        List<Topics> topics = topicsService.findAll();
        List<String> listTopicName = topics.stream().map(Topics::getTopicName).toList();
        ObservableList<String> list = FXCollections.observableArrayList(listTopicName);
        topicComboBox.setItems(list);


        // Lấy TextField bên trong DatePicker
        TextField editor = declarationDatePicker.getEditor();

        // Định dạng ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Chuyển đổi giá trị LocalDate sang chuỗi
        declarationDatePicker.setConverter(new StringConverter<LocalDate>() {
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
                declarationDatePicker.setValue(date);
            } catch (DateTimeParseException e) {
                declarationDatePicker.setValue(null);
            }
        });

    }

    public void initData(Courses selectedLearner) {
        courses = selectedLearner;
        if (selectedLearner != null) {
            declarationDatePicker.setValue(selectedLearner.getDeclarationDate());
            descriptionField.setText(selectedLearner.getDescription());
            timeField.setText(selectedLearner.getTime().toString());

            courseNameField.setText(courses.getCourseName());
            topicComboBox.getSelectionModel().select(courses.getTopics().getTopicName());

            if (courses.getIsDelete()) {
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

    private void handleDialogClose(DialogEvent dialogEvent) {
    }


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }




    public void handleUpdate(ActionEvent actionEvent) {
        ComboboxValidation.topicsValidation(topicComboBox, errorTopicComboBox);
        DatePickerValidation.validateDeclarationDatePicker(declarationDatePicker, errorDeclarationDateLabel);
        NumberValidation.validationTimeField(timeField, errorTimeLabel);

        if (errorTopicComboBox.getText().isEmpty() &&
                errorDeclarationDateLabel.getText().isEmpty() &&
                errorTimeLabel.getText().isEmpty()) {
            String topicId = topicsService.findByName((String) topicComboBox.getValue());
            try {
                courseService.update(
                        Courses.builder()
                                .id(courses.getId())
                                .topicId(topicId)
                                .courseName(courseNameField.getText())
                                .declarationDate(declarationDatePicker.getValue())
                                .time(Integer.valueOf(timeField.getText()))
                                .tuitionFee(courses.getTuitionFee())
                                .dateCreated(courses.getDateCreated())
                                .description(descriptionField.getText())
                                .isDelete(courses.getIsDelete())
                                .usersId("U000")
                                .build());
                manageCoursesController.updateTable();
                NotificationUtil.showToastSuccess("Cập nhật thông tin thành công!");
            }catch (Exception e ) {
                NotificationUtil.showToastError("Lỗi không xác định!");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String stackTrace = sw.toString();

                System.out.println(stackTrace);
            }
        }
        dialogStage.hide();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(() -> dialogStage.close());
        }));
        timeline.setCycleCount(1);
        timeline.play();

        confirmed = true;
    }

    public void handleDelete(ActionEvent actionEvent) {
        Platform.runLater(() -> manageCoursesController.updateTable());
        courseService.updateStatusDeleteTrue(courses.getId());
        NotificationUtil.showToastSuccess("Xóa người học thành công!");

        dialogStage.hide();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(() -> dialogStage.close());
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void handleRestore(ActionEvent actionEvent) {
        Platform.runLater(() -> manageCoursesController.updateTable());
        courseService.updateStatusDeleteFalse(courses.getId());
        NotificationUtil.showToastSuccess("Khôi phục người học thành công!");

        dialogStage.hide();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(() -> dialogStage.close());
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}

