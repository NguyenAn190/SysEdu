package org.example.sysedu.controller.dialog.students;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.sysedu.controller.components.ManageStudentsController;
import org.example.sysedu.dto.learners.LearnersDTO;
import org.example.sysedu.entity.Courses;
import org.example.sysedu.entity.Students;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.LearnersService;
import org.example.sysedu.service.StudentsService;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.utils.NotificationUtil;
import org.example.sysedu.validation.ComboboxValidation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddStudentsDialogController {
    LearnersService learnersService = new LearnersService();
    private ManageStudentsController manageStudentsController;
    public ComboBox courseCombobox;
    public Label errorTopicComboBoxLabel;
    public Label errorCourseComboboxLabel;
    public Label errorLearnersTableLabel;
    TopicsService topicsService = new TopicsService();
    CourseService courseService = new CourseService();
    StudentsService studentsService = new StudentsService();
    private Set<String> selectedLearners = new HashSet<>();

    public DialogPane dialogPane;
    public TableView learnersTable;
    public ComboBox topicCombobox;
    public void setManageStudentsController(ManageStudentsController manageStudentsController) {
        this.manageStudentsController = manageStudentsController;
    }
    @Setter
    private Stage dialogStage;
    @Getter
    private boolean confirmed = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(this::handleCloseRequest);
    }

    private void handleCloseRequest(WindowEvent windowEvent) {
    }

    @FXML
    private void initialize() {

        TableColumn<LearnersDTO, Boolean> selectCol = new TableColumn<>("Chọn");
        selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setEditable(true);

        TableColumn<LearnersDTO, String> id = new TableColumn<>("Mã NH");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<LearnersDTO, String> fullName = new TableColumn<>("Họ và tên");
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<LearnersDTO, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<LearnersDTO, String> phone = new TableColumn<>("Số điện thoại");
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<LearnersDTO, Boolean> gender = new TableColumn<>("Giới tính");
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        gender.setCellFactory(column -> new TableCell<LearnersDTO, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Nam" : "Nữ");
                }
            }
        });

        learnersTable.getColumns().addAll(selectCol, id, fullName, email, phone, gender);

        List<LearnersDTO> learners = learnersService.findAllByStatusDelete();
        learners.forEach(learner -> {
            if (selectedLearners == null) {
                learner.setSelected(false);
            } else {
                learner.setSelected(selectedLearners.contains(learner.getId()));
            }

            learner.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        selectedLearners.add(learner.getId());
                    } else {
                        selectedLearners.remove(learner.getId());
                    }
                    System.out.println("Selected Learner ID: " + selectedLearners);
                }
            });
        });

        ObservableList<LearnersDTO> observableLearners = FXCollections.observableArrayList(learners);
        learnersTable.setItems(observableLearners);
        learnersTable.setEditable(true);
        learnersTable.setDisable(true);
        learnersTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        List<String> topicsName = topicsService.findAllByIsDeleteFalse();
        ObservableList<String> observableTopic = FXCollections.observableArrayList(topicsName);
        topicCombobox.setItems(observableTopic);

    }

    private void handleDialogClose(DialogEvent dialogEvent) {
        System.out.println("Đóng");
    }

    @FXML
    private void handleCreate() {
        ComboboxValidation.courseValidation(courseCombobox, errorCourseComboboxLabel);
        ComboboxValidation.topicsValidation(topicCombobox, errorTopicComboBoxLabel);
        ComboboxValidation.learnersValidation(selectedLearners.stream().toList(), errorLearnersTableLabel);

        try{
            if (errorCourseComboboxLabel.getText().isEmpty() &&
                    errorCourseComboboxLabel.getText().isEmpty() &&
                    errorLearnersTableLabel.getText().isEmpty()) {
                Courses course = courseService.findCourseByName(courseCombobox.getValue().toString());
                selectedLearners.stream().forEach(selectedLearner -> {
                    studentsService.save(
                            Students.builder()
                                    .coursesId(course.getId())
                                    .learnersId(selectedLearner)
                                    .score(0.0f)
                                    .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                                    .build()
                    );
                });
            }
            manageStudentsController.updateTable();
            NotificationUtil.showToastSuccess("Thêm thông tin thành công!");
        }catch (Exception e) {
            System.out.println(e.getMessage());
            NotificationUtil.showToastError("Lỗi chưa xác định");
        }
        confirmed = true;

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

    public void handleChangeTopic(ActionEvent actionEvent) {
        if (!topicCombobox.getValue().toString().isEmpty()) {
            List<String> courses = courseService.findAllByTopicNameAndIsDelete(topicCombobox.getValue().toString());
            if(!courses.isEmpty()) {
                ObservableList<String> observableList = FXCollections.observableArrayList(courses);
                courseCombobox.setItems(observableList);
                learnersTable.setDisable(true);
            }else{
                courseCombobox.setItems(null);
                learnersTable.setDisable(false);
                learnersTable.setEditable(true);
            }
        } else {
            learnersTable.setDisable(true);
            courseCombobox.setItems(null);
        }
    }

    public void handleChangeCourse(ActionEvent actionEvent) {
        List<LearnersDTO> learners = learnersService.findAllByStatusDelete();
        List<Students> students = studentsService.findAll();

        List<String> learnersId = students.stream()
                .map(Students::getLearnersId)
                .toList();

        List<LearnersDTO> result = learners.stream()
                .filter(learnersDTO -> !learnersId.contains(learnersDTO.getId()))
                .toList();

        result.forEach(learner -> {
            if (selectedLearners == null) {
                learner.setSelected(false);
            } else {
                learner.setSelected(selectedLearners.contains(learner.getId()));
            }

            learner.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        selectedLearners.add(learner.getId());
                    } else {
                        selectedLearners.remove(learner.getId());
                    }
                }
            });
        });

        ObservableList<LearnersDTO> observableLearners = FXCollections.observableArrayList(result);
        learnersTable.getItems().clear();
        learnersTable.setDisable(false);
        learnersTable.setItems(observableLearners);
        learnersTable.setEditable(true);
        learnersTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }
}

