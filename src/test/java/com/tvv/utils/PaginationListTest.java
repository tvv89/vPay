package com.tvv.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginationListTest {
    List<String> list = new ArrayList<>();
    {
        for (int i = 1; i <= 15; i++) {
            list.add("string_"+i);
        }
    }
    int page;
    int count;

    @Test
    void testGetListPageNotEmpty() {
        page = 2;
        count = 4;
        List<String> assertionList = new ArrayList<>();
        for (int i = 5; i <=8 ; i++) {
            assertionList.add("string_"+i);
        }
        assertArrayEquals(assertionList.toArray(),PaginationList.getListPage(list,page,count).toArray());
    }

    @Test
    void testGetListPageNullEmpty() {
        page = 2;
        count = 4;
        List<String> assertionList = new ArrayList<>();
        List<String> listEmpty = new ArrayList<>();
        assertArrayEquals(assertionList.toArray(),PaginationList.getListPage(listEmpty,page,count).toArray());
    }

    @Test
    void testGetListPageOver() {
        page = 5;
        count = 4;
        List<String> assertionList = new ArrayList<>();
        for (int i = 13; i <=15 ; i++) {
            assertionList.add("string_"+i);
        }
        assertArrayEquals(assertionList.toArray(),PaginationList.getListPage(list,page,count).toArray());
    }

    @Test
    void testGetPages() {
        int pages = 1;
        count = 20;
        assertEquals(pages,PaginationList.getPages(list,count));

        pages = 4;
        count = 4;
        assertEquals(pages,PaginationList.getPages(list,count));

        pages = 15;
        count = 1;
        assertEquals(pages,PaginationList.getPages(list,count));

        pages = 3;
        count = 6;
        assertEquals(pages,PaginationList.getPages(list,count));
    }
}