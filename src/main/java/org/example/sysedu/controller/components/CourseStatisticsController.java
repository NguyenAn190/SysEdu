package org.example.sysedu.controller.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.example.sysedu.dto.courses.CoursesDTO;
import org.example.sysedu.dto.students.ScoreStatisticsDTO;
import org.example.sysedu.dto.students.StudentRankDTO;
import org.example.sysedu.dto.students.StudentScoreDTO;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.StudentsService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CourseStatisticsController {
    public TableView courseTable;
    StudentsService studentsService = new StudentsService();
    CourseService courseService = new CourseService();
    public PieChart pieChart;
    public BarChart barChart;

    @FXML
    public void initialize() {
        // Thiết lập các cột cho bảng điểm
        TableColumn<StudentScoreDTO, String> id = new TableColumn<>("Mã học viên");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<StudentScoreDTO, String> courseName = new TableColumn<>("Tên khóa học");
        courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        TableColumn<StudentScoreDTO, String> yearCreate = new TableColumn<>("Năm");
        yearCreate.setCellValueFactory(new PropertyValueFactory<>("yearCreated"));

        TableColumn<StudentScoreDTO, String> countStudent = new TableColumn<>("Số lượng HV");
        countStudent.setCellValueFactory(new PropertyValueFactory<>("countStudent"));

        TableColumn<StudentScoreDTO, Timestamp> minDate = new TableColumn<>("DK sớm nhất ");
        minDate.setCellValueFactory(new PropertyValueFactory<>("minDate"));
        minDate.setCellFactory(column -> new TableCell<StudentScoreDTO, Timestamp>() {
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

        TableColumn<StudentScoreDTO, Timestamp> maxDate = new TableColumn<>("DK muộn nhất");
        maxDate.setCellValueFactory(new PropertyValueFactory<>("maxDate"));
        maxDate.setCellFactory(column -> new TableCell<StudentScoreDTO, Timestamp>() {
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

        courseTable.getColumns().addAll(id, courseName, yearCreate, countStudent, minDate, maxDate);

        List<CoursesDTO> coursesDTOS = courseService.getCourse(0);
        ObservableList<CoursesDTO> observableList = FXCollections.observableArrayList(coursesDTOS);

        courseTable.setItems(observableList);
        courseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Thêm dữ liệu cho biểu đồ
        List<Integer> countCourseThisYear = courseService.getCourseInYear(LocalDate.now().getYear());
        List<Integer> countCourseLastYear = courseService.getCourseInYear(LocalDate.now().getYear() - 1);

        int currentYearCount = (countCourseThisYear != null && !countCourseThisYear.isEmpty()) ? countCourseThisYear.get(0) : 0;
        int lastYearCount = (countCourseLastYear != null && !countCourseLastYear.isEmpty()) ? countCourseLastYear.get(0) : 0;

        PieChart.Data slice = new PieChart.Data(String.valueOf(LocalDate.now().getYear()), currentYearCount);
        PieChart.Data slice1 = new PieChart.Data(String.valueOf(LocalDate.now().getYear() - 1), lastYearCount);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice);

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Số lượng học viên");
        coursesDTOS.stream()
                .limit(5)
                .forEach(coursesDTO -> {
                    series.getData().add(new XYChart.Data<>("" + coursesDTO.getId(), coursesDTO.getCountStudent()));
                });


        barChart.getData().addAll(series);

    }

    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleChangeStatus(ActionEvent actionEvent) {
    }
}
