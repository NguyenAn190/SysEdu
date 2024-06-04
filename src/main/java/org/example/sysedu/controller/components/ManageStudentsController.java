package org.example.sysedu.controller.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.sysedu.controller.dialog.learners.UpdateLearnersDialogController;
import org.example.sysedu.controller.dialog.students.AddStudentsDialogController;
import org.example.sysedu.controller.dialog.students.UpdateStudentsDialogController;
import org.example.sysedu.entity.Courses;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.entity.Students;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.repository.LearnersRepository;
import org.example.sysedu.repository.StudentsRepository;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.LearnersService;
import org.example.sysedu.service.StudentsService;
import org.example.sysedu.service.TopicsService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManageStudentsController {
    StudentsService studentsService = new StudentsService();
    TopicsService topicsService = new TopicsService();
    LearnersService learnersService = new LearnersService();
    CourseService courseService = new CourseService();
    public TextField searchStudent;
    public ComboBox courseComboBox;
    public ComboBox topicComboBox;
    public BarChart barChart;
    public TableView studentTable;
    private Timeline searchTimeline = new Timeline();
    public Label countStudentByYear;
    public Label countStudent;

    @FXML
    public void initialize() {
        StudentsRepository studentsRepository = new StudentsRepository();

        TableColumn<Students, String> id = new TableColumn<>("Mã học viên");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Students, String> fullName = new TableColumn<>("Họ và tên");
        fullName.setCellValueFactory(cellData -> {
            Students student = cellData.getValue();
            if (student.getLearner() != null) {
                return new SimpleStringProperty(student.getLearner().getFullName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<Students, String> topicName = new TableColumn<>("Chuyên đề");
        topicName.setCellValueFactory(cellData -> {
            Students students = cellData.getValue();
            if(students.getCourses() != null) {
                return new SimpleStringProperty(students.getCourses().getTopics().getTopicName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<Students, String> courseName = new TableColumn<>("Khóa học");
        courseName.setCellValueFactory(cellData -> {
            Students students = cellData.getValue();
            if(students.getCourses() != null) {
                return new SimpleStringProperty(students.getCourses().getCourseName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<Students, String> learnersId = new TableColumn<>("Mã người học");
        learnersId.setCellValueFactory(new PropertyValueFactory<>("learnersId"));

        TableColumn<Students, Boolean> score = new TableColumn<>("Điểm");
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<Students, Timestamp> dateCreated = new TableColumn<>("Ngày thêm");
        dateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        dateCreated.setCellFactory(column -> new TableCell<Students, Timestamp>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDateTime().format(formatter));
                }
            }
        });

        studentTable.getColumns().addAll(id, fullName, topicName, courseName, learnersId, score, dateCreated);

        List<Students> students = studentsRepository.findAll();

        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        studentTable.getItems().addAll(students);

        countStudent.setText(String.valueOf(studentsService.findCountStudent()));
        countStudentByYear.setText(String.valueOf(studentsService.findCountStudentByYear()));

        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), this::performSearch));
        searchTimeline.setCycleCount(1);

        // Tạo dữ liệu cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");
        List<Integer> countLearnersOnYear = studentsService.statisticsOnThisYearStudent();

        for (int i = 1; i <= 12; i++) {
            series.getData().add(new XYChart.Data<>("T" + String.valueOf(i), countLearnersOnYear.get(i - 1)));
        }

        barChart.getData().addAll(series);

        List<Topics> courses = topicsService.findAllByFilter("", false);
        ObservableList<String> courseNameComboBox = FXCollections.observableArrayList(courses.stream().map(Topics::getTopicName).toList());
        topicComboBox.setItems(courseNameComboBox);
    }

    @FXML
    public void handleSearch(KeyEvent keyEvent) {
        if (searchTimeline.getStatus() != Animation.Status.STOPPED) {
            searchTimeline.stop();
        }

        searchTimeline.playFromStart();
    }

    private void performSearch(ActionEvent event) {
        String searchText = searchStudent.getText();
        String topicName =  topicComboBox.getValue() == null ? "" : topicComboBox.getValue().toString();
        String courseName = courseComboBox.getValue() == null ? "" : courseComboBox.getValue().toString();
        findByFilter(searchText, topicName, courseName);
    }

    private void findByFilter(String searchText, String topicName, String courseName) {
        List<Students> students = studentsService.findByFilter(
                searchText,
                topicName == null ? "" : topicName,
                courseName == null ? "" : courseName);

        studentTable.getItems().clear();
        studentTable.getItems().addAll(students);

    }


    /**
     * Phương thức mở dialog thêm người học mới
     * @param actionEvent sự kiện nhấn nút mở dialog
     */
    public void handleOpenDialog(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/students/AddStudentsDialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Tạo dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Thêm người học");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
            // Lấy ra controller và gán dialogStage
            AddStudentsDialogController controller = loader.getController();
            controller.setManageStudentsController(this);
            controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTable() {
        List<Students> students = studentsService.findAll();
        studentTable.getItems().clear();
        Platform.runLater(() -> studentTable.getItems().addAll(students));
        Platform.runLater(() -> countStudent.setText(String.valueOf(studentsService.findCountStudent())));
        Platform.runLater(() -> countStudentByYear.setText(String.valueOf(studentsService.findCountStudentByYear())));
    }


    public void handleTableRowClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Students selectedLearner = (Students) studentTable.getSelectionModel().getSelectedItem();

            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (selectedLearner != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/students/UpdateStudentsDialog.fxml"));
                        DialogPane dialogPane = loader.load();
                        // Tạo dialog
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(dialogPane);
                        dialog.setTitle("Cập nhật thông tin học viên");
                        dialog.initModality(Modality.WINDOW_MODAL);
                        dialog.initOwner(studentTable.getScene().getWindow());

                        // Lấy ra controller và gọi phương thức initData
                        UpdateStudentsDialogController controller = loader.getController();
                        controller.setManageStudentsController(this);
                        controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());
                        controller.initData(selectedLearner);

                        dialog.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void handleSearchTopic(ActionEvent actionEvent) {
        String topicId = topicsService.findByName(topicComboBox.getValue().toString());
        List<String> coursesName = courseService.findCourseNameByTopicId(topicId);
        ObservableList<String> observableList = FXCollections.observableArrayList(coursesName);
        courseComboBox.setItems(observableList);
        performSearch(actionEvent);
    }

    public void handleSearchCourse(ActionEvent actionEvent) {
        performSearch(actionEvent);
    }
}
