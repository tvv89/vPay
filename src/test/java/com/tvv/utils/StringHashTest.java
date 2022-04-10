package com.tvv.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHashTest {

    @Test
    void testGetHashString() {
        String inputString = "admin";
        String assertString = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";
        assertEquals(assertString,StringHash.getHashString(inputString));
        inputString = "Password123.";
        assertString = "fdd041272cbb46ef43ceb4032ca730663a18326d441aeddd395e42e890641ab5";
        assertEquals(assertString,StringHash.getHashString(inputString));
        inputString = "p@ssword_075";
        assertString = "2d28de13d89f8015089b5f677621ded941ad3ec676921a97a75dbfa9516bc291";
        assertEquals(assertString,StringHash.getHashString(inputString));
        inputString = " ";
        assertString = "36a9e7f1c95b82ffb99743e0c5c4ce95d83c9a430aac59f84ef3cbfab6145068";
        assertEquals(assertString,StringHash.getHashString(inputString));

    }

    @Test
    void testGetHashNullString() {
        String inputString = "";
        String assertString = "";
        assertEquals(assertString,StringHash.getHashString(inputString));
        inputString = null;
        assertString = "";
        assertEquals(assertString,StringHash.getHashString(inputString));

    }
}