package org.example.sysedu.controller.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.example.sysedu.dto.students.ScoreStatisticsDTO;
import org.example.sysedu.dto.students.StudentRankDTO;
import org.example.sysedu.dto.students.StudentScoreDTO;
import org.example.sysedu.dto.topics.HighestTopicScoreDTO;
import org.example.sysedu.dto.topics.TopicScoreDTO;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.StudentsService;
import org.example.sysedu.service.TopicsService;

import java.util.List;

public class ScoreStatisticsController {
    public TableView scoreTable;
    StudentsService studentsService = new StudentsService();
    TopicsService topicsService = new TopicsService();
    CourseService courseService = new CourseService();
    public PieChart pieChart;
    public BarChart barChart;

    @FXML
    public void initialize() {
        // Thiết lập các cột cho bảng điểm
        TableColumn<TopicScoreDTO, String> id = new TableColumn<>("Mã chuyên đề");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<TopicScoreDTO, String> topicName = new TableColumn<>("Tên chuyên đề");
        topicName.setCellValueFactory(new PropertyValueFactory<>("topicName"));

        TableColumn<TopicScoreDTO, String> countStudent = new TableColumn<>("Số lượng học viên");
        countStudent.setCellValueFactory(new PropertyValueFactory<>("countStudent"));

        TableColumn<TopicScoreDTO, String> averageScore = new TableColumn<>("Điểm trung bình");
        averageScore.setCellValueFactory(new PropertyValueFactory<>("averageScore"));

        TableColumn<TopicScoreDTO, String> highestScore = new TableColumn<>("Điểm cao nhất");
        highestScore.setCellValueFactory(new PropertyValueFactory<>("highestScore"));

        TableColumn<TopicScoreDTO, String> lowestScore = new TableColumn<>("Điểm thấp nhất");
        lowestScore.setCellValueFactory(new PropertyValueFactory<>("lowestScore"));

        scoreTable.getColumns().addAll(id, topicName, countStudent, averageScore, highestScore, lowestScore);

        List<TopicScoreDTO> studentScoreDTOList = topicsService.getScoreTopics("");
        ObservableList<TopicScoreDTO> observableList = FXCollections.observableArrayList(studentScoreDTOList);

        scoreTable.setItems(observableList);
        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<HighestTopicScoreDTO> highestTopicScoreDTOS = topicsService.getTopScoreTopic("");

        // Thêm dữ liệu cho biểu đồ
        highestTopicScoreDTOS.stream().forEach(highestTopicScoreDTO -> {
            PieChart.Data slice = new PieChart.Data(highestTopicScoreDTO.getId(), highestTopicScoreDTO.getCountStudent());
            pieChart.getData().add(slice);
        });

        XYChart.Series<String, Float> series = new XYChart.Series<>();
        series.setName("Số lượng học viên");
        highestTopicScoreDTOS.stream().forEach(highestTopicScoreDTO -> {
            series.getData().add(new XYChart.Data<>("" + highestTopicScoreDTO.getId(), highestTopicScoreDTO.getAverageScore()));
        });

        barChart.getData().addAll(series);


    }

    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleChangeStatus(ActionEvent actionEvent) {
    }
}
