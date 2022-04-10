package com.tvv.utils;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UtilsGeneratorTest {

    @Test
    void getGUID() {
        String guid = UtilsGenerator.getGUID();
        String regex = "[0-9]{10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(guid);
        assertTrue(matcher.matches());
    }

    @Test
    void getAccountUID() {
        String uid = UtilsGenerator.getAccountUID();
        String regex = "[a-zA-Z0-9]{25}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uid);
        assertTrue(matcher.matches());
    }
}