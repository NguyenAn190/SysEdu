package org.example.sysedu.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.example.sysedu.dto.usersDTO.UserSession;
import org.example.sysedu.entity.Users;
import org.example.sysedu.enums.Role;
import org.example.sysedu.service.UsersService;
import org.example.sysedu.ui.UIManager;
import org.example.sysedu.utils.NotificationUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeController {
    UsersService usersService = new UsersService();
    public Button manageStudentsButton;
    public Button manageLearnersButton;
    public Button manageTopicsButton;
    public Button manageCoursesButton;
    public StackPane rootPane;
    public Button manageUsersButton;
    public Button manageScoreboardButton;
    public Button manageCourseStatisticsButton;
    public Button manageScoreStatisticsButton;
    public Button manageRevenueButton;
    private Stage loginStage;

    @Setter
    private Stage stage;
    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @FXML
    private BorderPane contentPane;

    @FXML
    private void initialize() {
        try {
            UserSession userSession = UserSession.getInstance();
            userSession.loadSessionFromFile();

            if (userSession.getExpirationTime() == null || LocalDateTime.now().isAfter(userSession.getExpirationTime())) {
                UIManager.openHomeWindow(stage);

                // Đóng cửa sổ hiện tại
                Platform.runLater(() -> {
                    Stage currentStage = (Stage) manageCoursesButton.getScene().getWindow();
                    currentStage.close();
                });
            } else {
                Optional<Users> users = usersService.findByUsername(userSession.getUsername());
                if(users.isPresent()) {
                    if(users.get().getRole() == Role.ADMIN){
                        manageUsersButton.setVisible(true);
                        manageScoreboardButton.setVisible(true);
                        manageCourseStatisticsButton.setVisible(true);
                        manageScoreStatisticsButton.setVisible(true);
                        manageRevenueButton.setVisible(true);
                    } else {
                        manageUsersButton.setVisible(false);
                        manageScoreboardButton.setVisible(false);
                        manageCourseStatisticsButton.setVisible(false);
                        manageScoreStatisticsButton.setVisible(false);
                        manageRevenueButton.setVisible(false);
                    }
                }else {
                    UIManager.openHomeWindow(stage);

                    // Đóng cửa sổ hiện tại
                    Platform.runLater(() -> {
                        Stage currentStage = (Stage) manageCoursesButton.getScene().getWindow();
                        currentStage.close();
                    });
                }
            }
        }catch (Exception e) {

        }
    }

    /**
     * Xử lí đóng modal login
     */
    @FXML
    private void handleCloseLoginWindow() {
        if (loginStage != null) {
            loginStage.close();
        }
    }

    /**
     * Phương thức mở tab quản lí người học
     * @param event sự kiện nhấn nút mở tab
     */
    @FXML
    private void handleManageLearners(ActionEvent event) {
            activeButton(event);
            loadView("/org/example/sysedu/fxml/components/ManageLearnersView.fxml");
    }

    /**
     * Phương thức mở tab quản lí học viên
     * @param event sự kiện nhấn nút mở tab
     */
    @FXML
    private void handleManageStudents(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/ManageStudentView.fxml");
    }

    /**
     * Phương thức mở tab quản lí chuyên đề
     * @param event sự kiện nhấn nút mở tab chuyên đề
     */
    @FXML
    private void handleManageTopics(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/ManageTopicsView.fxml");
    }

    /**
     * Phương thức mở tab quản lí khóa học
     * @param event sự kiện nhấn nút mở tab khóa học
     */
    @FXML
    private void handleManageCourses(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/ManageCoursesView.fxml");
    }

    public void handleManageUsers(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/ManageUsersView.fxml");
    }

    public void handleManageScoreboard(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/ScoreboardStatisticsView.fxml");
    }

    public void handleCourseStatistics(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/CourseStatisticsView.fxml");
    }

    public void handleScoreStatidtics(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/ScoreStatisticsView.fxml");
    }

    public void handleRevenueStatistics(ActionEvent event) {
        activeButton(event);
        loadView("/org/example/sysedu/fxml/components/RevenueStatisticsView.fxml");
    }

    private void activeButton(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.getStyleClass().add("active-nav");
        List<Button> listButton = new ArrayList<>();
        listButton.add(manageLearnersButton);
        listButton.add(manageStudentsButton);
        listButton.add(manageTopicsButton);
        listButton.add(manageCoursesButton);
        listButton.add(manageUsersButton);
        listButton.add(manageScoreboardButton);
        listButton.add(manageCourseStatisticsButton);
        listButton.add(manageScoreStatisticsButton);
        listButton.add(manageRevenueButton);

        for (Button btn : listButton) {
            btn.getStyleClass().remove("active-nav");
        }

        button.getStyleClass().add("active-nav");
    }

    /**
     * Phương thức mở view
     * @param fxmlFileName tên file fxml
     */
    private void loadView(String fxmlFileName) {
        // Tạo một Popup mới
        Popup loadingPopup = new Popup();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sysedu/fxml/loading/Loading.fxml"));
            Parent loadingRoot = loader.load();

            loadingPopup.getContent().add(loadingRoot);


            loadingPopup.setHideOnEscape(false);
            loadingPopup.setAutoHide(false);
//            loadingPopup.setModal(true);

            // Hiển thị Popup
            loadingPopup.show(rootPane.getScene().getWindow());

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        // Load nội dung chính
                        Pane view = FXMLLoader.load(getClass().getResource(fxmlFileName));

                        // Hiển thị nội dung chính trên contentPane
                        Platform.runLater(() -> {
                            contentPane.getChildren().setAll(view);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        // Đóng Popup sau khi load hoàn tất
                        Platform.runLater(() -> {
                            loadingPopup.hide();
                        });
                    }
                    return null;
                }
            };

            // Bắt đầu task trong một luồng mới
            new Thread(task).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleLogout(ActionEvent actionEvent) {
        UserSession.getInstance().clearSession();

        Node source = (Node) actionEvent.getSource();
        Stage loginStage = (Stage) source.getScene().getWindow();
        UIManager.openLoginWindow(loginStage);

        loginStage.close();
    }
}
