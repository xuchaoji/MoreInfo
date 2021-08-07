package com.xuchaoji.craft.moreinfo.util;

import java.util.List;

public class ListUtil {
    public static <T> boolean isEmpty(List<T> list) {
        return null == list || list.size() == 0;
    }
}
