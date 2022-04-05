package com.tvv.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination functions class
 */
public class PaginationList {

    /**
     * Create list per current page = 'page'
     * @param tList input List type 'T'
     * @param page page what we want to show
     * @param count item per page
     * @param <T> objects (User, Account, Payment, Card)
     * @return List of object per 'page'
     */
    public static <T> List<T> getListPage(List<T> tList, int page, int count) {
        List<T> result = new ArrayList<>();
        if (tList==null || tList.isEmpty()) return result;

        int pages = (int)Math.ceil((double)tList.size()/count);

        if (page>pages) return tList.subList(count*(pages-1), tList.size());

        if (page< pages) result = tList.subList(count*(page-1), count*page);
        else result = tList.subList(count*(pages-1), tList.size());
        return result;
    }

    /**
     * Get Max pages for input list
     * @param tList List of object
     * @param count items per page
     * @param <T> type of object
     * @return int value pages
     */
    public static <T> int getPages(List<T> tList, int count) {
        return (int)Math.ceil((double)tList.size()/count);
    }

}
