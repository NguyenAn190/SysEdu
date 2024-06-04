package org.example.sysedu.controller.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import org.example.sysedu.repository.LearnersRepository;
import org.example.sysedu.controller.dialog.learners.AddLearnersDialogController;
import org.example.sysedu.controller.dialog.learners.UpdateLearnersDialogController;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.service.LearnersService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManageLearnersController {
    LearnersService learnersService = new LearnersService();
    public ComboBox statusLearnersBox;
    private Timeline searchTimeline = new Timeline();
    @FXML
    public TableView learnersTable;
    public Label countLearners;
    public Label countLearnersByYear;
    public TextField searchLearners;
    public Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    /**
     * Phương thức khởi tạo
     */
    @FXML
    public void initialize() {
        TableColumn<Learners, String> id = new TableColumn<>("Mã NH");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Learners, String> fullName = new TableColumn<>("Họ và tên");
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Learners, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Learners, String> phone = new TableColumn<>("Số điện thoại");
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Learners, Boolean> gender = new TableColumn<>("Giới tính");
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        gender.setCellFactory(column -> new TableCell<Learners, Boolean>() {
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

        learnersTable.getColumns().addAll(id, fullName, email, phone, gender, isDelete, dateCreated);

        List<Learners> learners = learnersService.findAll();

        learnersTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        learnersTable.getItems().addAll(learners);

        // Tạo dữ liệu cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");
        List<Integer> countLearnersOnYear = learnersService.statisticsOnThisYearLearners();

        for (int i = 1; i <= 12; i++) {
            series.getData().add(new XYChart.Data<>("T" + String.valueOf(i), countLearnersOnYear.get(i - 1)));
        }

        barChart.getData().addAll(series);

        countLearners.setText(String.valueOf(learnersService.findCountLearners()));
        countLearnersByYear.setText(String.valueOf(learnersService.findCountLearnersByYear()));

        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), this::performSearch));
        searchTimeline.setCycleCount(1);
    }

    /**
     * Phương thức mở dialog cập nhật thông tin người học
     *
     * @param event sự kiện click
     */
    @FXML
    private void handleOpenDialog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/learners/AddLearnersDialog.fxml"));
            DialogPane dialogPane = loader.load();
            // Tạo dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Thêm người học mới");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(((Button) event.getSource()).getScene().getWindow());
            // Lấy ra controller và gán dialogStage
            AddLearnersDialogController controller = loader.getController();
            controller.setManageLearnersController(this);
            controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleTableRowClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Learners selectedLearner = (Learners) learnersTable.getSelectionModel().getSelectedItem();

            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (selectedLearner != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/learners/UpdateLearnersDialog.fxml"));
                        DialogPane dialogPane = loader.load();
                        // Tạo dialog
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(dialogPane);
                        dialog.setTitle("Cập nhật thông tin người học");
                        dialog.initModality(Modality.WINDOW_MODAL);
                        dialog.initOwner(learnersTable.getScene().getWindow());

                        // Lấy ra controller và gọi phương thức initData
                        UpdateLearnersDialogController controller = loader.getController();
                        controller.setManageLearnersController(this);
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

    @FXML
    public void handleSearch(KeyEvent keyEvent) {
        if (searchTimeline.getStatus() != Animation.Status.STOPPED) {
            searchTimeline.stop();
        }

        searchTimeline.playFromStart();
    }

    private void performSearch(ActionEvent event) {
        String searchText = searchLearners.getText();
        findByFilter(searchText, (String) statusLearnersBox.getValue());

    }

    public void handleChangeStatus(ActionEvent actionEvent) {
        String searchText = searchLearners.getText();

        findByFilter(searchText, (String) statusLearnersBox.getValue());
    }

    public void findByFilter(String search, String status) {
        Boolean isStatus = null;
        List<Learners> learners;

        if (status.equals("Đang hoạt động")) {
            isStatus = false;
        } else if (status.equals("Đã xóa")) {
            isStatus = true;
        }

        learners = learnersService.findAllByFilters(search, isStatus);

        learnersTable.getItems().clear();
        learnersTable.getItems().addAll(learners);
    }

    public void updateTable() {
        List<Learners> learners = learnersService.findAll();
        learnersTable.getItems().clear();
        Platform.runLater(() -> learnersTable.getItems().addAll(learners));
        Platform.runLater(() -> countLearners.setText(String.valueOf(learnersService.findCountLearners())));
        Platform.runLater(() -> countLearnersByYear.setText(String.valueOf(learnersService.findCountLearnersByYear())));
    }
}
