package org.example.sysedu.controller.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.sysedu.controller.dialog.topics.AddTopicsDialogController;
import org.example.sysedu.controller.dialog.topics.UpdateTopicsDialogController;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.utils.CurrencyFormat;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManageTopicsController {
    TopicsService topicsService = new TopicsService();
    public TableView topicsTable;
    public ComboBox statusTopicsBox;
    public TextField searchTopics;

    private final Timeline searchTimeline = new Timeline();
    public Label countTopics;
    public Label countTopicsByYear;
    public BarChart<String, Number> barChart;

    @FXML
    public void initialize() {
        TableColumn<Learners, String> id = new TableColumn<>("Mã chuyên đề");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Learners, String> topicName = new TableColumn<>("Tên chuyên đề");
        topicName.setCellValueFactory(new PropertyValueFactory<>("topicName"));

        TableColumn<Learners, BigDecimal> tuitionFee = new TableColumn<>("Học phí");
        tuitionFee.setCellValueFactory(new PropertyValueFactory<>("tuitionFee"));
        tuitionFee.setCellFactory(column -> new TableCell<Learners, BigDecimal>() {
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

        TableColumn<Learners, Integer> time = new TableColumn<>("Thời gian");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        time.setCellFactory(column -> new TableCell<Learners, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item + " Giờ");
                }
            }
        });


        TableColumn<Learners, Boolean> isDelete = new TableColumn<>("Trạng thái");
        isDelete.setCellValueFactory(new PropertyValueFactory<>("isDelete"));
        isDelete.setCellFactory(column -> new TableCell<Learners, Boolean>() {
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

        TableColumn<Learners, Timestamp> dateCreated = new TableColumn<>("Ngày tạo");
        dateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        dateCreated.setCellFactory(column -> new TableCell<Learners, Timestamp>() {
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

        topicsTable.getColumns().addAll(id, topicName, time, tuitionFee, isDelete, dateCreated);

        List<Topics> learners = topicsService.findAll();

        topicsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        topicsTable.getItems().addAll(learners);

        // Tạo dữ liệu cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");
        List<Integer> countLearnersOnYear = topicsService.statisticsOnThisYearCreatedTopics();

        for (int i = 1; i <= 12; i++) {
            series.getData().add(new XYChart.Data<>("T" + String.valueOf(i), countLearnersOnYear.get(i - 1)));
        }

        barChart.getData().addAll(series);

        countTopics.setText(String.valueOf(topicsService.findCountTopics()));
        countTopicsByYear.setText(String.valueOf(topicsService.findCountTopicsByYear()));

        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), this::performSearch));
        searchTimeline.setCycleCount(1);
    }

    @FXML
    public void handleSearch(KeyEvent keyEvent) {
        if (searchTimeline.getStatus() != Animation.Status.STOPPED) {
            searchTimeline.stop();
        }

        searchTimeline.playFromStart();
    }

    public void handleChangeStatus(ActionEvent actionEvent) {
        String searchText = searchTopics.getText();
        findByFilter(searchText, (String) statusTopicsBox.getValue());
    }

    /**
     * Phương thức tìm kiếm theo hiệu năng
     * @param actionEvent sự kiện nhập dữ liệu
     */
    private void performSearch(ActionEvent actionEvent) {
        String searchText = searchTopics.getText();
        findByFilter(searchText, (String) statusTopicsBox.getValue());
    }

    /**
     * Phương thức tìm kiếm người học theo filter
     * @param searchText từ khóa tìm kiếm
     * @param status trạng thái người học
     */
    private void findByFilter(String searchText, String status) {
        Boolean isStatus = null;
        List<Topics> topics;

        if(status != null) {
            if (status.equals("Đang hoạt động")) {
                isStatus = false;
            } else if (status.equals("Đã xóa")) {
                isStatus = true;
            }
        }

        System.out.println(isStatus);
        topics = topicsService.findAllByFilter(searchText, isStatus);

        topicsTable.getItems().clear();
        topicsTable.getItems().addAll(topics);
    }

    /**
     * Phương thức mở dialog cập nhật thông tin người học
     * @param event sự kiện click
     */
    @FXML
    private void handleOpenDialog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/topics/AddTopicsDialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Tạo dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Thêm chuyên đề");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(((Button) event.getSource()).getScene().getWindow());
            // Lấy ra controller và gán dialogStage
            AddTopicsDialogController controller = loader.getController();
            controller.setManageTopicsController(this);
            controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleTableRowClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Topics selectedTopics = (Topics) topicsTable.getSelectionModel().getSelectedItem();
            if (selectedTopics != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/topics/UpdateTopicsDialog.fxml"));
                    DialogPane dialogPane = loader.load();

                    // Tạo và thiết lập dialog
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(dialogPane);
                    dialog.setTitle("Cập nhật thông tin chuyên đề");
                    dialog.initModality(Modality.WINDOW_MODAL);
                    dialog.initOwner(topicsTable.getScene().getWindow());

                    // Lấy controller và khởi tạo dữ liệu
                    UpdateTopicsDialogController controller = loader.getController();
                    controller.setManageTopicsController(this);
                    controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());
                    controller.initData(selectedTopics);

                    dialog.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Phương thức cập nhật bảng
     */
    public void updateTable() {
        List<Topics> topics = topicsService.findAll();
        topicsTable.getItems().clear();
        Platform.runLater(() -> topicsTable.getItems().addAll(topics));
        Platform.runLater(() -> countTopics.setText(String.valueOf(topicsService.findCountTopics())));
        Platform.runLater(() -> countTopicsByYear.setText(String.valueOf(topicsService.findCountTopicsByYear())));
    }
}
