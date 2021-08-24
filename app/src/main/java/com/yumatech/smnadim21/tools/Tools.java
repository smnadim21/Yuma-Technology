package com.yumatech.smnadim21.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.yumatech.smnadim21.db.entity.BaseDB.DATE_TIME_FORMAT;

public class Tools {

    public static String getCurrentTimeString() {
        return new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH).format(Calendar.getInstance().getTime());
    }
}
