package de.ndhbr.ynvest.util;

import java.util.Random;

public class StringUtils {

    public static String generateRandomPassword(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz!@#$%&";

        for (int i = 0; i < length; i++)
            sb.append(chars.charAt(random.nextInt(chars.length())));

        return sb.toString();
    }
}
