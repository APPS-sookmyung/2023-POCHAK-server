package com.apps.pochak.global.converter;

import java.util.List;

public class LongListToStringConverter {
    public static String convertLongListToString(List<Long> longList) {
        String result = "";

        for (Long l : longList) {
            result += (l + ",");
        }

        return result;
    }
}
