package org.example.sysedu.controller.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.example.sysedu.dto.students.ScoreStatisticsDTO;
import org.example.sysedu.dto.students.StudentRankDTO;
import org.example.sysedu.dto.students.StudentScoreDTO;
import org.example.sysedu.entity.Students;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.StudentsService;
import org.example.sysedu.service.TopicsService;

import java.util.List;

public class ScoreboardStatisticsController {
    public TableView scoreTable;
    public TextField searchParam;
    public ComboBox topicComboBox;
    public ComboBox courseComboBox;
    StudentsService studentsService = new StudentsService();
    CourseService courseService = new CourseService();
    TopicsService topicsService = new TopicsService();
    public PieChart pieChart;
    public BarChart barChart;
    private Timeline searchTimeline = new Timeline();
    @FXML
    public void initialize() {
        // Thiết lập các cột cho bảng điểm
        TableColumn<StudentScoreDTO, String> id = new TableColumn<>("Mã học viên");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<StudentScoreDTO, String> fullName = new TableColumn<>("Họ và tên");
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<StudentScoreDTO, String> score = new TableColumn<>("Điểm");
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<StudentScoreDTO, String> ranks = new TableColumn<>("Xếp loại");
        ranks.setCellValueFactory(new PropertyValueFactory<>("ranks"));

        scoreTable.getColumns().addAll(id, fullName, score, ranks);

        List<StudentScoreDTO> studentScoreDTOList = studentsService.getStudentScores("", "","");
        ObservableList<StudentScoreDTO> observableList = FXCollections.observableArrayList(studentScoreDTOList);

        scoreTable.setItems(observableList);
        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<StudentRankDTO> studentRankDTOS = studentsService.getStudentRank("");

        // Thêm dữ liệu cho biểu đồ
        studentRankDTOS.stream().forEach(rank -> {
            PieChart.Data slice = new PieChart.Data(rank.getRanks(), rank.getRankCount());
            pieChart.getData().add(slice);
        });

        List<ScoreStatisticsDTO> scoreStatisticsDTOS = studentsService.getCountScore("");
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Số lượng học viên");
        scoreStatisticsDTOS.stream()
                .filter(scoreStatisticsDTO -> scoreStatisticsDTO.getScore() != 0)
                .forEach(scoreStatisticsDTO -> {
            series.getData().add(new XYChart.Data<>("" + scoreStatisticsDTO.getScore(), scoreStatisticsDTO.getCount()));
        });

        barChart.getData().addAll(series);

        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), this::performSearch));
        searchTimeline.setCycleCount(1);

        List<Topics> courses = topicsService.findAllByFilter("", false);
        ObservableList<String> courseNameComboBox = FXCollections.observableArrayList(courses.stream().map(Topics::getTopicName).toList());
        topicComboBox.setItems(courseNameComboBox);
    }

    private void performSearch(ActionEvent event) {
        String searchText = searchParam.getText();
        String topicName = topicComboBox.getValue() == null ? "" : topicComboBox.getValue().toString();
        String courseName = courseComboBox.getValue() == null ? "" : courseComboBox.getValue().toString();
        findByFilter(searchText, topicName, courseName);
    }

    private void findByFilter(String searchText, String topicName, String courseName) {
        List<StudentScoreDTO> students = studentsService.getStudentScores(
                searchText,
                topicName == null ? "" : topicName,
                courseName == null ? "" : courseName);

        scoreTable.getItems().clear();
        scoreTable.getItems().addAll(students);


    }
    public void handleSearch(KeyEvent keyEvent) {
        if (searchTimeline.getStatus() != Animation.Status.STOPPED) {
            searchTimeline.stop();
        }

        searchTimeline.playFromStart();
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
