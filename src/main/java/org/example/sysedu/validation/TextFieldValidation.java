package org.example.sysedu.validation;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.sysedu.exception.GlobalValidation;
import org.example.sysedu.service.LearnersService;
import org.example.sysedu.service.TopicsService;

public class TextFieldValidation {

    TopicsService topicsService = new TopicsService();
    /**
     * Phương thức bắt lỗi họ và tên của người học
     *
     * @param fullNameField      dữ liệu cần kiểm tra
     * @param errorFullNameField lỗi nếu có
     */
    public static void validateFullNameField(TextField fullNameField, Label errorFullNameField) {
        BooleanBinding isInvalid = GlobalValidation.isTextFieldEmpty(fullNameField);
        BooleanBinding isTooShort = GlobalValidation.isTextFieldLengthLessThan(fullNameField, 2);
        BooleanBinding isTooLong = GlobalValidation.isTextFieldLengthGreaterThan(fullNameField, 50);
        BooleanBinding hasSpecialCharacters = GlobalValidation.containsSpecialCharacters(fullNameField);

        BooleanBinding anyInvalid = isInvalid.or(isTooShort).or(isTooLong).or(hasSpecialCharacters);

        errorFullNameField.visibleProperty().bind(anyInvalid);
        errorFullNameField.textProperty().bind(Bindings.when(isInvalid)
                .then("Vui lòng nhập họ và tên!")
                .otherwise(Bindings.when(isTooShort)
                        .then("Họ và tên phải nhiều hơn 2 kí tự!")
                        .otherwise(Bindings.when(isTooLong)
                                .then("Họ và tên phải ít hơn 50 kí tự!")
                                .otherwise(Bindings.when(hasSpecialCharacters)
                                        .then("Họ và tên không được chứa ký tự đặc biệt!")
                                        .otherwise("")

                                )
                        )
                )

        );
    }

    /**
     * Phương thức validation trường số điện thoại
     *
     * @param phone           dữ liệu cần kiểm tra
     * @param errorPhoneLabel lỗi nếu có
     */
    public static void validatePhoneNumberField(TextField phone, Label errorPhoneLabel) {
        LearnersService learnersService = new LearnersService();
        BooleanBinding isEmptyPhone = GlobalValidation.isTextFieldEmpty(phone);
        BooleanBinding isPhoneValid = GlobalValidation.isValidVietnamesePhoneNumber(phone);
        BooleanBinding isTooShort = GlobalValidation.isTextFieldLengthLessThan(phone, 9);
        BooleanBinding isTooLong = GlobalValidation.isTextFieldLengthGreaterThan(phone, 13);
        BooleanBinding existsPhone = new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return learnersService.existsPhone(phone.getText().trim());
            }
        };

        BooleanBinding anyInvalid = isEmptyPhone.or(isPhoneValid).or(isTooShort).or(isTooLong);

        errorPhoneLabel.visibleProperty().bind(anyInvalid);
        errorPhoneLabel.textProperty().bind(Bindings.when(isEmptyPhone)
                .then("Số điện thoại không được bỏ trống!")
                .otherwise(Bindings.when(isPhoneValid)
                        .then("Số điện thoại không hợp lệ!")
                        .otherwise(Bindings.when(isTooShort)
                                .then("Số điện thoại không hợp lệ")
                                .otherwise(Bindings.when(isTooLong)
                                        .then("Số điện thoại không hợp lệ")
                                        .otherwise(Bindings.when(existsPhone)
                                                .then("Số điện thoại đã tồn tại")
                                                .otherwise(""))
                                )
                        )
                )
        );
    }

    /**
     * Phương thức validation trường số điện thoại khi cập nhật
     *
     * @param phone           dữ liệu cần kiểm tra
     * @param errorPhoneLabel lỗi nếu có
     */
    public static void validatePhoneNumberUpdateField(TextField phone, Label errorPhoneLabel) {
        BooleanBinding isEmptyPhone = GlobalValidation.isTextFieldEmpty(phone);
        BooleanBinding isPhoneValid = GlobalValidation.isValidVietnamesePhoneNumber(phone);
        BooleanBinding isTooShort = GlobalValidation.isTextFieldLengthLessThan(phone, 9);
        BooleanBinding isTooLong = GlobalValidation.isTextFieldLengthGreaterThan(phone, 13);

        BooleanBinding anyInvalid = isEmptyPhone.or(isPhoneValid).or(isTooShort).or(isTooLong);

        errorPhoneLabel.visibleProperty().bind(anyInvalid);
        errorPhoneLabel.textProperty().bind(Bindings.when(isEmptyPhone)
                .then("Số điện thoại không được bỏ trống!")
                .otherwise(Bindings.when(isPhoneValid)
                        .then("Số điện thoại không hợp lệ!")
                        .otherwise(Bindings.when(isTooShort)
                                .then("Số điện thoại không hợp lệ")
                                .otherwise(Bindings.when(isTooLong)
                                        .then("Số điện thoại không hợp lệ")
                                        .otherwise(""))
                        )
                )
        );
    }

    /**
     * Phương thức bắt lỗi email
     *
     * @param email           dữ liệu cần kiểm tra
     * @param errorEmailField lỗi nếu có
     */
    public static void validateEmailField(TextField email, Label errorEmailField) {
        LearnersService learnersService = new LearnersService();
        BooleanBinding isEmptyEmail = GlobalValidation.isTextFieldEmpty(email);
        BooleanBinding isEmailValid = GlobalValidation.isValidEmail(email);
        BooleanBinding existsEmail = new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return learnersService.existsEmail(email.getText().trim());
            }
        };

        BooleanBinding anyInvalid = isEmptyEmail.or(isEmailValid).or(existsEmail);

        errorEmailField.visibleProperty().bind(anyInvalid);
        errorEmailField.textProperty().bind(Bindings.when(isEmptyEmail)
                .then("Email không được bỏ trống!")
                .otherwise(Bindings.when(isEmailValid)
                        .then("Email không hợp lệ!")
                        .otherwise(Bindings.when(anyInvalid)
                                .then("Email đã tồn tại!")
                                .otherwise(""))
                )
        );
    }

    /**
     * Phương thức bắt lỗi email khi cập nhật
     *
     * @param email           dữ liệu cần kiểm tra
     * @param errorEmailField lỗi nếu có
     */
    public static void validateEmailUpdateField(TextField email, Label errorEmailField) {
        BooleanBinding isEmptyEmail = GlobalValidation.isTextFieldEmpty(email);
        BooleanBinding isEmailValid = GlobalValidation.isValidEmail(email);

        BooleanBinding anyInvalid = isEmptyEmail.or(isEmailValid);

        errorEmailField.visibleProperty().bind(anyInvalid);
        errorEmailField.textProperty().bind(Bindings.when(isEmptyEmail)
                .then("Email không được bỏ trống!")
                .otherwise(Bindings.when(isEmailValid)
                        .then("Email không hợp lệ!")
                        .otherwise("")
                )
        );
    }

    public static void validationNameField(TextField nameField, Label error) {
        BooleanBinding isEmpty = GlobalValidation.isTextFieldEmpty(nameField);
        BooleanBinding isNameValid = GlobalValidation.containsSpecialCharacters(nameField);

        BooleanBinding anyInvalid = isEmpty.or(isNameValid);

        error.visibleProperty().bind(anyInvalid);
        error.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng nhập tên chuyên đề!")
                .otherwise(Bindings.when(isNameValid)
                        .then("Tên chuyên đề không được chứa ký tự đặc biệt!")
                        .otherwise(""))
        );
    }

    public static void validationTopicNameCreatedField(TextField nameField, Label error) {

        BooleanBinding isEmpty = GlobalValidation.isTextFieldEmpty(nameField);
        BooleanBinding isExitsTopicName = GlobalValidation.isExistTopicName(nameField);

        BooleanBinding anyInvalid = isEmpty.or(isExitsTopicName);

        error.visibleProperty().bind(anyInvalid);
        error.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng nhập tên chuyên đề!")
                .otherwise(Bindings.when(isExitsTopicName)
                        .then("Tên chuyên đề đã tồn tại!")
                        .otherwise(""))
        );
    }

    public static void validationUsername(TextField username, Label errorLabel) {
        BooleanBinding isEmpty = GlobalValidation.isTextFieldEmpty(username);
        BooleanBinding isUsernameLengthGreaterThan = GlobalValidation.isTextFieldLengthGreaterThan(username, 50);
        BooleanBinding isUserNameLengthLessThan = GlobalValidation.isTextFieldLengthLessThan(username, 3);

        BooleanBinding anyInvalid = isEmpty.or(isUsernameLengthGreaterThan).or(isUserNameLengthLessThan);

        errorLabel.visibleProperty().bind(anyInvalid);

        errorLabel.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng nhập tên đăng nhập!")
                .otherwise(Bindings.when(isUserNameLengthLessThan)
                        .then("Tên đăng nhập phải lớn hơn 3 kí tự!")
                        .otherwise(Bindings.when(isUsernameLengthGreaterThan)
                                .then("Tên đăng nhập phải nhỏ hơn 30 kí tự!")
                                .otherwise("")))
        );
    }

    public static void validationPasswords(TextField passwordField, Label errorLabel) {
        BooleanBinding isEmpty = GlobalValidation.isTextFieldEmpty(passwordField);
        BooleanBinding isPasswordLengthGreaterThan = GlobalValidation.isTextFieldLengthGreaterThan(passwordField, 50);
        BooleanBinding isPasswordLengthLessThan = GlobalValidation.isTextFieldLengthLessThan(passwordField, 3);

        BooleanBinding anyInvalid = isEmpty.or(isPasswordLengthGreaterThan).or(isPasswordLengthLessThan);

        errorLabel.visibleProperty().bind(anyInvalid);

        errorLabel.textProperty().bind(Bindings.when(isEmpty)
                .then("Vui lòng nhập mật khẩu!")
                .otherwise(Bindings.when(isPasswordLengthLessThan)
                        .then("Mật khẩu phải lớn hơn 3 kí tự!")
                        .otherwise(Bindings.when(isPasswordLengthGreaterThan)
                                .then("Mật khẩu phải nhỏ hơn 50 kí tự")
                                .otherwise(""))));

    }
}
