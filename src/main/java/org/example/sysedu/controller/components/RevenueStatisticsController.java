package org.example.sysedu.controller.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.example.sysedu.dto.students.ScoreStatisticsDTO;
import org.example.sysedu.dto.students.StudentRankDTO;
import org.example.sysedu.dto.students.StudentScoreDTO;
import org.example.sysedu.dto.topics.RevenueStatisticsDTO;
import org.example.sysedu.dto.topics.TopTopicHotDTO;
import org.example.sysedu.dto.topics.TotalTuitionFeeDTO;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.StudentsService;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.utils.CurrencyFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RevenueStatisticsController {
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    TopicsService topicsService = new TopicsService();
    public TableView revenueTable;
    public PieChart pieChart;
    public BarChart barChart;
    private final Timeline searchTimeline = new Timeline();
    @FXML
    public void initialize() {
        // Thiết lập các cột cho bảng điểm
        TableColumn<RevenueStatisticsDTO, String> id = new TableColumn<>("Mã chuyên đề");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<RevenueStatisticsDTO, String> topicName = new TableColumn<>("Tên khóa học");
        topicName.setCellValueFactory(new PropertyValueFactory<>("topicName"));

        TableColumn<RevenueStatisticsDTO, String> countStudent = new TableColumn<>("Số lượng HV");
        countStudent.setCellValueFactory(new PropertyValueFactory<>("countStudent"));

        TableColumn<RevenueStatisticsDTO, String> courseCount = new TableColumn<>("SL Khóa học");
        courseCount.setCellValueFactory(new PropertyValueFactory<>("courseCount"));

        TableColumn<RevenueStatisticsDTO, BigDecimal> sumTuitionFee = new TableColumn<>("Tổng doanh thu");
        sumTuitionFee.setCellValueFactory(new PropertyValueFactory<>("sumTuitionFee"));
        sumTuitionFee.setCellFactory(column -> new TableCell<RevenueStatisticsDTO, BigDecimal>() {
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

        TableColumn<RevenueStatisticsDTO, BigDecimal> minTuitionFee = new TableColumn<>("HP thấp nhất");
        minTuitionFee.setCellValueFactory(new PropertyValueFactory<>("minTuitionFee"));
        minTuitionFee.setCellFactory(column -> new TableCell<RevenueStatisticsDTO, BigDecimal>() {
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

        TableColumn<RevenueStatisticsDTO, BigDecimal> averageTuitionFee = new TableColumn<>("HP trung bình");
        averageTuitionFee.setCellValueFactory(new PropertyValueFactory<>("averageTuitionFee"));
        averageTuitionFee.setCellFactory(column -> new TableCell<RevenueStatisticsDTO, BigDecimal>() {
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

        TableColumn<RevenueStatisticsDTO, BigDecimal> maxTuitionFee = new TableColumn<>("HP cao nhất");
        maxTuitionFee.setCellValueFactory(new PropertyValueFactory<>("maxTuitionFee"));
        maxTuitionFee.setCellFactory(column -> new TableCell<RevenueStatisticsDTO, BigDecimal>() {
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
        revenueTable.getColumns().addAll(id, topicName, countStudent, courseCount, sumTuitionFee, minTuitionFee, averageTuitionFee, maxTuitionFee);

        List<RevenueStatisticsDTO> revenueStatisticsDTOS = topicsService.getRevenueStatistic(null, null);
        ObservableList<RevenueStatisticsDTO> observableList = FXCollections.observableArrayList(revenueStatisticsDTOS);

        revenueTable.setItems(observableList);
        revenueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<TotalTuitionFeeDTO> totalTuitionFeeThisYear = topicsService.getTotalTuitionFeeInYear(LocalDate.now().getYear());
        List<TotalTuitionFeeDTO> totalTuitionFeeLashYear = topicsService.getTotalTuitionFeeInYear(LocalDate.now().getYear() - 1);
        // Thêm dữ liệu cho biểu đồ
        PieChart.Data slice = new PieChart.Data(String.valueOf(LocalDate.now().getYear()), totalTuitionFeeThisYear.size() > 0 ? totalTuitionFeeThisYear.get(0).getTuitionFee().intValue() : 0);
        PieChart.Data slice1 = new PieChart.Data(String.valueOf(LocalDate.now().getYear() - 1), totalTuitionFeeLashYear.size() > 0 ? totalTuitionFeeThisYear.get(0).getTuitionFee().intValue() : 0);
        pieChart.getData().add(slice);
        pieChart.getData().add(slice1);

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName(String.valueOf(LocalDate.now().getYear()));

        List<TopTopicHotDTO> topTopicHotDTOS = topicsService.getTop5TopicHot();
        topTopicHotDTOS.stream().forEach(topTopicHotDTO -> {
            series.getData().add(new XYChart.Data<>("" + topTopicHotDTO.getId(), topTopicHotDTO.getTotalTuitionFee().intValue()));
        });

        barChart.getData().addAll(series);


        // Lấy TextField bên trong DatePicker
        TextField editor = startDatePicker.getEditor();

        // Định dạng ngày tháng
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Chuyển đổi giá trị LocalDate sang chuỗi
        startDatePicker.setConverter(new StringConverter<LocalDate>() {
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
                startDatePicker.setValue(date);
            } catch (DateTimeParseException e) {
                startDatePicker.setValue(null);
            }
        });

        TextField editor2 = endDatePicker.getEditor();
        endDatePicker.setConverter(new StringConverter<LocalDate>() {
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

        editor2.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                LocalDate date = LocalDate.parse(newValue, formatter);
                endDatePicker.setValue(date);
            } catch (DateTimeParseException e) {
                endDatePicker.setValue(null);
            }
        });

        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), this::performSearch));
        searchTimeline.setCycleCount(1);
    }
    /**
     * Phương thức tìm kiếm theo hiệu năng
     * @param actionEvent sự kiện nhập dữ liệu
     */
    private void performSearch(ActionEvent actionEvent) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        findByFilter(startDate, endDate);
    }

    private void findByFilter(LocalDate startDate, LocalDate endDate) {

        List<RevenueStatisticsDTO> revenueStatisticsDTOS = topicsService.getRevenueStatistic(
                startDate != null ? startDate : endDate,
                startDate != null && endDate != null ? endDate : null
        );

        revenueTable.getItems().clear();
        revenueTable.getItems().addAll(revenueStatisticsDTOS);
    }


    public void handleChangStartDate(ActionEvent actionEvent) {
        performSearch(actionEvent);
    }

    public void handleChangEndDate(ActionEvent actionEvent) {
        performSearch(actionEvent);
    }
}
