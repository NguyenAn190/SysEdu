package org.example.sysedu.validation;

public class FullNameValidation {
    public static String isValidateFullName(String username) {
        if(username.isEmpty()) {
            return "Họ và tên không được bỏ trống!";
        } else if(username.length() < 2) {
            return "Tên phải có ít nhất 2 kí tự!";
        } else if(username.length() > 50) {
            return "Tên phải có tối đa 50 kí tự!";
        } else {
            return "";
        }
    }
}
