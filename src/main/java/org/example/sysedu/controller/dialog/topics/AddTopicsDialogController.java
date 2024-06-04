package org.example.sysedu.controller.dialog.topics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.sysedu.controller.components.ManageTopicsController;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.utils.GenerateID;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.FileValidation;
import org.example.sysedu.validation.NumberValidation;
import org.example.sysedu.validation.TextFieldValidation;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AddTopicsDialogController {

    TopicsService topicsService = new TopicsService();
    public Label errorImage;
    private ManageTopicsController manageTopicsController;
    public TextField topicNameFiled;
    public ImageView imageView;
    public Label errorTopicNameLabel;
    public TextField timeFiled;
    public Label errorTimeLabel;
    public Label errorTuitionFeeLabel;
    public TextArea descriptionField;
    public TextField tuitionFeeFiled;

    @Setter
    private Stage dialogStage;
    @Getter
    private boolean confirmed = false;

    public void AddTopicsDialogController() {
    }

    public void setManageTopicsController(ManageTopicsController manageTopicsController) {
        this.manageTopicsController = manageTopicsController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(this::handleCloseRequest);
    }

    private void handleCloseRequest(WindowEvent windowEvent) {
    }

    @FXML
    private void initialize() {
        String defaultImage = "D:\\FPT Polytechnic\\SysEdu\\SysEdu\\src\\main\\resources\\org\\example\\sysedu\\images\\default_image.png";
        Image img = new Image(defaultImage);
        imageView.setImage(img);

    }

    private void handleDialogClose(DialogEvent dialogEvent) {
    }

    @FXML
    private void handleCreate() {
        TextFieldValidation.validationTopicNameCreatedField(topicNameFiled, errorTopicNameLabel);
        NumberValidation.validationTimeField(timeFiled, errorTimeLabel);
        NumberValidation.validationTuitionFeeField(tuitionFeeFiled, errorTuitionFeeLabel);
        FileValidation.validationAvatar(imageView, errorImage);

        if (errorTopicNameLabel.getText().isEmpty() &&
                errorTimeLabel.getText().isEmpty() &&
                errorTuitionFeeLabel.getText().isEmpty() &&
                errorImage.getText().isEmpty()) {
            try {
                topicsService.save(
                        Topics.builder()
                                .id(GenerateID.generateID("T", topicsService.findMaxId()))
                                .topicName(topicNameFiled.getText())
                                .time(Integer.valueOf(timeFiled.getText()))
                                .tuitionFee(BigDecimal.valueOf(Long.parseLong(tuitionFeeFiled.getText())))
                                .description(descriptionField.getText())
                                .avatar(imageView.getImage().getUrl())
                                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                                .isDelete(false)
                                .build()
                );
                manageTopicsController.updateTable();
                NotificationUtil.showToastSuccess("Thêm thông tin thành công!");
                System.out.println("Lưu thông tin thành công");
            } catch (Exception e) {
                NotificationUtil.showToastError("Lỗi không xác định!");
                System.out.println(e.getMessage());
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
    private void handleUpdate() {
        System.out.println("udpate");
        confirmed = true;
    }

    @FXML
    private void handleDelete() {
        System.out.println("delete");
        confirmed = true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void handleAddImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) imageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }
}

