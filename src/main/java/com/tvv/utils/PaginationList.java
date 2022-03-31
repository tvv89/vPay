package com.tvv.utils;

import java.util.ArrayList;
import java.util.List;

public class PaginationList {

    public static <T> List<T> getListPage(List<T> tList, int page, int count) {
        List<T> result = new ArrayList<>();
        if (tList==null || tList.isEmpty()) return result;

        int pages = (int)Math.ceil((double)tList.size()/count);

        if (page>pages) return tList.subList(count*(pages-1), tList.size());

        if (page< pages) result = tList.subList(count*(page-1), count*page);
        else result = tList.subList(count*(pages-1), tList.size());
        return result;
    }

    public static <T> int getPages(List<T> tList, int count) {
        return (int)Math.ceil((double)tList.size()/count);
    }

}
