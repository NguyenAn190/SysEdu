package org.example.sysedu.controller.dialog.students;

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
import org.example.sysedu.controller.components.ManageStudentsController;
import org.example.sysedu.entity.Students;
import org.example.sysedu.service.StudentsService;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.NumberValidation;

import java.util.Objects;
import java.util.Optional;

public class UpdateStudentsDialogController {
    StudentsService studentsService = new StudentsService();
    public Button deleteButton;
    public TextField scoreField;
    public Label errorScoreLabel;
    TopicsService topicsService = new TopicsService();
    private ManageStudentsController manageStudentsController;

    Students student = new Students();
    @Setter
    private Stage dialogStage;
    @Getter
    private boolean confirmed = false;

    public void UpdateStudentsDialogController() {
    }

    public void setManageStudentsController(ManageStudentsController manageStudentsController) {
        this.manageStudentsController = manageStudentsController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(this::handleCloseRequest);
    }

    private void handleCloseRequest(WindowEvent windowEvent) {
    }

    @FXML
    private void initialize() {

    }

    public void initData(Students selectedStudent) {

        if (selectedStudent != null) {

            student = selectedStudent;


            scoreField.setText(student.getScore().toString());
        }
    }

    private void handleDialogClose(DialogEvent dialogEvent) {
    }

    @FXML
    private void handleUpdate() {
        NumberValidation.scoreValidation(scoreField, errorScoreLabel);
        if (Objects.equals(errorScoreLabel.getText(), "")) {
            try {
                studentsService.update(
                        Students.builder()
                                .id(student.getId())
                                .score(Float.valueOf(scoreField.getText()))
                                .coursesId(student.getCoursesId())
                                .learnersId(student.getLearnersId())
                                .dateCreated(student.getDateCreated())
                                .build()
                );

                dialogStage.hide();

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
                    Platform.runLater(() -> dialogStage.close());
                }));
                timeline.setCycleCount(1);
                timeline.play();
                manageStudentsController.updateTable();
                NotificationUtil.showToastSuccess("Cập nhật thông tin thành công!");
            } catch (Exception e) {
                NotificationUtil.showToastError("Lỗi không xác định");
            }
        }
        confirmed = true;
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa học viên");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa học viên này?");
        alert.setContentText("Dữ liệu sẽ không thể khôi phục khi bạn xóa");

        ButtonType buttonTypeYes = new ButtonType("Đồng ý", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeCancel = new ButtonType("Đóng", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes){
            studentsService.delete(student.getId());

            dialogStage.hide();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
                Platform.runLater(() -> dialogStage.close());
            }));
            timeline.setCycleCount(1);
            timeline.play();
            manageStudentsController.updateTable();
            NotificationUtil.showToastSuccess("Xóa học viên thành công!");
        } else {

        }
        confirmed = true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

}

