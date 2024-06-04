package org.example.sysedu.dto.usersDTO;

import org.example.sysedu.utils.EncryptionUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserSession {

    private static final String TEMP_FILE = "D:\\FPT Polytechnic\\SysEdu\\SysEdu\\src\\main\\resources\\org\\example\\sysedu\\file\\user_session.txt";
    private static UserSession instance;

    private String username;
    private LocalDateTime expirationTime;

    private UserSession() {
        // Private constructor to prevent instantiation
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void saveSessionToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE))) {
            String data = username + "\n" + expirationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String encryptedData = EncryptionUtil.encrypt(data);
            writer.write(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSessionFromFile() throws Exception {
        File file = new File(TEMP_FILE);
        if (file.exists()) {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String encryptedData = reader.readLine();
            String decryptedData = EncryptionUtil.decrypt(encryptedData);
            String[] data = decryptedData.split("\n");
            this.username = data[0];
            this.expirationTime = LocalDateTime.parse(data[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    public void clearSession() {
        username = null;
        expirationTime = null;
        File file = new File(TEMP_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}

