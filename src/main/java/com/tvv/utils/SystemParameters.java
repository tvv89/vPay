package com.tvv.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * System parameters
 */
public class SystemParameters {
    /**
     * Payment commission for outside recipient
     */
    public static final Double COMMISSION_PERCENT = 0.05;
    /**
     * Min sum of payment
     */
    public static final Double MIN_PAYMENT_SUM = 1D;
    /**
     * Max sum of payment
     */
    public static final Double MAX_PAYMENT_SUM = 100000D;

    /**
     * Function for select resources with language name
     * @param local String locale ISO 639-1
     * @return ResourceBundle for use in code
     * @throws IOException
     */
    public static ResourceBundle getLocale(String local) throws IOException {
        Locale locale = new Locale(local);
        ResourceBundle message = ResourceBundle.getBundle("resources",locale);
        return message;
    }

    public static Map<String,String> jsLanguagePack(String local) {
        Map<String,String> result = new HashMap<>();
        Locale locale = new Locale(local);
        ResourceBundle data = ResourceBundle.getBundle("resources",locale);
        List<String> keyList = data.keySet()
                .stream()
                .filter(value->value.contains("javascript."))
                .collect(Collectors.toList());
        for (String key: keyList) {
            String keys = key.replace(".", "_");
            result.put(keys,data.getString(key));
        }

        return result;
    }

}
