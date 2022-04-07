package com.tvv.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    public static ResourceBundle getLocale(String local) throws IOException {
        Locale locale = new Locale(local);
        ResourceBundle message = ResourceBundle.getBundle("resources",locale);
        return message;
    }

}
