package com.horse.config.security;

import java.util.HashMap;
import java.util.Map;

public class SingletonClass {

    private static Map<String, Integer> dbObject;

    private SingletonClass() {

    }

    public static Map<String, Integer> getDbObject() {
        if (dbObject == null) {
            return dbObject = new HashMap<>();
        } else {
            return dbObject;
        }
    }

    public static void setDbObject(String key, Integer account_id) {
        if (dbObject == null) {
            dbObject = new HashMap<>();
        }
        dbObject.put(key, account_id);
    }
}
