package org.example.sysedu.validation;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.example.sysedu.exception.GlobalValidation;

public class DatePickerValidation {

    /**
     * Bắt lỗi trường ngày khai giảng
     * @param declarationDatePicker dữ liệu cần kiểm tra
     * @param errorBirthDayField lỗi khi bắt lỗi dữ liệu
     */
    public static void validateDeclarationDatePicker(DatePicker declarationDatePicker, Label errorBirthDayField) {
        BooleanBinding isBirthDateEmpty = GlobalValidation.isDatePickerEmpty(declarationDatePicker);

        BooleanBinding anyInvalid = isBirthDateEmpty;

        errorBirthDayField.visibleProperty().bind(anyInvalid);
        errorBirthDayField.textProperty().bind(
                Bindings.when(isBirthDateEmpty)
                        .then("Ngày khải giảng không được bỏ trống!")
                        .otherwise("")

        );
    }

    /**
     * Phương thức bắt lỗi tuổi của người học
     *
     * @param birthDatePicker    dữ liệu cần kiểm tra
     * @param errorBirthDayField lỗi nếu có
     */
    public static void validateBirthdayDatePicker(DatePicker birthDatePicker, Label errorBirthDayField) {
        BooleanBinding isBirthDateEmpty = GlobalValidation.isDatePickerEmpty(birthDatePicker);
        BooleanBinding isBirthDateValid = GlobalValidation.isBirthDateValid(birthDatePicker);
        BooleanBinding isBirthDateWithinAgeLimit = GlobalValidation.isBirthDateWithinAgeLimit(birthDatePicker);

        BooleanBinding anyInvalid = isBirthDateValid.or(isBirthDateWithinAgeLimit).or(isBirthDateEmpty);

        errorBirthDayField.visibleProperty().bind(anyInvalid);
        errorBirthDayField.textProperty().bind(
                Bindings.when(isBirthDateEmpty)
                        .then("Ngày sinh không được bỏ trống!")
                        .otherwise(Bindings.when(isBirthDateValid)
                                .then("Ngày sinh không hợp lệ!")
                                .otherwise(Bindings.when(isBirthDateWithinAgeLimit)
                                        .then("Ngày sinh không vượt quá 150 tuổi!")
                                        .otherwise("")
                                ))

        );
    }
}
