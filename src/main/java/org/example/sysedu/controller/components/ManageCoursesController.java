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
import org.example.sysedu.controller.dialog.courses.AddCoursesDialogController;
import org.example.sysedu.controller.dialog.courses.UpdateCoursesDialogController;
import org.example.sysedu.controller.dialog.learners.UpdateLearnersDialogController;
import org.example.sysedu.entity.Courses;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.entity.Students;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.utils.CurrencyFormat;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ManageCoursesController {
    public ComboBox topicNameBox;
    TopicsService topicsService = new TopicsService();
    public TableView courseTable;
    public Timeline searchTimeline = new Timeline();
    public TextField searchCourse;
    public ComboBox statusCourseBox;
    public Label countCourseByYear;
    public Label countCourse;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    public void initialize() {
        CourseService courseService = new CourseService();

        TableColumn<Courses, String> id = new TableColumn<>("Mã KH");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Courses, String> topicName = new TableColumn<>("Tên chuyên đề");
        topicName.setCellValueFactory(cellData -> {
            Courses courses = cellData.getValue();
            if (courses.getTopics() != null) {
                return new SimpleStringProperty(courses.getTopics().getTopicName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<Courses, BigDecimal> tuitionFee = new TableColumn<>("Học phí");
        tuitionFee.setCellValueFactory(new PropertyValueFactory<>("tuitionFee"));
        tuitionFee.setCellFactory(column -> new TableCell<Courses, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(CurrencyFormat.format(item) + " VNĐ");
                }
            }
        });

        TableColumn<Courses, String> time = new TableColumn<>("Thời gian");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Courses, String> declarationDate = new TableColumn<>("Ngày khai giảng");
        declarationDate.setCellValueFactory(new PropertyValueFactory<>("declarationDate"));

        TableColumn<Courses, Boolean> isDelete = new TableColumn<>("Trạng thái");
        isDelete.setCellValueFactory(new PropertyValueFactory<>("isDelete"));
        isDelete.setCellFactory(column -> new TableCell<Courses, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("danger-badge", "success-badge");
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Đã xóa" : "Đang hoạt động");

                    if (item) {
                        getStyleClass().add("danger-badge");
                    } else {
                        getStyleClass().add("success-badge");
                    }
                }
            }
        });

        TableColumn<Courses, Timestamp> dateCreated = new TableColumn<>("Ngày tạo");
        dateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        dateCreated.setCellFactory(column -> new TableCell<Courses, Timestamp>() {
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

        // Add columns to the table
        courseTable.getColumns().addAll(id, topicName, time, tuitionFee, declarationDate, isDelete, dateCreated);

        // Fetch and add data to the table
        List<Courses> courses = courseService.findAll();
        courseTable.getItems().addAll(courses);

        // Ensure table resize policy is set
        courseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Tạo dữ liệu cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");
        List<Integer> countLearnersOnYear = courseService.statisticsOnThisYearLearners();

        for (int i = 1; i < 12; i++) {
            series.getData().add(new XYChart.Data<>("T" + String.valueOf(i), countLearnersOnYear.get(i - 1)));
        }

        barChart.getData().addAll(series);

        countCourse.setText(String.valueOf(courseService.findCountCourse()));
        countCourseByYear.setText(String.valueOf(courseService.findAllCourseByYearCreate()));

        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), this::performSearch));
        searchTimeline.setCycleCount(1);


        List<String> listTopicName = topicsService.findAll().stream().map(Topics::getTopicName).toList();
        ObservableList<String> list = FXCollections.observableArrayList(listTopicName);
        topicNameBox.setItems(list);

    }

    /**
     * Phương thức thực hiện tìm kiếm
     *
     * @param event
     */
    private void performSearch(ActionEvent event) {
        String topicId = topicNameBox.getValue() == null ? null : topicsService.findByName(topicNameBox.getValue().toString());
        String searchText = searchCourse.getText();
        findByFilter(searchText, (String) statusCourseBox.getValue(), topicId);

    }

    private void findByFilter(String searchText, String status, String topicId) {
        CourseService courseService = new CourseService();
        List<Courses> learners = courseService.findAllByFilter(searchText, status, topicId);

        courseTable.getItems().clear();
        courseTable.getItems().addAll(learners);
    }


    @FXML
    private void handleOpenDialog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/courses/AddCoursesDialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Tạo dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Thêm khóa học mới");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(((Button) event.getSource()).getScene().getWindow());
            // Lấy ra controller và gán dialogStage
            AddCoursesDialogController controller = loader.getController();
            controller.setManageCoursesController(this);
            controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTable() {
        CourseService courseService = new CourseService();
        List<Courses> courses = courseService.findAll();
        courseTable.getItems().clear();
        Platform.runLater(() -> courseTable.getItems().addAll(courses));
        Platform.runLater(() -> countCourse.setText(String.valueOf(courseService.findCountCourse())));
        Platform.runLater(() -> countCourseByYear.setText(String.valueOf(courseService.findAllCourseByYearCreate())));
    }

    public void handleTableRowClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Courses selectedCourses = (Courses) courseTable.getSelectionModel().getSelectedItem();

            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (selectedCourses != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/courses/UpdateCoursesDialog.fxml"));
                        DialogPane dialogPane = loader.load();
                        // Tạo dialog
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(dialogPane);
                        dialog.setTitle("Cập nhật thông tin người học");
                        dialog.initModality(Modality.WINDOW_MODAL);
                        dialog.initOwner(courseTable.getScene().getWindow());

                        // Lấy ra controller và gọi phương thức initData
                        UpdateCoursesDialogController controller = loader.getController();
                        controller.setManageCoursesController(this);
                        controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());
                        controller.initData(selectedCourses);

                        dialog.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void handleSearch(KeyEvent keyEvent) {
        if (searchTimeline.getStatus() != Animation.Status.STOPPED) {
            searchTimeline.stop();
        }

        searchTimeline.playFromStart();
    }

    public void handleChangeStatus(ActionEvent actionEvent) {
        String topicId = null;
        if (topicNameBox.getValue() != null && !topicNameBox.getValue().toString().isEmpty()) {
            topicId = topicsService.findByName(topicNameBox.getValue().toString());
        }

        String searchText = searchCourse.getText();
        String statusCourse = statusCourseBox.getValue() != null ? statusCourseBox.getValue().toString() : null;

        findByFilter(searchText, statusCourse, topicId);
    }

}
