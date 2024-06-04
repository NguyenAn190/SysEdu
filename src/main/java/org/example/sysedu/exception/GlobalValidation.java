package org.example.sysedu.exception;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.service.TopicsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GlobalValidation {
    /**
     * Phương thức bắt lỗi trường dữ liệu trống
     * @param textField dữ liệu bắt lỗi
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isTextFieldEmpty(TextField textField) {
        return Bindings.createBooleanBinding(
                () -> textField.getText().trim().isEmpty(),
                textField.textProperty()
        );
    }

    /**
     * Bắt lỗi số kí tự tối thiểu phải nhập vào
     * @param textField dữ liệu bắt lỗi
     * @param minLength số lượng kí tự nhỏ nhất
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isTextFieldLengthLessThan(TextField textField, int minLength) {
        return Bindings.createBooleanBinding(
                () -> textField.getText().trim().length() < minLength,
                textField.textProperty()
        );
    }

    /**
     * Bắt lỗi số lượng kí tự tối đa được nhập vào
     * @param textField dữ liệu muốn bắt lỗi
     * @param maxLength số lượng kí tự tối đa
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isTextFieldLengthGreaterThan(TextField textField, int maxLength) {
        return Bindings.createBooleanBinding(
                () -> textField.getText().trim().length() > maxLength,
                textField.textProperty()
        );
    }

    /**
     * Bắt lỗi email có hợp lệ hay không
     * @param textField dữ liệu muốn bắt lỗi
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isValidEmail(TextField textField) {
        return Bindings.createBooleanBinding(
                () -> {
                    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                    return !textField.getText().matches(emailRegex);
                },
                textField.textProperty()
        );
    }

    /**
     * Bắt lỗi kí tự đặc biệt
     * @param textField dữ liệu muốn bắt lỗi
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding containsSpecialCharacters(TextField textField) {
        return Bindings.createBooleanBinding(
                () -> !textField.getText().matches("[a-zA-Z\\sÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẮẰẲẴẶâầấẩẫậắằẵẳặàảãáạèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộùúủũụưừứửữựỳýỷỹỵ]+"),
                textField.textProperty()
        );
    }

    /**
     * Bắt lỗi ngày sinh có lớn hơn ngày hiện tại hay không
     * @param birthDate dữ liệu bắt lỗi
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isBirthDateValid(DatePicker birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Bindings.createBooleanBinding(
                () -> {
                    LocalDate selectedDate = birthDate.getValue();
                    return selectedDate != null && !selectedDate.isBefore(currentDate);
                },
                birthDate.valueProperty()
        );
    }

    /**
     * Bắt lỗi ngày sinh có quá 150 tuổi hay không
     * @param birthDate dữ liệu cần bắt lỗi
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isBirthDateWithinAgeLimit(DatePicker birthDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate maxAllowedDate = currentDate.minusYears(150);
        return Bindings.createBooleanBinding(
                () -> {
                    LocalDate selectedDate = birthDate.getValue();
                    return selectedDate != null && selectedDate.isBefore(maxAllowedDate);
                },
                birthDate.valueProperty()
        );
    }

    /**
     * Bắt lỗi số điện thoại theo định dạng việt nam
     * @param phoneNumberField dữ liệu cần kiểm tra
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isValidVietnamesePhoneNumber(TextField phoneNumberField) {
        return Bindings.createBooleanBinding(
                () -> {
                    String phoneNumber = phoneNumberField.getText().trim();
                    // Kiểm tra số điện thoại theo định dạng phổ biến của Việt Nam
                    return phoneNumber.matches("(0[1-9][0-9]{9,11})|(\\+84[1-9][0-9]{10})");
                },
                phoneNumberField.textProperty()
        );
    }

    /**
     * Bắt lỗi ngày bỏ trống
     * @param birthDatePicker dữ liệu cần kiểm tra
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isDatePickerEmpty(DatePicker birthDatePicker) {
        return Bindings.createBooleanBinding(
                () -> birthDatePicker.getValue() == null,
                birthDatePicker.valueProperty()
        );
    }

    /**
     * Phương thức bắt lỗi số
     * @param numberField dữ liệu cần kiểm tra
     * @return trả về lỗi nếu có
     */
    public static BooleanBinding isNumberValid(TextField numberField) {
        return Bindings.createBooleanBinding(() -> {
            String text = numberField.getText();
            if (text == null || text.trim().isEmpty()) {
                return false;
            }
            try {
                int value = Integer.parseInt(text.trim());
                return value > 0 && value < 1000000000;
            } catch (NumberFormatException e) {
                return false;
            }
        }, numberField.textProperty());
    }

    public static BooleanBinding isImageViewEmpty(ImageView imageView) {
        return Bindings.createBooleanBinding(
                () -> imageView.getImage() == null,
                imageView.imageProperty()
        );
    }

    public static BooleanBinding isImageValidFormat(ImageView imageView, List<String> validFormats) {
        return Bindings.createBooleanBinding(() -> {
            Image image = imageView.getImage();
            if (image == null) {
                return false;
            }
            String url = image.getUrl();

            if (url == null) {
                return false;
            }
            String extension = getFileExtension(url);
            return validFormats.contains(extension.toLowerCase());
        }, imageView.imageProperty());
    }

    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * Bắt lỗi combobox rỗng
     * @param topicComboBox dữ liệu cần kiểm tra
     * @return lỗi nếu có
     */
    public static BooleanBinding isComboBoxEmpty(ComboBox topicComboBox) {
        return Bindings.createBooleanBinding(
                () -> topicComboBox.getSelectionModel().getSelectedItem() == null,
                topicComboBox.getSelectionModel().selectedItemProperty()
        );
    }

    /**
     * Bắt lỗi list rỗng
     * @param list dữ liệu danh sách cần kiểm tra
     * @return lỗi nếu có
     */
    public static BooleanBinding isListEmpty(ObservableList<String> list) {
        return Bindings.createBooleanBinding(
                () -> list.isEmpty(),
                list
        );
    }

    public static BooleanBinding isExistTopicName(TextField topicName) {
        return Bindings.createBooleanBinding(() -> {
            TopicsService topicsService = new TopicsService();
            Optional<Topics> topics = topicsService.findTopicByName(topicName.getText());

            // Trả về true nếu chủ đề tồn tại, false nếu không tồn tại
            return topics.isPresent();
        }, topicName.textProperty());
    }


    public static BooleanBinding isScoreValid(TextField score) {
        return Bindings.createBooleanBinding(() -> {
            String text = score.getText();
            if (text == null || text.trim().isEmpty()) {
                return true; // Consider empty field as valid to separate concerns
            }
            try {
                int value = Integer.parseInt(text.trim());
                return value >= 0 && value <= 10;
            } catch (NumberFormatException e) {
                return false;
            }
        }, score.textProperty());
    }

}
