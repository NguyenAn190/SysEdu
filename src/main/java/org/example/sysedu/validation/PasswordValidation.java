package org.example.sysedu.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class PasswordValidation {
    public static String isValidatePassword(String password) {
        if(password.isEmpty()) {
            return "Mật khẩu không được bỏ trống!";
        } else if(password.length() <= 5) {
            return "Mật khẩu phải có ít nhất 6 kí tự!";
        } else if(password.length() > 30) {
            return "Mật khẩu phải có tối đa 30 kí tự!";
        } else {
            return "";
//            // Kiểm tra xem mật khẩu có chứa ít nhất một ký tự đặc biệt hay không
//            Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");
//            Matcher matcher = specialCharPattern.matcher(password);
//            if (!matcher.find()) {
//                return "Mật khẩu phải chứa ít nhất một ký tự đặc biệt!";
//            } else {
//                return "";
//            }
        }
    }

    public static String isValidateConfirmPassword(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)) {
            return "Mật khẩu nhập lại không trùng khớp!";
        } else {
            return "";
        }
    }
}
