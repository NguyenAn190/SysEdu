package org.example.sysedu.validation;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.sysedu.exception.GlobalValidation;

public class NumberValidation {

    /**
     * Bắt lỗi học phí
     *
     * @param description dữ liệu cần kiểm tra
     * @param error       lỗi nếu có
     */
    public static void validationTuitionFeeField(TextField description, Label error) {
        BooleanBinding isEmpty = GlobalValidation.isTextFieldEmpty(description);
        BooleanBinding isNumberInvalid = GlobalValidation.isNumberValid(description).not();

        BooleanBinding anyInvalid = isEmpty.or(isNumberInvalid);

        error.visibleProperty().bind(anyInvalid);

        error.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng nhập giá học phí!")
                .otherwise(Bindings.when(isNumberInvalid)
                        .then("Giá học phí không hợp lệ")
                        .otherwise(""))
        );
    }

    /**
     * Bắt lỗi trường thời lượng
     *
     * @param time  dữ liệu cần bắt lỗi
     * @param error lỗi nếu có
     */
    public static void validationTimeField(TextField time, Label error) {
        BooleanBinding isEmpty = GlobalValidation.isTextFieldEmpty(time);
        BooleanBinding isNumberInvalid = GlobalValidation.isNumberValid(time).not();

        BooleanBinding anyInvalid = isEmpty.or(isNumberInvalid);

        error.visibleProperty().bind(anyInvalid);

        error.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng nhập thời lượng!")
                .otherwise(Bindings.when(isNumberInvalid)
                        .then("Thời lượng không hợp lệ")
                        .otherwise(""))
        );
    }

    /**
     * Phương thức bắt lỗi điểm
     * @param score dữ liệu cần kiểm tra
     * @param errorLabel lỗi nếu có
     */
    public static void scoreValidation(TextField score, Label errorLabel) {
        BooleanBinding isEmpty = GlobalValidation.isTextFieldEmpty(score);
        BooleanBinding isScoreInvalid = GlobalValidation.isScoreValid(score);

        BooleanBinding anyInvalid = isEmpty.or(isScoreInvalid.not());

        errorLabel.visibleProperty().bind(anyInvalid);
        errorLabel.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng nhập điểm")
                .otherwise(Bindings.when(isScoreInvalid.not())
                        .then("Điểm không hợp lệ")
                        .otherwise("")));
    }
}
