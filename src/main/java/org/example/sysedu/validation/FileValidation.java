package org.example.sysedu.validation;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.example.sysedu.exception.GlobalValidation;

import java.util.Arrays;

public class FileValidation {
    /**
     * Bắt lỗi hình ảnh
     * @param avatar dữ liệu cần kiểm tra
     * @param error lỗi nếu có
     */
    public static void validationAvatar(ImageView avatar, Label error) {
        BooleanBinding isEmpty = GlobalValidation.isImageViewEmpty(avatar);
        BooleanBinding isImageValid = GlobalValidation.isImageValidFormat(avatar, Arrays.asList(".jpg", ".jpeg", ".png", ".gif"));

        BooleanBinding anyInvalid = isEmpty.or(isImageValid);

        error.visibleProperty().bind(anyInvalid);

        error.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng chọn ảnh đại diện!")
                .otherwise(Bindings.when(isImageValid)
                        .then("Ảnh không hợp lệ")
                        .otherwise(""))
        );

        System.out.println(error.getText());
    }
}
