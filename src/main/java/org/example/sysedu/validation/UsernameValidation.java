package org.example.sysedu.validation;

public class UsernameValidation {
    public static String isValidateUsername(String username) {
        if(username.isEmpty()) {
            return "Tên đăng nhập không được bỏ trống!";
        } else if(username.length() < 3) {
            return "Tên đăng nhập phải có ít nhất 3 kí tự!";
        } else if(username.length() > 30) {
            return "Tên đăng nhập phải có tối đa 30 kí tự!";
        } else {
            return "";
        }
    }
}
