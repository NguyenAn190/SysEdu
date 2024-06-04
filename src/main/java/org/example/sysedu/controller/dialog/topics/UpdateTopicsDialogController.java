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
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.FileValidation;
import org.example.sysedu.validation.NumberValidation;
import org.example.sysedu.validation.TextFieldValidation;

import java.io.File;
import java.math.BigDecimal;

public class UpdateTopicsDialogController {
    TopicsService topicsService = new TopicsService();
    public Button deleteButton;
    public Button restoreButton;
    Topics topics = new Topics();
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

    public void initData(Topics selectedTopics) {

        if (selectedTopics != null) {
            try {
                Image image = new Image(selectedTopics.getAvatar());
                System.out.println(selectedTopics.getAvatar());
                Platform.runLater(() -> {
                    imageView.setImage(image);
                });
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid image path: " + selectedTopics.getAvatar());
                e.printStackTrace();
            }

            topics = selectedTopics;
            System.out.println(topics.getTuitionFee().intValue());
            int tuitionFee  = topics.getTuitionFee().intValue();

            topicNameFiled.setText(topics.getTopicName());
            timeFiled.setText(String.valueOf(topics.getTime()));
            tuitionFeeFiled.setText(String.valueOf(tuitionFee));
            descriptionField.setText(topics.getDescription());

            if (topics.getIsDelete()) {
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
    private void handleUpdate() {
        TextFieldValidation.validationNameField(topicNameFiled, errorTopicNameLabel);
        NumberValidation.validationTimeField(timeFiled, errorTimeLabel);
        NumberValidation.validationTuitionFeeField(tuitionFeeFiled, errorTuitionFeeLabel);
        FileValidation.validationAvatar(imageView, errorImage);

        if (errorTopicNameLabel.getText().isEmpty() &&
                errorTimeLabel.getText().isEmpty() &&
                errorTuitionFeeLabel.getText().isEmpty() &&
                errorImage.getText().isEmpty()) {
            try {
                topicsService.update(
                        Topics.builder()
                                .id(topics.getId())
                                .topicName(topicNameFiled.getText())
                                .time(Integer.valueOf(timeFiled.getText()))
                                .tuitionFee(BigDecimal.valueOf(Long.parseLong(tuitionFeeFiled.getText())))
                                .description(descriptionField.getText())
                                .avatar(imageView.getImage().getUrl())
                                .dateCreated(topics.getDateCreated())
                                .isDelete(topics.getIsDelete())
                                .build()
                );
                manageTopicsController.updateTable();
                NotificationUtil.showToastSuccess("Cập nhật thông tin thành công!");
            } catch (Exception e) {
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
        Platform.runLater(() -> manageTopicsController.updateTable());
        topicsService.updateStatusDeleteTrueById(topics.getId());
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
        Platform.runLater(() -> manageTopicsController.updateTable());
        topicsService.updateStatusDeleteFalseById(topics.getId());
        NotificationUtil.showToastSuccess("Khôi phục người học thành công!");

        dialogStage.hide();


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(() -> dialogStage.close());
        }));
        timeline.setCycleCount(1);
        timeline.play();
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

