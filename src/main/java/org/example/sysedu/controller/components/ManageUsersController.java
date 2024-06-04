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
import org.example.sysedu.controller.dialog.users.AddUsersDialogController;
import org.example.sysedu.controller.dialog.users.UpdateUsersDialogController;
import org.example.sysedu.entity.Courses;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.entity.Users;
import org.example.sysedu.service.CourseService;
import org.example.sysedu.service.TopicsService;
import org.example.sysedu.service.UsersService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManageUsersController {
    public TableView usersTable;
    public Label countUsersByYear;
    public Label countUsers;
    public TextField searchUsers;
    UsersService usersService = new UsersService();
    public Timeline searchTimeline = new Timeline();
    public ComboBox statusUsersBox;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    public void initialize() {
        TableColumn<Users, String> id = new TableColumn<>("Mã TK");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Users, String> fullName = new TableColumn<>("Họ và tên");
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Users, String> username = new TableColumn<>("Tên tài khoản");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Users, String> role = new TableColumn<>("Loại tài khoản");
        role.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Users, Boolean> isDelete = new TableColumn<>("Trạng thái");
        isDelete.setCellValueFactory(new PropertyValueFactory<>("isDelete"));
        isDelete.setCellFactory(column -> new TableCell<Users, Boolean>() {
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

        TableColumn<Users, Timestamp> dateCreated = new TableColumn<>("Ngày tạo");
        dateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        dateCreated.setCellFactory(column -> new TableCell<Users, Timestamp>() {
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
        usersTable.getColumns().addAll(id, fullName, username, role, isDelete, dateCreated);

        // Fetch and add data to the table
        List<Users> users = usersService.findAll();
        usersTable.getItems().addAll(users);

        // Ensure table resize policy is set
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Tạo dữ liệu cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");
        List<Integer> countUsersOnYear = usersService.statisticsOnThisYearUsers();

        for (int i = 1; i <= 12; i++) {
            series.getData().add(new XYChart.Data<>("T" + String.valueOf(i), countUsersOnYear.get(i - 1)));
        }

        barChart.getData().addAll(series);

        countUsers.setText(String.valueOf(usersService.findCountUsers()));
        countUsersByYear.setText(String.valueOf(usersService.findCountUserByYearCreate()));

        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), this::performSearch));
        searchTimeline.setCycleCount(1);

    }

    /**
     * Phương thức thực hiện tìm kiếm
     *
     * @param event
     */
    private void performSearch(ActionEvent event) {
        String searchText = searchUsers.getText();
        String status = statusUsersBox.getValue().toString();
        findByFilter(searchText, status);

    }

    private void findByFilter(String searchText, String status) {

        List<Users> users = usersService.findAllByFilter(searchText, status);

        usersTable.getItems().clear();
        usersTable.getItems().addAll(users);
    }


    @FXML
    private void handleOpenDialog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/users/AddUsersDialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Tạo dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Thêm khóa học mới");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(((Button) event.getSource()).getScene().getWindow());
            // Lấy ra controller và gán dialogStage
            AddUsersDialogController controller = loader.getController();
            controller.setManageUsersController(this);
            controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTable() {
        List<Users> users = usersService.findAll();
        usersTable.getItems().clear();
        Platform.runLater(() -> usersTable.getItems().addAll(users));
        Platform.runLater(() -> countUsers.setText(String.valueOf(usersService.findCountUsers())));
        Platform.runLater(() -> countUsersByYear.setText(String.valueOf(usersService.findCountUserByYearCreate())));
    }

    public void handleTableRowClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Users selectedCourses = (Users) usersTable.getSelectionModel().getSelectedItem();

            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (selectedCourses != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/dialog/users/UpdateUsersDialog.fxml"));
                        DialogPane dialogPane = loader.load();
                        // Tạo dialog
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(dialogPane);
                        dialog.setTitle("Cập nhật thông tin người học");
                        dialog.initModality(Modality.WINDOW_MODAL);
                        dialog.initOwner(usersTable.getScene().getWindow());

                        // Lấy ra controller và gọi phương thức initData
                        UpdateUsersDialogController controller = loader.getController();
                        controller.setManageUsersController(this);
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
        String searchText = searchUsers.getText();
        String statusUser = statusUsersBox.getValue().toString();

        findByFilter(searchText, statusUser);
    }

}
