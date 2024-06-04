package org.example.sysedu.exception;

import javafx.application.Platform;
import org.example.sysedu.utils.NotificationUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GlobalExceptionHandler {
    public static void handleException(Throwable e) {
        Platform.runLater(() -> {
            NotificationUtil.showToastError(e.getMessage());

            // In chi tiết lỗi ra console
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();

            System.out.println(stackTrace);
        });

    }
}
