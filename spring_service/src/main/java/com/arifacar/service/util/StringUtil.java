package com.arifacar.service.util;

public class StringUtil {

    public static boolean isNothing(String str) {
        return (null == str || str.equals(""));
    }

    /**
     * return true if any string is null
     *
     * @param strings
     * @return
     */
    public static boolean isNothing(String... strings) {
        for (String str : strings) {
            if (null == str || str.equals(""))
                return true;
        }
        return false;
    }


    /**
     * @param str
     * @return
     */
    public static boolean isFullDigit(String str) {
        String regex = "[0-9]+";
        return !isNothing(str) && str.matches(regex);
    }


}
