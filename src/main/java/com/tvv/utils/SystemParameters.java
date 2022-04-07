package com.tvv.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    public static Properties getLocale(String locale) throws IOException {
        Properties properties = new Properties();
        String fl = locale;
        if (fl==null || fl.isEmpty()) fl="en";
        String filePath = "resources_"+fl+".properties";
        try (InputStream input = SystemParameters.class.getClassLoader().getResourceAsStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
