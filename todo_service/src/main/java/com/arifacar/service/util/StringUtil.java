package com.arifacar.service.util;

import com.arifacar.domain.model.user.User;

import java.util.Date;

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

    /**
     * @return
     */
    public static String generateUniqueFileName() {
        String filename = "";
        long millis = System.currentTimeMillis();
        String datetime = new Date().toGMTString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        filename = datetime + "_" + millis;
        return filename;
    }


    public static void trimUser(User user) {
        if (user.getName() != null) user.setName(user.getName().trim());
        if (user.getSurname() != null) user.setSurname(user.getSurname().trim());
        if (user.getUsername() != null) user.setUsername(user.getUsername().trim());
        if (user.getEmail() != null) user.setEmail(user.getEmail().trim());
        if (user.getProfilePic() != null) user.setProfilePic(user.getProfilePic().trim());
    }
}
