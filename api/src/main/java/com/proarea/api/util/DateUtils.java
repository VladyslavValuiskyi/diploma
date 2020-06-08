package com.proarea.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    private static final String SIMPLE_DAY = "yy.MM.dd";

    private static final String TIME_FORMAT = "HH:mm";

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

    public static LocalDateTime asLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE);
    }

    public static Long toEpochMilli(LocalDateTime date) {
        return date.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    public static Instant toInstant(LocalDateTime date) {
        return date.atZone(DEFAULT_ZONE).toInstant();
    }

    public static Long nowEpochMilli() {
        return toEpochMilli(now());
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(DEFAULT_ZONE);
    }

    public static Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(DEFAULT_ZONE).toInstant());
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    public static LocalDateTime asLocalDateTime(Long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    public static String getDateWithoutTime(Long timestamp) {
        return format(new Date(timestamp), SIMPLE_DAY);
    }


    public static String getDateTime(Long timestamp) {
        return format(new Date(timestamp), TIME_FORMAT);
    }

    public static Date getDataFromString(String date) {
        return parse(date, SIMPLE_DAY);
    }

    private static String format(Date date, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    private static Date parse(String date, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

}
