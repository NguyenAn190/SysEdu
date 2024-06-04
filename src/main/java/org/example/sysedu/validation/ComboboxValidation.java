package org.example.sysedu.validation;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.sysedu.exception.GlobalValidation;

import java.util.List;

public class ComboboxValidation {
    /**
     * Bắt lỗi chưa chọn người học
     * @param learners dữ liệu cần kiểm tra
     * @param errorLabel lỗi nếu có
     */
    public static void learnersValidation(List<String> learners, Label errorLabel) {
        ObservableList<String> observableList = FXCollections.observableArrayList(learners);
        BooleanBinding isEmpty = GlobalValidation.isListEmpty(observableList);

        errorLabel.visibleProperty().bind(isEmpty);

        errorLabel.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng chọn học viên!")
                .otherwise("")
        );
    }

    /**
     * Bắt lỗi trường chuyên đề
     * @param topicComboBox dữ liệu cần kiểm tra
     * @param errorTopicComboBox lỗi nếu có
     */
    public static void topicsValidation(ComboBox topicComboBox, Label errorTopicComboBox) {
        BooleanBinding isEmpty = GlobalValidation.isComboBoxEmpty(topicComboBox);

        BooleanBinding anyInvalid = isEmpty;

        errorTopicComboBox.visibleProperty().bind(anyInvalid);
        errorTopicComboBox.textProperty().bind(
                Bindings.when(isEmpty)
                        .then("Vui lòng chọn chuyên đề!")
                        .otherwise("")

        );
    }

    /**
     * Bắt lỗi chọn khóa học
     * @param courseName dữ liệu cần kiểm tra
     * @param errorLabel lỗi nếu có
     */
    public static void courseValidation(ComboBox courseName, Label errorLabel) {
        BooleanBinding isEmpty = GlobalValidation.isComboBoxEmpty(courseName);

        errorLabel.visibleProperty().bind(isEmpty);

        errorLabel.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng chọn khóa học!")
                .otherwise("")
        );
    }
}
