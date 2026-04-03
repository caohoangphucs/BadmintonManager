package com.example.winfinal.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormatUtils {
    public static String formatCurrency(Object value) {
        if (value == null) return "0";
        try {
            double doubleValue;
            if (value instanceof Number) {
                doubleValue = ((Number) value).doubleValue();
            } else {
                doubleValue = Double.parseDouble(value.toString());
            }
            DecimalFormat formatter = new DecimalFormat("#,###");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            formatter.setDecimalFormatSymbols(symbols);
            return formatter.format(doubleValue);
        } catch (Exception e) {
            return value.toString();
        }
    }
}
