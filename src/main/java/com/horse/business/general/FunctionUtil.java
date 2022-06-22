package com.horse.business.general;

public class FunctionUtil {
    public static boolean checkBlank(String string) {
        if (string.replaceAll(" ", "").length() == 0)
            return false;
        return true;
    }
}
