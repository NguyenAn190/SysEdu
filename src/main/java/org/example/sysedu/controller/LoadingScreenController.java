package org.example.sysedu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.sysedu.dto.usersDTO.UserSession;
import org.example.sysedu.entity.Users;
import org.example.sysedu.repository.UsersRepository;
import org.example.sysedu.service.UsersService;
import org.example.sysedu.ui.UIManager;

import javax.crypto.IllegalBlockSizeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class LoadingScreenController {
    @Getter
    private UsersRepository usersRepository;
    UsersService usersService = new UsersService();
    @Setter
    private Stage stage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        usersRepository = new UsersRepository();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<Users> entities = loadData();
                return null;
            }

            private List<Users> loadData() throws InterruptedException {
                List<Users> entities = usersRepository.findAll();
                int totalData = entities.size();
                for (int i = 0; i < totalData; i++) {
                    Thread.sleep(5000);

                    final int progress = (i + 1) * 100 / totalData;
                    Platform.runLater(() -> {
                        progressBar.setProgress(progress / 100.0);
                    });
                }
                return entities;
            }
        };

        task.setOnSucceeded(e -> {
            UserSession userSession = null;
            try {
                userSession = UserSession.getInstance();
                userSession.loadSessionFromFile();

                LocalDateTime expirationTime = userSession.getExpirationTime();
                if (expirationTime != null && LocalDateTime.now().isBefore(expirationTime)) {

                    Optional<Users> users = usersService.findByUsername(userSession.getUsername());
                    if (users.isPresent()) {
                        UIManager.openHomeWindow(stage);
                        stage.close();
                    } else {
                        UIManager.openLoginWindow(stage);
                        stage.close();
                    }
                }else {
                    UIManager.openLoginWindow(stage);
                    stage.close();
                }
            } catch (Exception ex) {
                ex.getMessage();
                UserSession.getInstance().clearSession();
                UIManager.openLoginWindow(stage);
                stage.close();
            }
        });

        new Thread(task).start();
    }
}
