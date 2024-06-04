package org.example.sysedu.utils;

public class GenerateID {
    public static String generateID(String character, String maxId) {
        if (maxId == null || maxId.isEmpty()) {
            return character + "000";
        }

        String numberPart = maxId.replaceAll("\\D", "");

        int maxNumber = Integer.parseInt(numberPart);

        int nextNumber = maxNumber + 1;

        String format = "000" + nextNumber;

        return character + String.format(format, nextNumber);
    }

}
