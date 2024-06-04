package org.example.sysedu.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CurrencyFormat {
    public static String format(BigDecimal value) {
        DecimalFormat myFormat = new DecimalFormat("###,###");
        return myFormat.format(value);
    }
}
