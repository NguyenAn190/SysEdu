package org.example.sysedu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.sysedu.entity.Users;
import org.example.sysedu.repository.UsersRepository;

import java.io.IOException;
import java.util.List;

public class AdminController {
    private Stage loginStage;
    @FXML
    private Label welcomeLabel;
    private UsersRepository usersRepository;

    public void initialize() {
        usersRepository = new UsersRepository();
        handleCloseLoginWindow();
    }

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @FXML
    private BorderPane contentPane;

    /**
     * Xử lí đóng modal login
     */
    @FXML
    private void handleCloseLoginWindow() {
        if (loginStage != null) {
            loginStage.close();
        }
    }

    @FXML
    private void handleManageLearners() {

        List<Users> entities = usersRepository.findAll();
        System.out.println(entities.toString());

        loadView("/org/example/sysedu/fxml/components/ManageLearnersView.fxml");
    }

    @FXML
    private void handleManageStudents() {
        System.out.println("Quản lí học viên");
        loadView("/org/example/sysedu/fxml/components/ManageStudentView.fxml");
    }

    @FXML
    private void handleManageTopics() {
        loadView("/org/example/sysedu/fxml/components/ManageTopicsView.fxml");
    }

    @FXML
    private void handleManageCourses() {
        loadView("/org/example/sysedu/fxml/components/ManageCourseListView.fxml");
    }

    private void loadView(String fxmlFileName) {
        try {
            Pane view = FXMLLoader.load(getClass().getResource(fxmlFileName));
            contentPane.setCenter(view); // Đặt view vào phần Center của BorderPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
