package com.horse.business.general;

import com.horse.config.exceptionHandler.GeneralExceptionHandler;

import static org.apache.logging.log4j.util.Strings.isBlank;

public class FunctionUtil {
    public static void checkBlank(String string, String name) {
//        if (string.replaceAll(" ", "").length() == 0) {
//            throw new GeneralExceptionHandler(name + " should not be blank");
//        }
        if (isBlank(string)) {
            throw new GeneralExceptionHandler(name + " should not be blank");
        }
    }

    public static void checkAgeTrainer(Integer age) {
        if (age < 18 || age > 60) {
            throw new GeneralExceptionHandler("age must be between 18 and 60");
        }
    }


}
