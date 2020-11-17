package com.arifacar.common.util;

import java.util.List;

public class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * get first item of list
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T first(List<T> list) {
        if (list == null)
            return null;
        return list.get(0);
    }

    /**
     * get last item of list
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T last(List<T> list) {
        if (list == null)
            return null;
        return list.get(list.size() - 1);
    }

    /**
     * Check item exist in int array.
     *
     * @param arr
     * @param item
     * @return
     */
    public static boolean containsIntArray(int[] arr, int item) {
        for (int n : arr) {
            if (item == n) {
                return true;
            }
        }
        return false;
    }

}
